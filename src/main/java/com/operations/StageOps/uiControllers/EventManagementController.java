package com.operations.StageOps.uiControllers;

import com.operations.StageOps.model.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class EventManagementController {

    @FXML
    private TextField eventNameField, roomIdField, ticketsAvailableField, ticketsSoldField,
            eventTypeField, totalRevenueField, startTimeField, endTimeField, layoutIdField, ticketPriceField, maxDiscountField, clientIdField;
    @FXML
    private DatePicker eventDateField;
    @FXML
    private TableView<EventDto> eventsTable;
    @FXML
    private TableColumn<EventDto, Integer> eventIdColumn, roomIdColumn, ticketsAvailableColumn, ticketsSoldColumn, layoutIdColumn, clientIdColumn;
    @FXML
    private TableColumn<EventDto, String> eventNameColumn, eventDateColumn, eventTypeColumn, startTimeColumn, endTimeColumn;
    @FXML
    private TableColumn<EventDto, Double> totalRevenueColumn, ticketPriceColumn, maxDiscountColumn;
    @FXML
    private Button scheduleEventButton;

    @FXML
    private ComboBox<String> roomIdComboBox, layoutIdComboBox;
    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private ComboBox<String> startTimeComboBox, endTimeComboBox;
    @FXML
    private ComboBox<String> clientComboBox;

    private final Map<String, Integer> clientNameToIdMap = new HashMap<>();
    private RestTemplate restTemplate = new RestTemplate();
    private String apiUrl = "http://localhost:8080/api/events"; // Replace with actual API URL
    private String BASE_URL = "http://localhost:8080/api/"; // Replace with actual API URL
    private Map<String, Integer> roomIdMap = new HashMap<>();  // To store room names and their corresponding IDs
    private Map<String,Integer> layoutIdMap = new HashMap<>(); // To store layout IDs and their corresponding names

    @FXML
    public void initialize() {
        loadEvents(); // Load events initially
        fetchRooms(); // Fetch rooms from the API
        fetchClients(); // Fetch clients from the API

        // Set up the columns for the table
        clientIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getClientId()).asObject());
        eventIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getEventId()).asObject());
        eventNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventName()));
        eventDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventDate().toString()));
        eventTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventType()));
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime().toString()));
        endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime().toString()));
        roomIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRoomId()).asObject());
        ticketsAvailableColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTicketsAvailable()).asObject());
        ticketsSoldColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTicketsSold()).asObject());
        layoutIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLayoutId()).asObject());
        ticketPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTicketPrice()).asObject());
        maxDiscountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getMaxDiscount()).asObject());
        totalRevenueColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalRevenue()).asObject());
    }
    // Populate Room ID ComboBox with data from the API

    // Fetch rooms method (you already have it)
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

        task.setOnSucceeded(event -> {
            List<Room> rooms = task.getValue();
            if (rooms != null) {
                roomIdComboBox.getItems().clear();
                for (Room room : rooms) {
                    roomIdComboBox.getItems().add(room.getRoomName());
                    roomIdMap.put(room.getRoomName(), room.getRoomId());
                }
            } else {
                showErrorAlert("Error", "Failed to fetch rooms. Please try again.");
            }
        });

        task.setOnFailed(event -> showErrorAlert("Error", "Failed to fetch rooms. Please try again."));

        new Thread(task).start();
    }

    // Fetch layouts based on the selected room
    private void fetchLayoutsForRoom(Integer roomId) {
        Task<List<LayoutConfiguration>> task = new Task<>() {
            @Override
            protected List<LayoutConfiguration> call() {
                try {
                    // Fetch layouts for the specific room using the room ID
                    String url = BASE_URL + "/layouts/rooms/" + roomId;
                    // Assuming the response contains a list of Layout objects, where each has a name and ID.
                    LayoutConfiguration[] layoutsArray = restTemplate.getForObject(url, LayoutConfiguration[].class);
                    return Arrays.asList(layoutsArray);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        task.setOnSucceeded(event -> {
            List<LayoutConfiguration> layouts = task.getValue();
            if (layouts != null) {
                layoutIdComboBox.getItems().clear();
                for (LayoutConfiguration layout : layouts) {
                    // Add only the layout names to the ComboBox
                    layoutIdComboBox.getItems().add(layout.getLayoutName());
                    // Optionally store the layout object with its ID in a map for easy access
                    layoutIdMap.put(layout.getLayoutName(), layout.getLayoutId());
                }
            } else {
                showErrorAlert("Error", "Failed to fetch layouts. Please try again.");
            }
        });

        task.setOnFailed(event -> showErrorAlert("Error", "Failed to fetch layouts. Please try again."));

        // Run the task in a new thread to prevent blocking the UI thread
        new Thread(task).start();
    }
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Handle room selection change event
    @FXML
    private void handleRoomSelection() {
        String selectedRoomName = roomIdComboBox.getSelectionModel().getSelectedItem();
        if (selectedRoomName != null) {
            // Get the room ID based on the selected room name
            Integer selectedRoomId = roomIdMap.get(selectedRoomName);
            System.out.println("Selected Room ID: " + selectedRoomId);

            // Optionally, you can set the roomIdField to display the room ID
            if (roomIdField != null) {
                roomIdField.setText(String.valueOf(selectedRoomId)); // This will show the selected room ID in the roomIdField
            }

            // Fetch layouts for the selected room
            fetchLayoutsForRoom(selectedRoomId);
        }
    }

    @FXML
    private void handleLayoutSelection() {
        // Get the selected layout name from the ComboBox
        String selectedLayoutName = layoutIdComboBox.getSelectionModel().getSelectedItem();

        if (selectedLayoutName != null) {
            // Fetch the layout ID from the layoutIdMap using the selected layout name
            Integer selectedLayoutId = layoutIdMap.get(selectedLayoutName);

            if (selectedLayoutId != null) {
                System.out.println("Selected Layout ID: " + selectedLayoutId);

                // Optionally, display the layout ID in a TextField (or other UI component)
                if (layoutIdField != null) {
                    layoutIdField.setText(String.valueOf(selectedLayoutId));  // Display the selected layout's ID in the TextField
                }
            } else {
                showErrorAlert("Error", "Layout ID not found. Please try again.");
            }
        } else {
            showErrorAlert("Error", "No layout selected.");
        }
    }


    public void scheduleEvent() {
        try {
            String eventName = eventNameField.getText();
            LocalDate eventDateLocal = eventDateField.getValue();
            Date eventDate = Date.from(eventDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String roomName = roomIdComboBox.getSelectionModel().getSelectedItem();
            Integer roomId = roomIdMap.get(roomName);
            int ticketsAvailable = Integer.parseInt(ticketsAvailableField.getText());
            int ticketsSold = Integer.parseInt(ticketsSoldField.getText());
            String eventType = eventTypeField.getText();
            double totalRevenue = Double.parseDouble(totalRevenueField.getText());

            // Get start and end date/time
            LocalDate startDate = startDatePicker.getValue();
            String startTime = startTimeComboBox.getValue();
            ZonedDateTime startDateTime = ZonedDateTime.of(startDate.atTime(LocalTime.parse(startTime)), ZoneId.systemDefault());

            LocalDate endDate = endDatePicker.getValue();
            String endTime = endTimeComboBox.getValue();

            // Make sure the endTime is not null and is in the correct format
            if (endTime == null || endTime.isEmpty()) {
                throw new IllegalArgumentException("End time cannot be empty");
            }

            ZonedDateTime endDateTime = ZonedDateTime.of(endDate.atTime(LocalTime.parse(endTime)), ZoneId.systemDefault());

            String selectedLayoutName = layoutIdComboBox.getSelectionModel().getSelectedItem();
            Integer layoutId = layoutIdMap.get(selectedLayoutName);
            double ticketPrice = Double.parseDouble(ticketPriceField.getText());
            double maxDiscount = Double.parseDouble(maxDiscountField.getText());
            String selectedClientName = clientComboBox.getValue();
            if (selectedClientName == null || !clientNameToIdMap.containsKey(selectedClientName)) {
                showErrorAlert("Error", "Please select a valid client.");
                return;
            }
            int clientId = clientNameToIdMap.get(selectedClientName);


            Event event = new Event(0, eventName, eventDate, startDateTime, endDateTime, roomId, ticketsAvailable, ticketsSold, eventType, totalRevenue, layoutId, ticketPrice, maxDiscount, clientId);
            System.out.println("Scheduling event: " + event.getLayoutId());
            System.out.println("Scheduling event: " + event.getRoomId());
            String response = restTemplate.postForObject(apiUrl + "/schedule", event, String.class);
            System.out.println(response);
            loadEvents();
        } catch (Exception e) {
            System.out.println("Error scheduling event: " + e.getMessage());
        }
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



    public void loadEvents() {
        try {
            ResponseEntity<EventDto[]> responseEntity = restTemplate.getForEntity(apiUrl + "/all", EventDto[].class);
            EventDto[] eventsArray = responseEntity.getBody();

            if (eventsArray != null) {
                eventsTable.setItems(FXCollections.observableArrayList(eventsArray)); // Correctly populating the table
            } else {
                System.out.println("No events found.");
            }
        } catch (Exception e) {
            System.out.println("Error loading events: " + e.getMessage());
        }
    }
}
