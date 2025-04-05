package com.operations.StageOps;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.operations.StageOps.model.*;
import com.operations.StageOps.uiControllers.EventDetailsController;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Component
public class SimpleUiController {

    // FXML UI elements
    @FXML
    private StackPane contentArea;
    @FXML
    private Button tab1Button;
    @FXML
    private Button tab2Button;
    @FXML
    private Button tab3Button;
    @FXML
    private Button tab4Button;
    @FXML
    private Button tab5Button;
    @FXML
    private Button tab6Button;
    @FXML
    private Button tab7Button;

    private List<Button> allTabs;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String EVENTS_API_URL = "http://localhost:8080/api/events/all";  // Adjust if needed
    private final String BOOKINGS_API_URL = "http://localhost:8080/booking/all";

//    private static final Logger logger = LoggerFactory.getLogger(SimpleUiController.class);


    // Initial setup for the controller
    @FXML
    public void initialize() {
        if (contentArea != null) {
            allTabs = Arrays.asList(tab1Button, tab2Button, tab3Button, tab4Button, tab5Button, tab6Button, tab7Button);
            showDashboard();  // Set default tab
        } else {
            System.out.println("contentArea is null");
        }
    }

    // ===================== API Interactions ====================

    // Fetch events from the API
    private List<EventDto> fetchEventsFromApi() {
        try {
            EventDto[] events = restTemplate.getForObject(EVENTS_API_URL, EventDto[].class);
            return events != null ? List.of(events) : List.of();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    private List<Booking> fetchBookingsFromApi() {
        try {
            Booking[] bookings = restTemplate.getForObject(BOOKINGS_API_URL, Booking[].class);
            return bookings != null ? List.of(bookings) : List.of();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // Fetch available seats for a specific event
    private List<Seating> fetchAvailableSeats(int eventId) {
        String url = "http://localhost:8080/api/events/" + eventId + "/seats";
        Seating[] seats = restTemplate.getForObject(url, Seating[].class);
        return seats != null ? List.of(seats) : List.of();
    }

    // Fetch event details by name
    private EventDto fetchEventByName(String entryTitle) {
        String eventName = entryTitle.split(" - layout: ")[0]; // Extract event name before " - layout:"
        List<EventDto> events = fetchEventsFromApi();
        return events.stream()
                .filter(event -> event.getEventName().equalsIgnoreCase(eventName))
                .findFirst()
                .orElse(null);
    }


    // ===================== UI & View Management ====================

    // Show calendar view with events loaded from the API
    @FXML
    public void showCalendar() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(createCalendarView());
        updateTabStyles(tab2Button);
    }

    // Create and configure the calendar view
    private CalendarView createCalendarView() {
        CalendarView calendarView = new CalendarView();
        calendarView.setContextMenuCallback(param -> null);  // Disable right-click context menu
        calendarView.setEntryFactory(param -> null);  // Disable event editing
        calendarView.setEntryDetailsCallback(param -> null);  // Disable popover

        // Load events from API
        loadEventsFromApi(calendarView);

        // Listen for event selection
        calendarView.getSelections().addListener((SetChangeListener<Object>) change -> {
            if (change.wasAdded() && change.getElementAdded() instanceof Entry<?>) {
                Entry<?> entry = (Entry<?>) change.getElementAdded();
                showEventDetailsInModal(entry);
            }
        });

        return calendarView;
    }

    public void loadEventsFromApi(CalendarView calendarView) {
        Calendar eventCalendar = new Calendar("Events");
        eventCalendar.setReadOnly(true);
        Calendar bookingCalendar = new Calendar("Bookings");
        bookingCalendar.setStyle(Calendar.Style.STYLE2);
        bookingCalendar.setReadOnly(true);

        // Fetch and add events
        List<EventDto> events = fetchEventsFromApi();
        for (EventDto event : events) {
            Entry<String> entry = new Entry<>(event.getEventName() + " - layout: " + event.getLayoutId() + " - room: " + event.getRoomId());

            LocalDateTime start = Instant.parse(event.getStartTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime end = Instant.parse(event.getEndTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();

            entry.setInterval(start, end);
            eventCalendar.addEntry(entry);
        }

        // Fetch and add bookings
        List<Booking> bookings = fetchBookingsFromApi();
        for (Booking booking : bookings) {
            LocalDateTime bookingStart = booking.getStartTime().toLocalDateTime();
            LocalDateTime bookingEnd = booking.getEndTime().toLocalDateTime();

            LocalDateTime currentDay = bookingStart;
            while (!currentDay.isAfter(bookingEnd)) {
                // Find the room assigned for this specific day (handling timestamp comparison)
                Integer roomIdForDay = getRoomIdForDate(booking.getRoomAssignments(), currentDay.toLocalDate());

                // Log the room ID passed for the specific day
//                logger.info("Processing room ID for day: " + currentDay.toLocalDate() + " - Room ID: " + roomIdForDay);

                String roomNameAndLayout = (roomIdForDay != null) ? getRoomNameAndLayoutById(roomIdForDay) : "Room Not Found - Layout Not Found";
                // Title includes room name
                String dayTitle = "Booking: " + booking.getClientId() + " -Room: " + roomNameAndLayout;

                // Set correct start & end times
                LocalDateTime startTime = (currentDay.equals(bookingStart)) ? bookingStart : currentDay.toLocalDate().atStartOfDay();
                LocalDateTime endTime = (currentDay.plusDays(1).isAfter(bookingEnd)) ? bookingEnd : currentDay.toLocalDate().atTime(23, 59, 59);

                Entry<String> entry = new Entry<>(dayTitle);
                entry.setInterval(startTime, endTime);

                bookingCalendar.addEntry(entry);
                currentDay = currentDay.plusDays(1);
            }
        }

        // Add calendars to CalendarSource
        CalendarSource calendarSource = new CalendarSource("Schedules");
        calendarSource.getCalendars().addAll(eventCalendar, bookingCalendar);
        calendarView.getCalendarSources().add(calendarSource);
    }

    private String getRoomNameAndLayoutById(int roomId) {
        // Fetch room details by ID
//        logger.info("Fetching room details for Room ID: " + roomId);
        Room room = fetchRoomDetailsById(roomId);

        // Fetch layout details based on currentLayoutId
        String roomName = (room != null && room.getRoomName() != null) ? room.getRoomName() : "Room Not Found";
        String layoutName = "Layout Not Found"; // Default layout name

        // Fetch layout details if room and currentLayoutId are available
        layoutName = fetchLayoutDetailsById(room.getCurrentLayoutId());

        // Log room name and layout
//        logger.info("Room ID: " + roomId + " - Room Name: " + roomName + " - Layout: " + layoutName);
        return roomName + " - Layout: " + layoutName;
    }

    private String fetchLayoutDetailsById(int layoutId) {
        // Fetch layout details from an external service or database
        String url = "http://localhost:8080/api/layouts/" + layoutId;
        LayoutConfiguration layout = restTemplate.getForObject(url, LayoutConfiguration.class);

        return (layout != null && layout.getLayoutName() != null) ? layout.getLayoutName() : "Unknown Layout";
    }
    /**
     * Gets the room ID assigned for a specific date, handling timestamp conversion.
     */
    private Integer getRoomIdForDate(List<BookingRoomAssignment> roomAssignments, LocalDate date) {
        for (BookingRoomAssignment assignment : roomAssignments) {
            // Convert timestamp to LocalDate before comparing
            LocalDate assignmentDate = assignment.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (assignmentDate.equals(date)) {
                // Log the room ID for the assignment date
//                logger.info("Found room assignment for date: " + assignmentDate + " - Room ID: " + assignment.getRoomId());
                return assignment.getRoomId();
            }
        }
        // Log if no room is found for the given date
//        logger.warn("No room found for date: " + date);
        return null; // No room found for the date
    }

    // Fetch room details by ID
    private Room fetchRoomDetailsById(int roomId) {
        String url = "http://localhost:8080/api/rooms/" + roomId;
        return restTemplate.getForObject(url, Room.class);
    }
    // Show event details in a modal window
    private void showEventDetailsInModal(Entry<?> entry) {
        EventDto event = fetchEventByName(entry.getTitle());
        if (event == null) {
            System.out.println("Event details not found!");
            return;
        }

        List<Seating> availableSeats = fetchAvailableSeats(event.getEventId());

        // Create and display modal window
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Event Details");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/event_details.fxml"));
        Object root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        EventDetailsController controller = loader.getController();
        controller.setEventDetails(event, availableSeats);

        Scene scene = new Scene((Parent) root, 1200, 800);
        modalStage.setScene(scene);
        modalStage.showAndWait();
    }

    // ===================== Tab Navigation ====================

    // Show default dashboard
    @FXML
    public void showDashboard() {
        showTabContent(1);
        updateTabStyles(tab1Button);
    }

    // Show content for bookings, seat management, etc.
    @FXML
    public void showBookings() {
        showTabContent(3);
        updateTabStyles(tab3Button);
    }

    @FXML
    public void showSeatManagement() {
        showTabContent(4);
        updateTabStyles(tab4Button);
    }

    @FXML
    public void showSalesRevenue() {
        showTabContent(5);
        updateTabStyles(tab5Button);
    }

    @FXML
    public void showEventTab() {
        showTabContent(6);
        updateTabStyles(tab6Button);
    }

    @FXML
    public void showClientTab() {
        showTabContent(7);
        updateTabStyles(tab7Button);
    }

    // Update styles of tab buttons
    private void updateTabStyles(Button selectedTab) {
        for (Button tab : allTabs) {
            if (tab.equals(selectedTab)) {
                tab.setStyle("-fx-background-color: #65C915; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: Sansation; -fx-font-size: 16px");
            } else {
                tab.setStyle("-fx-background-color: none; -fx-text-fill: black; -fx-font-family: Sansation; -fx-font-size: 16px");
            }
        }
    }

    // Handle content switching based on selected tab
    private void showTabContent(int tabId) {
        contentArea.getChildren().clear();

        switch (tabId) {
            case 1:
                loadDashboardFXML();
                break;
            case 2:
                contentArea.getChildren().add(new Label("Calendar View"));
                break;
            case 3:
                loadBookingManagement();
                break;
            case 4:
                loadSeatManagementFXML();
                break;
            case 5:
                loadSalesRevenue();
                break;
            case 6:
                loadEventManagement();
                break;
            case 7:
                loadClientManagement();
                break;
            default:
                contentArea.getChildren().add(new Label("Unknown tab"));
        }
    }

    // Load dashboard view from FXML
    private void loadDashboardFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboardView.fxml"));
            loader.setClassLoader(getClass().getClassLoader());
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            contentArea.getChildren().add(new Label("Error loading dashboard"));
        }
    }
    private void loadSeatManagementFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/seatManagement.fxml"));
            loader.setClassLoader(getClass().getClassLoader());
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            contentArea.getChildren().add(new Label("Error loading dashboard"));
        }
    }

    private void loadBookingManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/bookingsView.fxml"));
            loader.setClassLoader(getClass().getClassLoader());
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            contentArea.getChildren().add(new Label("Error loading dashboard"));
        }
    }
    private void loadSalesRevenue() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/salesRevenueView.fxml"));
            loader.setClassLoader(getClass().getClassLoader());
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            contentArea.getChildren().add(new Label("Error loading dashboard"));
        }
    }
    private void loadEventManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/eventsView.fxml"));
            loader.setClassLoader(getClass().getClassLoader());
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            contentArea.getChildren().add(new Label("Error loading dashboard"));
        }
    }
    private void loadClientManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClientManagement.fxml"));
            loader.setClassLoader(getClass().getClassLoader());
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            contentArea.getChildren().add(new Label("Error loading dashboard"));
        }
    }
}




    // ===================== Utility Methods ====================

//    // Fetch layouts from the API
//    private ObservableList<LayoutConfiguration> fetchLayouts() {
//        try {
//            LayoutConfiguration[] layouts = restTemplate.getForObject(API_URL, LayoutConfiguration[].class);
//            return layouts != null ? FXCollections.observableArrayList(layouts) : FXCollections.observableArrayList();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return FXCollections.observableArrayList();
//        }
//    }
//}
