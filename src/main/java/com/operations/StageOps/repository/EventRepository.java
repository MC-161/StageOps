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
import java.util.UUID;

@Repository
public class EventRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LayoutRepository layoutRepository;

    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.layoutRepository = new LayoutRepository(jdbcTemplate);
    }

    // Save a new event
    public int save(Event event) {
        // Fetch valid layouts for the given room ID
        List<LayoutConfiguration> validLayouts = layoutRepository.findLayoutsByRoomId(event.getRoomId());

        // Check if the selected layout is part of the valid layouts for this room
        boolean isValidLayout = validLayouts.stream()
                .anyMatch(layout -> layout.getLayoutId() == event.getLayoutConfiguration().getLayoutId());

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
                event.getLayoutConfiguration().getLayoutId());

        // If the event was successfully inserted, update the room's layout_id
        if (rowsAffected > 0) {
            String updateRoomSql = "UPDATE rooms SET layout_id = ? WHERE room_id = ?";
            jdbcTemplate.update(updateRoomSql, event.getLayoutConfiguration().getLayoutId(), event.getRoomId());
        }

        return rowsAffected;
    }


    // Get all events
    public List<Event> getAllEvents() {
        String sql = "SELECT * FROM events";
        return jdbcTemplate.query(sql, new EventRowMapper());
    }

    // Get event by ID
    public Event getEventById(int eventId) {
        String sql = "SELECT * FROM events WHERE event_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{eventId}, new EventRowMapper());
    }

    // Update an event
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
                event.getLayoutConfiguration().getLayoutId(),
                event.getEventId());
    }

    // Delete an event
    public int delete(int eventId) {
        String sql = "DELETE FROM events WHERE event_id = ?";
        return jdbcTemplate.update(sql, eventId);
    }
    // Get events by a specific date
    public List<Event> getEventsForDate(LocalDate date) {
        String sql = "SELECT * FROM events WHERE event_date = ?";
        return jdbcTemplate.query(sql, new Object[]{java.sql.Date.valueOf(date)}, new EventRowMapper());
    }

    // Get events within a specific date range
    public List<Event> getEventsForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM events WHERE event_date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate)}, new EventRowMapper());
    }
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





    // RowMapper for Event
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
                    new LayoutConfiguration(rs.getInt("layout_id"), "", 0, rs.getInt("room_id"), "")
            );
        }
    }
}
