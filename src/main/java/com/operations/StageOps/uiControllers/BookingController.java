package com.operations.StageOps.uiControllers;

import com.operations.StageOps.model.*;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.*;

/**
 * Controller class for managing bookings in the JavaFX UI.
 */
public class BookingController {

    @FXML
    private TextField clientIdField, statusField, totalCostField;
    @FXML
    private DatePicker startDateField, endDateField;
    @FXML
    private ComboBox<Integer> startHourComboBox, startMinuteComboBox;
    @FXML
    private ComboBox<Integer> endHourComboBox, endMinuteComboBox;
    @FXML
    private TableView<BookingDTO> bookingsTable;
    @FXML
    private TableColumn<BookingDTO, Integer> bookingIdColumn, clientIdColumn, roomIdColumn;
    @FXML
    private TableColumn<BookingDTO, String> startDateColumn, endDateColumn, statusColumn;
    @FXML
    private TableColumn<BookingDTO, Double> totalCostColumn;
    @FXML
    private ComboBox<String> clientComboBox;

    private final Map<String, Integer> clientNameToIdMap = new HashMap<>();
    private final Map<String, Integer> roomIdMap = new HashMap<>();
    private List<String> roomNames = new ArrayList<>();  // List to store room names

    @FXML
    private Button createBookingButton, calculateCostButton, approveBookingButton;
    @FXML
    private VBox roomSelectionVBox;

    private RestTemplate restTemplate = new RestTemplate();
    private String apiUrl = "http://localhost:8080/booking"; // Replace with your actual API URL
    private final double roomRatePerNight = 100.0; // Assume a room rate of 100.0 per night
    private final String BASE_URL = "http://localhost:8080/api";

    /**
     * Initializes the BookingController.
     */
    @FXML
    public void initialize() {
        loadBookings();
        fetchRooms();

        fetchClients();

        bookingIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBookingId()).asObject());
        clientIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getClientId()).asObject());
        startDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime().toString()));
        endDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime().toString()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        totalCostColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalCost()).asObject());

        // Populate the ComboBoxes with valid hour (0-23) and minute (0-59) values
        for (int i = 0; i < 24; i++) {
            startHourComboBox.getItems().add(i);
            endHourComboBox.getItems().add(i);
        }

        for (int i = 0; i < 60; i++) {
            startMinuteComboBox.getItems().add(i);
            endMinuteComboBox.getItems().add(i);
        }


        // Add listeners for automatic cost calculation
        startDateField.valueProperty().addListener((observable, oldValue, newValue) -> calculateTotalCost());
        endDateField.valueProperty().addListener((observable, oldValue, newValue) -> calculateTotalCost());
        startHourComboBox.valueProperty().addListener((observable, oldValue, newValue) -> calculateTotalCost());
        startMinuteComboBox.valueProperty().addListener((observable, oldValue, newValue) -> calculateTotalCost());
        endHourComboBox.valueProperty().addListener((observable, oldValue, newValue) -> calculateTotalCost());
        endMinuteComboBox.valueProperty().addListener((observable, oldValue, newValue) -> calculateTotalCost());

    }

    // Method called when the start or end date is selected
    @FXML
    public void onDateRangeSelected() {
        // Get start and end date
        LocalDate startDate = startDateField.getValue();
        LocalDate endDate = endDateField.getValue();

        if (startDate != null && endDate != null) {
            roomSelectionVBox.getChildren().clear(); // Clear previous room selections

            // Add a ComboBox for each day in the range
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                ComboBox<String> roomComboBox = new ComboBox<>();
                roomComboBox.getItems().addAll(roomNames); // Add room names here dynamically
                roomComboBox.setPromptText("Select room for " + currentDate.toString());
                roomSelectionVBox.getChildren().add(roomComboBox);
                currentDate = currentDate.plusDays(1); // Move to the next day
            }
        }
    }

    /**
     * Loads bookings from the API and updates the UI.
     */
    public void loadBookings() {
        Task<List<BookingDTO>> task = new Task<>() {
            @Override
            protected List<BookingDTO> call() {
                try {
                    // Send GET request to fetch all bookings from the API (replace with actual API URL)
                    ResponseEntity<List<BookingDTO>> response = restTemplate.exchange(
                            apiUrl + "/all", HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<BookingDTO>>() {}
                    );
                    return response.getBody();
                } catch (Exception e) {
                    e.printStackTrace();
                    return Collections.emptyList();
                }
            }
        };

        // When the bookings are successfully fetched, update the TableView
        task.setOnSucceeded(event -> {
            List<BookingDTO> bookings = task.getValue();
            if (bookings != null) {
                bookingsTable.getItems().clear();
                bookingsTable.getItems().addAll(bookings);
            } else {
                showErrorAlert("Error", "Failed to load bookings. Please try again.");
            }
        });

        // If the task fails, show an error
        task.setOnFailed(event -> showErrorAlert("Error", "Failed to load bookings. Please try again."));

        // Run the task in a new thread to prevent blocking the UI thread
        new Thread(task).start();
    }


    /**
     * Fetches available rooms from the API.
     */
    private void fetchRooms() {
        Task<List<Room>> task = new Task<>() {
            @Override
            protected List<Room> call() {
                try {
                    // Fetch rooms from the server (replace with actual API URL and endpoint)
                    return Arrays.asList(restTemplate.getForObject(BASE_URL + "/rooms", Room[].class));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        // When rooms are successfully fetched, update the ComboBox
        task.setOnSucceeded(event -> {
            List<Room> rooms = task.getValue();
            if (rooms != null) {
                roomNames.clear();
                roomIdMap.clear();
                for (Room room : rooms) {
                    roomNames.add(room.getRoomName());
                    roomIdMap.put(room.getRoomName(), room.getRoomId());
                }
            } else {
                showErrorAlert("Error", "Failed to fetch rooms. Please try again.");
            }
        });

        // If the task fails, show an error
        task.setOnFailed(event -> showErrorAlert("Error", "Failed to fetch rooms. Please try again."));

        // Run the task in a new thread to prevent blocking the UI thread
        new Thread(task).start();
    }

    // Display error alert
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Creates a new booking.
     */
    public void createBooking() {
        try {
            String selectedClientName = clientComboBox.getValue();
            if (selectedClientName == null || !clientNameToIdMap.containsKey(selectedClientName)) {
                showErrorAlert("Error", "Please select a valid client.");
                return;
            }
            int clientId = clientNameToIdMap.get(selectedClientName);

            LocalDate startDate = startDateField.getValue();
            LocalDate endDate = endDateField.getValue();

            if (startDate == null || endDate == null) {
                showErrorAlert("Error", "Please select both start and end dates.");
                return;
            }



            // Create booking with room assignments
            String status = statusField.getText();
            double totalCost = Double.parseDouble(totalCostField.getText());
            ZonedDateTime startTime = ZonedDateTime.of(startDate, LocalTime.of(startHourComboBox.getValue(), startMinuteComboBox.getValue()), ZoneId.systemDefault());
            ZonedDateTime endTime = ZonedDateTime.of(endDate, LocalTime.of(endHourComboBox.getValue(), endMinuteComboBox.getValue()), ZoneId.systemDefault());


            // Collect room assignments based on selected rooms for each day
            List<BookingRoomAssignment> roomAssignments = new ArrayList<>();
            LocalDate currentDate = startDate;
            for (Node node : roomSelectionVBox.getChildren()) {
                if (node instanceof ComboBox) {
                    ComboBox<String> roomComboBox = (ComboBox<String>) node;
                    String selectedRoom = roomComboBox.getValue();
                    if (selectedRoom != null) {
                        Integer roomId = roomIdMap.get(selectedRoom);
                        if (roomId != null) {
                            ZonedDateTime dateTime = startTime;
                            roomAssignments.add(new BookingRoomAssignment(0, dateTime, roomId));
                        }
                    }
                }
                currentDate = currentDate.plusDays(1);  // Move to the next day
            }

            Booking booking = new Booking(0, clientId, startTime, endTime, status, totalCost, roomAssignments);
            // Send POST request to create the booking
            String response = restTemplate.postForObject(apiUrl + "/create", booking, String.class);
            System.out.println(response);

        } catch (Exception e) {
            System.out.println("Error creating booking: " + e.getMessage());
        }
    }

    /**
     * Calculates the total cost of the booking.
     */
    @FXML
    public void calculateTotalCost() {
        try {
            // Get selected start and end dates
            LocalDate startDate = startDateField.getValue();
            LocalDate endDate = endDateField.getValue();

            if (startDate != null && endDate != null) {
                double totalCost = 0;
                LocalDate currentDate = startDate;

                // Iterate over each ComboBox corresponding to each day in the range
                for (Node node : roomSelectionVBox.getChildren()) {
                    if (node instanceof ComboBox) {
                        ComboBox<String> roomComboBox = (ComboBox<String>) node;
                        String selectedRoomName = roomComboBox.getSelectionModel().getSelectedItem();
                        Integer roomId = roomIdMap.get(selectedRoomName);

                        if (roomId == null) {
                            showErrorAlert("Error", "Please select a room for " + currentDate.toString());
                            return;
                        }

                        // Get the room details
                        Room room = getRoomDetails(roomId);
                        if (room == null) {
                            showErrorAlert("Error", "Error fetching room details for " + currentDate.toString());
                            return;
                        }

                        // Get the start and end time for the room on this specific day
                        LocalTime startTime = LocalTime.of(startHourComboBox.getValue(), startMinuteComboBox.getValue());
                        LocalTime endTime = LocalTime.of(endHourComboBox.getValue(), endMinuteComboBox.getValue());

                        // Convert start and end times to ZonedDateTime
                        ZonedDateTime startZonedDateTime = ZonedDateTime.of(currentDate, startTime, ZoneId.systemDefault());
                        ZonedDateTime endZonedDateTime = ZonedDateTime.of(currentDate, endTime, ZoneId.systemDefault());

                        // Calculate the cost for the selected room on this day
                        double roomCost = calculateRoomCost(room, startZonedDateTime, endZonedDateTime);

                        // Add the room cost to the total cost
                        totalCost += roomCost;

                        // Move to the next day
                        currentDate = currentDate.plusDays(1);
                    }
                }

                // Add VAT (20%)
                totalCost += totalCost * 0.20;

                // Display the total cost in the totalCostField
                totalCostField.setText(String.format("%.2f", totalCost));

            } else {
                System.out.println("Please select both start and end dates.");
            }
        } catch (Exception e) {
            System.out.println("Error calculating total cost: " + e.getMessage());
        }
    }


    private double calculateRoomCost(Room room, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        double roomCost = 0.0;

        // Check if the end time is before the start time, which should not happen
        if (endDateTime.isBefore(startDateTime)) {
            showErrorAlert("Error", "End time cannot be before start time.");
            return 0.0; // Return 0 if times are incorrect
        }

        // Calculate the duration between start and end time
        Duration duration = Duration.between(startDateTime, endDateTime);
        long durationInHours = duration.toHours();
        long durationInDays = duration.toDays();

        // Adjust the logic to handle different durations properly
        if (durationInDays >= 7) {
            // Weekly rate if the duration is a full week or longer
            roomCost = room.getWeeklyRate();
        } else if (durationInDays >= 1) {
            // Daily rate for bookings lasting a full day or more
            roomCost = room.getDailyRate() * durationInDays;
        } else if (durationInHours >= 5 && durationInHours <= 7) {
            // Evening rate for bookings lasting between 5 to 7 hours
            roomCost = room.getEveningRate();
        } else if (durationInHours < 3) {
            // Hourly rate for bookings lasting less than 3 hours
            roomCost = room.getHourlyRate() * durationInHours;
        } else if (durationInHours >= 3 && durationInHours < 5) {
            // Possible adjustment for duration between 3 to 5 hours if needed
            roomCost = room.getHourlyRate() * durationInHours;
        } else {
            // In case the duration doesn't fit any of the above cases
            roomCost = room.getDailyRate(); // Default to daily rate if no other condition matches
        }

        return roomCost;
    }


    private void fetchClients() {
        Task<List<Client>> task = new Task<>() {
            @Override
            protected List<Client> call() {
                try {
                    return Arrays.asList(restTemplate.getForObject(BASE_URL + "/clients", Client[].class));
                } catch (Exception e) {
                    e.printStackTrace();
                    return Collections.emptyList();
                }
            }
        };

        task.setOnSucceeded(event -> {
            List<Client> clients = task.getValue();
            if (clients != null) {
                for (Client client : clients) {
                    String displayName = "Name: " + client.getName() + " Cid:" + "(" + client.getClientId() + ")";
                    clientComboBox.getItems().add(displayName);
                    clientNameToIdMap.put(displayName, client.getClientId());
                }
            } else {
                showErrorAlert("Error", "Failed to load clients.");
            }
        });

        new Thread(task).start();
    }




    // Helper method to get room details by ID
    private Room getRoomDetails(int roomId) {
        try {
            String url = BASE_URL + "/rooms/" + roomId; // Fetch room by ID
            return restTemplate.getForObject(url, Room.class); // Make the GET request to fetch the room
        } catch (Exception e) {
            showErrorAlert("Error", "Error fetching room details.");
            return null;
        }
    }

}
