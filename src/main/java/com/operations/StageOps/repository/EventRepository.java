package com.operations.StageOps.repository;

import com.operations.StageOps.model.Event;
import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Seating;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EventRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LayoutRepository layoutRepository;
    private final SeatingRepository seatingRepository;

    /**
     * Constructor to initialize the EventRepository with JdbcTemplate and other dependencies.
     *
     * @param jdbcTemplate The JdbcTemplate instance for database operations.
     * @param seatingRepository The repository to handle seating operations.
     */
    public EventRepository(JdbcTemplate jdbcTemplate, SeatingRepository seatingRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.layoutRepository = new LayoutRepository(jdbcTemplate);
        this.seatingRepository = seatingRepository;
    }

    /**
     * Save a new event to the database.
     *
     * @param event The event object to be saved.
     * @return The number of rows affected by the insert operation.
     * @throws IllegalArgumentException If the room is not available or the layout is invalid.
     */
    public int save(Event event) {
        if (!isRoomAvailableForEvent(event.getRoomId(), event.getStartTime(), event.getEndTime())) {
            throw new IllegalArgumentException("The room is not available for the selected event time.");
        }

        // Fetch valid layouts for the given room ID
        List<LayoutConfiguration> validLayouts = layoutRepository.findLayoutsByRoomId(event.getRoomId());

        // Check if the selected layout is part of the valid layouts for this room
        boolean isValidLayout = validLayouts.stream()
                .anyMatch(layout -> layout.getLayoutId() == event.getLayoutId());

        if (!isValidLayout) {
            throw new IllegalArgumentException("The selected layout is not associated with the specified room.");
        }

        // Proceed with event creation if the layout is valid
        String sql = "INSERT INTO events (event_name, event_date, start_time, end_time, room_id, tickets_available, tickets_sold, event_type, total_revenue, layout_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Using a generated key to retrieve the event ID
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, event.getEventName());
            ps.setDate(2, new java.sql.Date(event.getEventDate().getTime()));
            ps.setTimestamp(3, java.sql.Timestamp.from(event.getStartTime().toInstant()));
            ps.setTimestamp(4, java.sql.Timestamp.from(event.getEndTime().toInstant()));
            ps.setInt(5, event.getRoomId());
            ps.setInt(6, event.getTicketsAvailable());
            ps.setInt(7, event.getTicketsSold());
            ps.setString(8, event.getEventType());
            ps.setDouble(9, event.getTotalRevenue());
            ps.setInt(10, event.getLayoutId());
            return ps;
        }, keyHolder);

        // If the event was successfully inserted, get the generated event_id
        if (rowsAffected > 0) {
            event.setEventId(keyHolder.getKey().intValue());  // Set the eventId to the generated ID

            // Associate event with the room layout
            String updateRoomSql = "UPDATE rooms SET layout_id = ? WHERE room_id = ?";
            jdbcTemplate.update(updateRoomSql, event.getLayoutId(), event.getRoomId());

            // Save seat-event associations using the eventId we just inserted
            List<Seating> seats = seatingRepository.getAllSeatsByRoom(event.getRoomId());
            List<String> seatIds = seats.stream().map(Seating::getSeatId).collect(Collectors.toList());
            seatingRepository.saveSeatEventAssociation(event.getEventId(), seatIds);

            // Update revenue tracking
            updateRevenueTracking(event.getRoomId(), event.getEventId(), event.getTotalRevenue(), event.getTicketsSold(), 0);
        }

        return rowsAffected;
    }

    /**
     * Method to update the revenue tracking table.
     *
     * @param roomId The room ID.
     * @param eventId The event ID.
     * @param totalRevenue The total revenue from the event.
     * @param ticketsSold The number of tickets sold.
     * @param venueHire The venue hire revenue.
     */
    private void updateRevenueTracking(int roomId, Integer eventId, double totalRevenue, int ticketsSold, double venueHire) {
        String sql = "INSERT INTO revenue_tracking (room_id, event_id, total_revenue, ticket_sales, venue_hire) VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE total_revenue = total_revenue + VALUES(total_revenue), ticket_sales = ticket_sales + VALUES(ticket_sales), venue_hire = venue_hire + VALUES(venue_hire)";
        jdbcTemplate.update(sql, roomId, eventId, totalRevenue, ticketsSold, venueHire);
    }

    /**
     * Get all events from the database.
     *
     * @return A list of all events.
     */
    public List<Event> getAllEvents() {
        String sql = "SELECT * FROM events";
        return jdbcTemplate.query(sql, new EventRowMapper());
    }

    /**
     * Get a specific event by its ID.
     *
     * @param eventId The ID of the event to retrieve.
     * @return The event with the specified ID.
     */
    public Event getEventById(int eventId) {
        String sql = "SELECT * FROM events WHERE event_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{eventId}, new EventRowMapper());
    }

    /**
     * Update an existing event.
     *
     * @param event The updated event object.
     * @return The number of rows affected by the update.
     * @throws IllegalArgumentException If the room is not available for the event.
     */
    public int update(Event event) {
        if (!isRoomAvailableForEvent(event.getRoomId(), event.getStartTime(), event.getEndTime())) {
            throw new IllegalArgumentException("The room is not available for the selected event time.");
        }
        String sql = "UPDATE events SET event_name = ?, event_date = ?, start_time = ?, end_time = ?, room_id = ?, tickets_available = ?, tickets_sold = ?, event_type = ?, total_revenue = ?, layout_id = ? WHERE event_id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                event.getEventName(),
                event.getEventDate(),
                java.sql.Timestamp.from(event.getStartTime().toInstant()),  // Convert ZonedDateTime to Timestamp
                java.sql.Timestamp.from(event.getEndTime().toInstant()),    // Convert ZonedDateTime to Timestamp
                event.getRoomId(),
                event.getTicketsAvailable(),
                event.getTicketsSold(),
                event.getEventType(),
                event.getTotalRevenue(),
                event.getLayoutId(),
                event.getEventId());

        if (rowsAffected > 0) {
            // Update revenue tracking for the event
            updateRevenueTracking(event.getRoomId(), event.getEventId(), event.getTotalRevenue(), event.getTicketsSold(), 0);
        }

        return rowsAffected;
    }

    /**
     * Delete an event by its ID.
     *
     * @param eventId The ID of the event to delete.
     * @return The number of rows affected by the delete operation.
     */
    public int delete(int eventId) {
        String sql = "DELETE FROM events WHERE event_id = ?";
        return jdbcTemplate.update(sql, eventId);
    }

    /**
     * Get events for a specific date.
     *
     * @param date The date to filter events.
     * @return A list of events that occur on the specified date.
     */
    public List<Event> getEventsForDate(LocalDate date) {
        String sql = "SELECT * FROM events WHERE event_date = ?";
        return jdbcTemplate.query(sql, new Object[]{java.sql.Date.valueOf(date)}, new EventRowMapper());
    }

    /**
     * Get events within a specific date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of events within the specified date range.
     */
    public List<Event> getEventsForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM events WHERE event_date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate)}, new EventRowMapper());
    }

    /**
     * Get available seats for a specific event.
     *
     * @param eventId The ID of the event to retrieve available seats for.
     * @return A list of available seating objects.
     */
    public List<Seating> getAvailableSeatsForEvent(int eventId) {
        String sql = "SELECT s.* FROM seating s " +
                "JOIN SeatEvents se ON s.seat_id = se.seat_id " +
                "WHERE se.event_id = ? AND se.reserved = false";

        return jdbcTemplate.query(sql, new Object[]{eventId}, (rs, rowNum) -> new Seating(
                rs.getString("seat_id"),
                rs.getInt("room_id"),
                rs.getInt("seat_number"),
                rs.getBoolean("is_accessible"),
                rs.getBoolean("is_restricted"),
                rs.getString("section_name")
        ));
    }

    /**
     * Get all seats for a specific event, including their reserved status.
     *
     * @param eventId The ID of the event.
     * @return A list of seats with their reserved status.
     */
    public List<Seating> getSeatsForEvent(int eventId) {
        String sql = "SELECT s.*, " +
                "CASE WHEN se.reserved = true THEN 'reserved' ELSE 'available' END AS status " +
                "FROM seating s " +
                "JOIN SeatEvents se ON s.seat_id = se.seat_id " +
                "WHERE se.event_id = ?";

        return jdbcTemplate.query(sql, new Object[]{eventId}, (rs, rowNum) -> {
            Seating seat = new Seating(
                    rs.getString("seat_id"),
                    rs.getInt("room_id"),
                    rs.getInt("seat_number"),
                    rs.getBoolean("is_accessible"),
                    rs.getBoolean("is_restricted"),
                    rs.getString("section_name")
            );
            seat.setStatus(rs.getString("status")); // Set the reserved/available status
            return seat;
        });
    }

    /**
     * Check if a room is available for a specific event time.
     *
     * @param roomId The room ID to check availability for.
     * @param startTime The start time of the event.
     * @param endTime The end time of the event.
     * @return True if the room is available, false otherwise.
     */
    public boolean isRoomAvailableForEvent(int roomId, ZonedDateTime startTime, ZonedDateTime endTime) {
        String sql = "SELECT COUNT(*) FROM events WHERE room_id = ? AND (event_date = ? AND (start_time < ? AND end_time > ?))";
        // Convert ZonedDateTime to java.sql.Date and java.sql.Timestamp
        java.sql.Date sqlEventDate = java.sql.Date.valueOf(startTime.toLocalDate());
        java.sql.Timestamp sqlStartTime = java.sql.Timestamp.from(startTime.toInstant());
        java.sql.Timestamp sqlEndTime = java.sql.Timestamp.from(endTime.toInstant());

        int count = jdbcTemplate.queryForObject(sql, new Object[]{roomId, sqlEventDate, sqlEndTime, sqlStartTime}, Integer.class);
        return count == 0;  // Return true if no conflicting events are found
    }

    /**
     * Get a list of upcoming events.
     *
     * @return A list of upcoming events.
     */
    public List<Event> getUpcomingEvents() {
        String sql = "SELECT * FROM events WHERE event_date >= CURDATE() ORDER BY event_date ASC";
        return jdbcTemplate.query(sql, new EventRowMapper());
    }

    /**
     * Count the total number of events within the current week.
     *
     * @param startOfWeek The start date of the current week.
     * @param endOfWeek The end date of the current week.
     * @return The number of events within the current week.
     */
    public int countByEventDateBetween(LocalDate startOfWeek, LocalDate endOfWeek) {
        String sql = "SELECT COUNT(*) FROM events WHERE event_date BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, startOfWeek, endOfWeek);
    }

    /**
     * RowMapper implementation for mapping rows of the 'events' table to Event objects.
     */
    private static class EventRowMapper implements RowMapper<Event> {

        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            // Retrieve the Timestamp values
            java.sql.Timestamp startTimestamp = rs.getTimestamp("start_time");
            java.sql.Timestamp endTimestamp = rs.getTimestamp("end_time");

            // Convert them to ZonedDateTime
            ZonedDateTime startTime = startTimestamp != null ? startTimestamp.toInstant().atZone(ZoneId.systemDefault()) : null;
            ZonedDateTime endTime = endTimestamp != null ? endTimestamp.toInstant().atZone(ZoneId.systemDefault()) : null;

            // Create and return the Event object
            return new Event(
                    rs.getInt("event_id"),
                    rs.getString("event_name"),
                    rs.getDate("event_date"),
                    startTime,
                    endTime,
                    rs.getInt("room_id"),
                    rs.getInt("tickets_available"),
                    rs.getInt("tickets_sold"),
                    rs.getString("event_type"),
                    rs.getDouble("total_revenue"),
                    rs.getInt("layout_id")
            );
        }
    }
}
