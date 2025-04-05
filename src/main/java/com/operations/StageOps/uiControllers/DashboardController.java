package com.operations.StageOps.uiControllers;

import com.operations.StageOps.model.EventDto;
import com.operations.StageOps.model.RevenueTracking;
import com.operations.StageOps.model.ReviewDto;
import com.operations.StageOps.model.Room;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardController {

    @FXML
    private TableView<EventDto> eventsTable; // This will reference the TableView in the FXML

    @FXML
    private TableColumn<EventDto, Integer> eventIdColumn;
    @FXML
    private TableColumn<EventDto, String> eventNameColumn;
    @FXML
    private TableColumn<EventDto, String> eventDateColumn;
    @FXML
    private TableColumn<EventDto, Integer> ticketsAvailableColumn;
    @FXML
    private TableColumn<EventDto, Integer> ticketsSoldColumn;
    @FXML
    private TableColumn<EventDto, Double> totalRevenueColumn;

    @FXML
    private LineChart<String, Number> revenueChart;  // The LineChart
    @FXML
    private VBox reviewsContainer; // Reference to VBox for displaying reviews

    @FXML
    private VBox availabilityContainer; // Reference to VBox for displaying reviews
    @FXML
    private Label totalEventsValueLabel; // Label for Total Events
    @FXML
    private Label totalSalesValueLabel; // Label for Total Sales
    @FXML
    private Label totalTicketsSoldValueLabel; // Label for Total Tickets Sold


    @FXML
    private CategoryAxis xAxis; // X-Axis (time)

    @FXML
    private NumberAxis yAxis; // Y-Axis (revenue)
    @FXML

    private static final String REVIEWS_API_URL = "http://localhost:8080/api/reviews"; // API endpoint for reviews
    private static final String EVENTS_API_URL = "http://localhost:8080/api/events/upcoming";
    private static final String EVENTS_API_URL2 = "http://localhost:8080/api/events/";// API URL for events
    private static final String REVENUE_API_URL = "http://localhost:8080/api/revenue/entries/monthly/2025/03"; // Example API URL for monthly revenue data
    private static final String TICKETS_API_URL = "http://localhost:8080/api/tickets"; // You may need to create this API for total tickets sold


    public void initialize() {
        // Fetch events from API when the controller is initialized
        loadEvents();
        loadTotalEventsForWeek();
        loadTotalSalesForWeek();
        loadTotalTicketsSoldForWeek();

        // Fetch revenue data and plot on chart
        loadRevenueData();
        loadReviews();
        loadRoomAvailability();

        // Initialize the table columns
        eventIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getEventId()).asObject());
        eventNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventName()));
        eventDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventDate().toString())); // Format this if needed
        ticketsAvailableColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTicketsAvailable()).asObject());
        ticketsSoldColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTicketsSold()).asObject());
        totalRevenueColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalRevenue()).asObject());
    }

    private void loadTotalEventsForWeek() {
        new Thread(() -> {
            try {
                RestTemplate restTemplate = new RestTemplate();
                // Fetch total events for the current week (replace with the appropriate endpoint if needed)
                ResponseEntity<Integer> response = restTemplate.exchange(
                        "http://localhost:8080/api/events/this-week/count", // Endpoint to fetch events for the current week
                        HttpMethod.GET,
                        null,
                        Integer.class
                );
                Integer totalEvents = response.getBody();

                // Update the UI with the total events
                Platform.runLater(() -> totalEventsValueLabel.setText(totalEvents.toString()));

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> totalEventsValueLabel.setText("Failed to load events"));
            }
        }).start();
    }

    // Method to load total sales for the current week (total revenue)
    private void loadTotalSalesForWeek() {
        new Thread(() -> {
            try {
                RestTemplate restTemplate = new RestTemplate();
                // Fetch total sales (revenue) for the current week
                ResponseEntity<Double> response = restTemplate.exchange(
                        "http://localhost:8080/api/revenue/this-week/total", // You may need to adjust this endpoint if necessary
                        HttpMethod.GET,
                        null,
                        Double.class
                );
                Double totalSales = response.getBody();

                // Update the UI with the total sales
                Platform.runLater(() -> totalSalesValueLabel.setText("$" + totalSales));

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> totalSalesValueLabel.setText("Failed to load total sales"));
            }
        }).start();
    }

    // Method to load total tickets sold for the current week
    private void loadTotalTicketsSoldForWeek() {
        new Thread(() -> {
            try {
                RestTemplate restTemplate = new RestTemplate();
                // Fetch total tickets sold for the current week
                ResponseEntity<Integer> response = restTemplate.exchange(
                        "http://localhost:8080/api/tickets/this-week/sold", // Replace with the correct endpoint if needed
                        HttpMethod.GET,
                        null,
                        Integer.class
                );
                Integer totalTicketsSold = response.getBody();

                // Update the UI with the total tickets sold
                Platform.runLater(() -> totalTicketsSoldValueLabel.setText(totalTicketsSold.toString()));

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> totalTicketsSoldValueLabel.setText("Failed to load total tickets sold"));
            }
        }).start();
    }


    private void loadRoomAvailability() {
        new Thread(() -> {
            try {
                RestTemplate restTemplate = new RestTemplate();

                // Fetch all rooms from the API
                ResponseEntity<List<Room>> response = restTemplate.exchange(
                        "http://localhost:8080/api/rooms", // Endpoint to get all rooms
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Room>>() {} // Type reference for Room list
                );

                List<Room> rooms = response.getBody(); // Get the list of rooms from the response

                if (rooms != null) {
                    // Clear the existing labels (if any)
                    Platform.runLater(() -> availabilityContainer.getChildren().clear());

                    // Iterate over all rooms and check availability
                    for (Room room : rooms) {
                        String roomId = String.valueOf(room.getRoomId());

                        // Assuming you have these values, they should be passed along as query parameters
                        LocalDate startDate = LocalDate.now(); // Use appropriate date for the availability check
                        LocalDate endDate = startDate.plusDays(0); // Example of an end date, adjust as needed

                        ZoneId zoneId = ZoneId.of("UTC"); // UTC time zone
                        ZonedDateTime eventStartTime = ZonedDateTime.now(zoneId); // Current time in UTC
                        ZonedDateTime eventEndTime = eventStartTime.plusHours(1); // Add 1 hour to the start time

                        // Format the ZonedDateTime to the ISO offset format (which uses the UTC offset, not the timezone name)
                        String formattedStartTime = eventStartTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        String formattedEndTime = eventEndTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

                        // Prepare the URI with query parameters (startDate, endDate, eventStartTime, eventEndTime)
                        String url = "http://localhost:8080/api/rooms/{roomId}/availability?" +
                                "startDate=" + startDate +
                                "&endDate=" + endDate +
                                "&eventStartTime=" + formattedStartTime +
                                "&eventEndTime=" + formattedEndTime;


                        try {
                            // Make the API call to check if the room is available
                            ResponseEntity<Boolean> availabilityResponse = restTemplate.exchange(
                                    url, // URL with query parameters
                                    HttpMethod.GET,
                                    null,
                                    Boolean.class,
                                    roomId
                            );

                            boolean isAvailable = availabilityResponse.getBody(); // Get the availability status

                            // Create a VBox for each room (the "card")
                            VBox roomCard = new VBox(10); // 10px spacing between elements
                            roomCard.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10px; -fx-border-radius: 10px; -fx-border-color: #DDDDDD; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
                            roomCard.setMinWidth(200); // Set a minimum width for the card
                            roomCard.setMaxWidth(450); // Set a maximum width for the card
                            roomCard.setPrefWidth(450); // Set a preferred width

                            // Title section with the room name
                            Label roomName = new Label(room.getRoomName());
                            roomName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                            roomName.setWrapText(true); // Allow the text to wrap within the label

                            // Availability status label
                            Label availabilityLabel = new Label(isAvailable ? "Available" : "Not Available");
                            availabilityLabel.setStyle("-fx-text-fill: " + (isAvailable ? "green" : "red") + "; -fx-font-size: 14px;");
                            availabilityLabel.setWrapText(true); // Allow the text to wrap within the label

                            // Adding everything to the card VBox
                            roomCard.getChildren().addAll(roomName, availabilityLabel);

                            // Add the room card to the main container
                            Platform.runLater(() -> availabilityContainer.getChildren().add(roomCard));

                        } catch (HttpClientErrorException | HttpServerErrorException e) {
                            e.printStackTrace(); // Log the error
                            Platform.runLater(() -> {
                                Label errorLabel = new Label("Error with room " + roomId + ": " + e.getMessage());
                                errorLabel.setStyle("-fx-text-fill: red;");
                                availabilityContainer.getChildren().add(errorLabel);
                            });
                        }
                    }
                } else {
                    Platform.runLater(() -> {
                        Label errorLabel = new Label("No rooms found.");
                        errorLabel.setStyle("-fx-text-fill: red;");
                        availabilityContainer.getChildren().add(errorLabel);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    Label errorLabel = new Label("Failed to load room availability.");
                    errorLabel.setStyle("-fx-text-fill: red;");
                    availabilityContainer.getChildren().add(errorLabel);
                });
            }
        }).start();
    }




    private void loadReviews() {
        new Thread(() -> {
            try {
                RestTemplate restTemplate = new RestTemplate();

                // Fetch reviews from API
                ResponseEntity<List<ReviewDto>> response = restTemplate.exchange(
                        REVIEWS_API_URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<ReviewDto>>() {}
                );

                List<ReviewDto> reviews = response.getBody();

                if (reviews != null && !reviews.isEmpty()) {
                    // Fetch event names for each review
                    for (ReviewDto review : reviews) {
                        try {
                            String eventUrl = EVENTS_API_URL2 + review.getEventId();
                            ResponseEntity<String> eventResponse = restTemplate.getForEntity(eventUrl, String.class);

                            // Parse JSON response
                            ObjectMapper objectMapper = new ObjectMapper();
                            JsonNode rootNode = objectMapper.readTree(eventResponse.getBody());

                            // Extract event name
                            String eventName = rootNode.path("eventName").asText();
                            review.setEventName(eventName); // Store extracted name
                        } catch (Exception e) {
                            review.setEventName("Unknown Event"); // Default if fetching fails
                        }
                    }
                }

                // Update UI in JavaFX Thread
                Platform.runLater(() -> {
                    reviewsContainer.getChildren().clear();

                    if (reviews != null && !reviews.isEmpty()) {
                        for (ReviewDto review : reviews) {
                            reviewsContainer.getChildren().add(createReviewCard(review));
                        }
                    } else {
                        Label noReviewsLabel = new Label("No reviews available.");
                        reviewsContainer.getChildren().add(noReviewsLabel);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    reviewsContainer.getChildren().clear();
                    Label errorLabel = new Label("Failed to load reviews.");
                    reviewsContainer.getChildren().add(errorLabel);
                });
            }
        }).start();
    }

    private VBox createReviewCard(ReviewDto review) {
        VBox card = new VBox();
        card.setSpacing(8);  // Increased spacing for better visual appeal
        card.setPadding(new Insets(8));  // Consistent padding for the cards
        card.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 8px; "
                + "-fx-border-color: #dddddd; -fx-padding: 8px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 2);"
                + "-fx-pref-width: 450px; -fx-min-width: 400px;");  // Set wider card width

        // Event Name
        Label eventLabel = new Label(review.getEventName());
        eventLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Rating Stars
        HBox starBox = new HBox();
        starBox.setSpacing(2);  // Slightly increased spacing for stars
        for (int i = 0; i < review.getRating(); i++) {
            Label star = new Label("★");
            star.setStyle("-fx-text-fill: gold; -fx-font-size: 16px;");
            starBox.getChildren().add(star);
        }

        // Review Text
        Label reviewText = new Label(review.getReviewText());
        reviewText.setStyle("-fx-font-size: 13px; -fx-text-fill: #555555;");
        reviewText.setWrapText(true);  // Wrap text for readability

        // Date
        Label dateLabel = new Label("🗓 " + review.getReviewDate());
        dateLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #777777;");

        // Layout
        card.getChildren().addAll(eventLabel, starBox, reviewText, dateLabel);
        return card;
    }







    // Method to load events dynamically from the API
    private void loadEvents() {
        // Create a new thread to avoid blocking the JavaFX UI thread
        new Thread(() -> {
            try {
                // Create a RestTemplate instance
                RestTemplate restTemplate = new RestTemplate();

                // Use ParameterizedTypeReference to specify the return type of the response
                ResponseEntity<List<EventDto>> response = restTemplate.exchange(
                        EVENTS_API_URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<EventDto>>() {}
                );

                // Get the list of events from the response
                List<EventDto> events = response.getBody();

                // Update UI with the events on the JavaFX Application Thread
                Platform.runLater(() -> {
                    if (events != null) {
                        // Set the data to the TableView
                        eventsTable.getItems().setAll(events);
                    } else {
                        eventsTable.getItems().clear();
                    }
                });

            } catch (Exception e) {
                // Handle exception (e.g., API call failure)
                e.printStackTrace();  // Log the error
                Platform.runLater(() -> eventsTable.getItems().clear());
            }
        }).start();
    }

    // Method to load revenue data and display it on the chart
    private void loadRevenueData() {
        // Create a new thread to avoid blocking the JavaFX UI thread
        new Thread(() -> {
            try {
                // Create a RestTemplate instance
                RestTemplate restTemplate = new RestTemplate();

                // Use ParameterizedTypeReference to specify the return type of the response
                ResponseEntity<List<RevenueTracking>> response = restTemplate.exchange(
                        REVENUE_API_URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<RevenueTracking>>() {}
                );

                // Get the list of revenue entries from the response
                List<RevenueTracking> revenueEntries = response.getBody();

                // Update UI with the revenue data on the JavaFX Application Thread
                Platform.runLater(() -> {
                    if (revenueEntries != null) {
                        // Plot the revenue data on the chart
                        plotRevenueData(revenueEntries);
                    }
                });

            } catch (Exception e) {
                // Handle exception (e.g., API call failure)
                e.printStackTrace();  // Log the error
            }
        }).start();
    }


    private void plotRevenueData(List<RevenueTracking> revenueEntries) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Revenue");

        // Define a date formatter to extract the date in the format "YYYY-MM-DD" (or any other format you prefer)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Create a RestTemplate instance to make the API calls
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/revenue/total/daily/";

        // Loop through the data and add it to the series
        for (RevenueTracking entry : revenueEntries) {
            // Assuming bookingDate is a ZonedDateTime object
            String date = entry.getBookingDate().format(formatter); // Format the booking date to string

            // Call the API to get the total revenue for this specific date
            try {
                // Make the API call to fetch total revenue for the day
                ResponseEntity<Double> response = restTemplate.getForEntity(apiUrl + date, Double.class);
                double revenue = response.getBody(); // Revenue for that date

                // Add the data point to the series
                series.getData().add(new XYChart.Data<>(date, revenue));
            } catch (Exception e) {
                e.printStackTrace(); // Log the error if the API call fails
                // You can decide how to handle the error (e.g., set revenue to 0 or skip the entry)
            }
        }

        // Clear any existing data and add the new series
        revenueChart.getData().clear();
        revenueChart.getData().add(series);
    }



}
