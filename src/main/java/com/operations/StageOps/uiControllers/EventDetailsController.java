package com.operations.StageOps.uiControllers;

import com.operations.StageOps.model.EventDto;
import com.operations.StageOps.model.Seating;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDetailsController {

    @FXML private Label nameLabel, dateLabel, startLabel, endLabel, roomLabel, layoutLabel,
            ticketsAvailableLabel, ticketsSoldLabel, eventTypeLabel, revenueLabel, roomLayoutInfo;
    @FXML private GridPane seatGrid;
    @FXML private Button closeButton;
    @FXML private ScrollPane seatScrollPane;
    @FXML private VBox seatKey;

    private Map<String, Seating> selectedSeats = new HashMap<>();

    public void setEventDetails(EventDto event, List<Seating> availableSeats) {
        nameLabel.setText("Event: " + event.getEventName());
        dateLabel.setText("Date: " + event.getEventDate());
        startLabel.setText("Start Time: " + event.getStartTime());
        endLabel.setText("End Time: " + event.getEndTime());
        roomLabel.setText("Room ID: " + event.getRoomId());
        layoutLabel.setText("Layout ID: " + event.getLayoutId());
        ticketsAvailableLabel.setText("Tickets Available: " + event.getTicketsAvailable());
        ticketsSoldLabel.setText("Tickets Sold: " + event.getTicketsSold());
        eventTypeLabel.setText("Event Type: " + event.getEventType());
        revenueLabel.setText("Total Revenue: $" + event.getTotalRevenue());
        roomLayoutInfo.setText("Room and Layout Details: " + event.getRoomId() + " - " + event.getLayoutId());

        // Group seats by section
        Map<String, List<Seating>> seatsBySection = new HashMap<>();
        for (Seating seat : availableSeats) {
            seatsBySection.computeIfAbsent(seat.getSectionName(), k -> new ArrayList<>()).add(seat);
        }

        // Add seat buttons for each available seat, grouped by section
        int row = 0;
        for (String section : seatsBySection.keySet()) {
            Label sectionLabel = new Label("Section: " + section);
            sectionLabel.setFont(new Font("Arial", 16));
            sectionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
            seatGrid.add(sectionLabel, 0, row++);
            int col = 0;
            for (Seating seat : seatsBySection.get(section)) {
                Button seatButton = new Button("Seat " + seat.getSectionName() + "-" + seat.getSeatNumber());
                String seatId = seat.getSeatId();

                if (seat.getStatus().equals("reserved")) {
                    seatButton.setStyle("-fx-padding: 10; -fx-background-color: #D32F2F; -fx-text-fill: white; -fx-font-size: 12px; -fx-border-radius: 5;");
                    seatButton.setDisable(true);
                } else {
                    seatButton.setStyle("-fx-padding: 10; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-border-radius: 5;");

                    seatButton.setOnMouseClicked((MouseEvent e) -> showSeatDetailsDialog(seat));
                }

                seatGrid.add(seatButton, col++, row);
            }
            row++; // Move to the next row only after finishing a section
        }

        // Close button action
        closeButton.setOnAction(e -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }

    private void showSeatDetailsDialog(Seating seat) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Seat Information");

        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 10;");

        Label seatLabel = new Label("Seat: " + seat.getSectionName() + "-" + seat.getSeatNumber());
        Label accessibleLabel = new Label("Accessible:");
        CheckBox accessibleCheckBox = new CheckBox();
        accessibleCheckBox.setSelected(seat.isAccessible());

        Label restrictedLabel = new Label("Restricted View:");
        CheckBox restrictedCheckBox = new CheckBox();
        restrictedCheckBox.setSelected(seat.isRestricted());

        content.getChildren().addAll(seatLabel, accessibleLabel, accessibleCheckBox, restrictedLabel, restrictedCheckBox);
        dialog.getDialogPane().setContent(content);

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                seat.setAccessible(accessibleCheckBox.isSelected());
                seat.setRestricted(restrictedCheckBox.isSelected());
                System.out.println("Seat Updated: " + seat.getSeatId() + " | Accessible: " + seat.isAccessible() + " | Restricted: " + seat.isRestricted());
            }
            return null;
        });

        dialog.showAndWait();
    }
}
