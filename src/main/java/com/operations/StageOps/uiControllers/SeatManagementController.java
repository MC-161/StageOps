package com.operations.StageOps.uiControllers;

import com.operations.StageOps.model.Room;
import com.operations.StageOps.model.Seating;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class SeatManagementController {

    @FXML
    private ComboBox<String> roomSelectionComboBox;
    @FXML
    private GridPane seatGrid;
    @FXML
    private Button saveSeatsButton, closeButton;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8080/api";

    private final Map<String, Integer> roomIdMap = new HashMap<>();
    private final List<Seating> seatList = new ArrayList<>();

    @FXML
    public void initialize() {
        fetchRooms(); // Load rooms into dropdown

        roomSelectionComboBox.setOnAction(event -> {
            String selectedRoom = roomSelectionComboBox.getValue();
            if (selectedRoom != null) {
                fetchSeats(roomIdMap.get(selectedRoom)); // Fetch seats for selected room
            }
        });

        saveSeatsButton.setOnAction(event -> saveSeatChanges());
    }

    private void fetchRooms() {
        Task<List<Room>> task = new Task<>() {
            @Override
            protected List<Room> call() {
                return Arrays.asList(restTemplate.getForObject(BASE_URL + "/rooms", Room[].class));
            }
        };

        task.setOnSucceeded(event -> {
            List<Room> rooms = task.getValue();
            roomSelectionComboBox.getItems().clear();
            for (Room room : rooms) {
                roomSelectionComboBox.getItems().add(room.getRoomName());
                roomIdMap.put(room.getRoomName(), room.getRoomId());
            }
        });

        new Thread(task).start();
    }

    private void fetchSeats(int roomId) {
        Task<List<Seating>> task = new Task<>() {
            @Override
            protected List<Seating> call() {
                return Arrays.asList(restTemplate.getForObject(BASE_URL + "/seating/room/" + roomId, Seating[].class));
            }
        };

        task.setOnSucceeded(event -> {
            seatList.clear();
            seatList.addAll(task.getValue());
            displaySeats();
        });

        new Thread(task).start();
    }

    private void displaySeats() {
        seatGrid.getChildren().clear();

        int seatsPerRow = 10; // How many seats to display per row
        int row = 0;

        // Group seats by section
        Map<String, List<Seating>> sections = groupSeatsBySection(seatList);

        // Iterate over each section and display the seats
        for (Map.Entry<String, List<Seating>> section : sections.entrySet()) {
            // Create a VBox or HBox for each section (this makes it more flexible)
            HBox sectionGroup = new HBox(10); // Horizontal Box for row arrangement
            Label sectionLabel = new Label("Section: " + section.getKey());
            sectionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
            sectionGroup.getChildren().add(sectionLabel);

            // Iterate through seats in the section, creating a row of seats
            for (Seating seat : section.getValue()) {
                StackPane seatPane = createSeat(seat); // Use the existing createSeat method
                sectionGroup.getChildren().add(seatPane);
            }

            // Add the section to the grid in the next available row
            seatGrid.add(sectionGroup, 0, row);

            // Increment row after adding each section group (you can add sections in rows)
            row++;
        }
    }

    private Map<String, List<Seating>> groupSeatsBySection(List<Seating> seatList) {
        Map<String, List<Seating>> sections = new HashMap<>();

        // Group seats by section name
        for (Seating seat : seatList) {
            String sectionName = seat.getSectionName(); // Assuming seat has a section name field
            sections.computeIfAbsent(sectionName, k -> new ArrayList<>()).add(seat);
        }

        return sections;
    }



    // In SeatManagementController.java

    private StackPane createSeat(Seating seat) {
        // Define the size of the seat
        Rectangle seatShape = new Rectangle(40, 40); // Increase the size for better visibility
        Color seatColor;

        // Determine seat color based on its status and accessibility
        if (seat.getStatus().equals("reserved")) {
            seatColor = Color.RED; // Reserved seats are red
        } else if (seat.isAccessible()) {
            seatColor = Color.BLUE; // Accessible seats are blue
        } else if (seat.isRestricted()) {
            seatColor = Color.GRAY; // Restricted seats are gray
        } else {
            seatColor = Color.GREEN; // Default to green if not reserved, accessible, or restricted
        }

        seatShape.setFill(seatColor);
        seatShape.setStroke(Color.BLACK);

        // Hover effect for better interactivity
        seatShape.setOnMouseEntered(event -> seatShape.setFill(Color.YELLOW)); // Change color on hover
        seatShape.setOnMouseExited(event -> seatShape.setFill(seatColor)); // Revert to original color

        // Seat number label with better visibility
        Label seatNumberLabel = new Label(String.valueOf(seat.getSeatNumber()));
        seatNumberLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        StackPane.setAlignment(seatNumberLabel, Pos.CENTER);

        // Add a section label if available
        Label sectionLabel = new Label(seat.getSectionName());
        sectionLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: white;");
        StackPane.setAlignment(sectionLabel, Pos.TOP_CENTER);

        // Group the seat and section label together
        StackPane seatPane = new StackPane(seatShape, seatNumberLabel, sectionLabel);

        // Make the seat clickable
        seatShape.setOnMouseClicked(event -> openSeatManagementWindow(seat));

        return seatPane;
    }



    private void openSeatManagementWindow(Seating seat) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Manage Seat - " + seat.getSeatNumber());

        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 10;");

        // Seat details
        Label seatLabel = new Label("Seat: " + seat.getSectionName() + "-" + seat.getSeatNumber());
        Label statusLabel = new Label("Current Status: " + seat.getStatus());
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("available", "reserved", "occupied");
        statusComboBox.setValue(seat.getStatus());

        // Accessible and Restricted options
        CheckBox accessibleCheckBox = new CheckBox("Accessible");
        accessibleCheckBox.setSelected(seat.isAccessible());

        CheckBox restrictedCheckBox = new CheckBox("Restricted View");
        restrictedCheckBox.setSelected(seat.isRestricted());

        // Add components to content VBox
        content.getChildren().addAll(seatLabel, statusLabel, statusComboBox, accessibleCheckBox, restrictedCheckBox);
        dialog.getDialogPane().setContent(content);

        // Define the buttons (Save, Cancel)
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);

        // Define how to handle the dialog result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                // Save the changes if the "Save" button is clicked
                seat.setStatus(statusComboBox.getValue());
                seat.setAccessible(accessibleCheckBox.isSelected());
                seat.setRestricted(restrictedCheckBox.isSelected());

                // Log the updated seat information for debugging
                System.out.println("Seat Updated: " + seat.getSeatNumber() +
                        " | Status: " + seat.getStatus() +
                        " | Accessible: " + seat.isAccessible() +
                        " | Restricted: " + seat.isRestricted());
            }
            return null;
        });

        // Show the dialog and wait for user action
        dialog.showAndWait();
    }







    private void saveSeatChanges() {
        for (Seating seat : seatList) {
            restTemplate.put(BASE_URL + "/seating/" + seat.getSeatId(), seat);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Seat Management");
        alert.setHeaderText("Changes Saved");
        alert.setContentText("Seat availability has been updated.");
        alert.showAndWait();

        // Refresh seat data
        String selectedRoom = roomSelectionComboBox.getValue();
        if (selectedRoom != null) {
            fetchSeats(roomIdMap.get(selectedRoom));
        }
    }

}
