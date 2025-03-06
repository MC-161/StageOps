package com.operations.StageOps.repository;

import com.operations.StageOps.model.Event;
import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Seating;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository for handling CRUD operations related to events in the database.
 * Provides methods for saving, updating, deleting, and retrieving event details.
 */
@Repository
public class EventRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LayoutRepository layoutRepository;

    /**
     * Constructs an instance of the EventRepository with the given JdbcTemplate.
     *
     * @param jdbcTemplate the JdbcTemplate to interact with the database.
     */
    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.layoutRepository = new LayoutRepository(jdbcTemplate);
    }

    /**
     * Saves a new event into the database.
     * Validates that the layout associated with the event is compatible with the room ID.
     *
     * @param event the event to be saved.
     * @return the number of rows affected by the insert operation.
     * @throws IllegalArgumentException if the layout is not valid for the specified room.
     */
    public int save(Event event) {
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

        int rowsAffected = jdbcTemplate.update(sql,
                event.getEventName(),
                event.getEventDate(),
                java.sql.Timestamp.from(event.getStartTime().toInstant()),
                java.sql.Timestamp.from(event.getEndTime().toInstant()),
                event.getRoomId(),
                event.getTicketsAvailable(),
                event.getTicketsSold(),
                event.getEventType(),
                event.getTotalRevenue(),
                event.getLayoutId());

        // If the event was successfully inserted, update the room's layout_id
        if (rowsAffected > 0) {
            String updateRoomSql = "UPDATE rooms SET layout_id = ? WHERE room_id = ?";
            jdbcTemplate.update(updateRoomSql, event.getLayoutId(), event.getRoomId());
        }

        return rowsAffected;
    }

    /**
     * Retrieves all events from the database.
     *
     * @return a list of all events.
     */
    public List<Event> getAllEvents() {
        String sql = "SELECT * FROM events";
        return jdbcTemplate.query(sql, new EventRowMapper());
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param eventId the ID of the event to be retrieved.
     * @return the event corresponding to the given ID.
     */
    public Event getEventById(int eventId) {
        String sql = "SELECT * FROM events WHERE event_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{eventId}, new EventRowMapper());
    }

    /**
     * Updates an existing event in the database.
     *
     * @param event the event to be updated with new details.
     * @return the number of rows affected by the update operation.
     */
    public int update(Event event) {
        String sql = "UPDATE events SET event_name = ?, event_date = ?, start_time = ?, end_time = ?, room_id = ?, tickets_available = ?, tickets_sold = ?, event_type = ?, total_revenue = ?, layout_id = ? WHERE event_id = ?";
        return jdbcTemplate.update(sql,
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
    }

    /**
     * Deletes an event from the database by its ID.
     *
     * @param eventId the ID of the event to be deleted.
     * @return the number of rows affected by the delete operation.
     */
    public int delete(int eventId) {
        String sql = "DELETE FROM events WHERE event_id = ?";
        return jdbcTemplate.update(sql, eventId);
    }

    /**
     * Retrieves a list of events for a specific date.
     *
     * @param date the date for which events are to be retrieved.
     * @return a list of events occurring on the specified date.
     */
    public List<Event> getEventsForDate(LocalDate date) {
        String sql = "SELECT * FROM events WHERE event_date = ?";
        return jdbcTemplate.query(sql, new Object[]{java.sql.Date.valueOf(date)}, new EventRowMapper());
    }

    /**
     * Retrieves a list of events within a specific date range.
     *
     * @param startDate the start date of the range.
     * @param endDate   the end date of the range.
     * @return a list of events occurring between the specified dates.
     */
    public List<Event> getEventsForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM events WHERE event_date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate)}, new EventRowMapper());
    }

    /**
     * Retrieves a list of available seats for a specific event.
     *
     * @param eventId the ID of the event for which available seats are to be retrieved.
     * @return a list of available seating arrangements for the event.
     */
    public List<Seating> getAvailableSeatsForEvent(int eventId) {
        String sql = "SELECT s.seat_id, s.section_name, s.seat_number, s.room_id, s.is_reserved, s.is_accessible, s.is_restricted " +
                "FROM seating s " +
                "LEFT JOIN tickets t ON s.seat_id = t.seat_id AND t.event_id = ? " +
                "WHERE s.room_id = (SELECT room_id FROM events WHERE event_id = ?) " +
                "AND (s.is_reserved = 0 OR s.is_reserved IS NULL)";

        return jdbcTemplate.query(sql, new Object[]{eventId, eventId}, (rs, rowNum) -> {
            return new Seating(
                    rs.getString("seat_id"),
                    rs.getInt("room_id"),
                    rs.getInt("seat_number"),
                    rs.getBoolean("is_reserved"),
                    rs.getBoolean("is_accessible"),
                    rs.getBoolean("is_restricted"),
                    rs.getString("section_name")
            );
        });
    }

    /**
     * RowMapper for mapping SQL result set rows to Event objects.
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
