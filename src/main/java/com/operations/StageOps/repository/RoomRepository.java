package com.operations.StageOps.repository;

import com.operations.StageOps.model.Room;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for interacting with the rooms table in the database.
 * Provides methods to save, retrieve, update, delete, and check room availability.
 */
@Repository
public class RoomRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for RoomRepository.
     *
     * @param jdbcTemplate the JdbcTemplate object for executing SQL queries
     */
    public RoomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new room in the database.
     *
     * @param room the Room object containing the room details to be saved
     * @return the number of rows affected in the database
     */
    public int save(Room room) {
        // Insert the room details into the rooms table including rates
        String sql = "INSERT INTO rooms (room_name, capacity, room_type, location, current_layout_id, " +
                "hourly_rate, evening_rate, daily_rate, weekly_rate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int rows = jdbcTemplate.update(sql, room.getRoomName(), room.getCapacity(), room.getRoomType(),
                room.getLocation(), room.getCurrentLayoutId(), room.getHourlyRate(), room.getEveningRate(),
                room.getDailyRate(), room.getWeeklyRate());

        if (rows > 0) {
            // Retrieve the generated room ID
            int roomId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

            // Insert multiple layout IDs into the room_layouts table
            String layoutSql = "INSERT INTO room_layouts (room_id, layout_id) VALUES (?, ?)";
            for (Integer layoutId : room.getLayoutIds()) {
                jdbcTemplate.update(layoutSql, roomId, layoutId);  // Insert each layoutId for the room
            }

            return rows;  // Successfully inserted room and layouts
        } else {
            return 0;  // Failed to insert room
        }
    }

    /**
     * Retrieves a room by its ID, including the layout configuration and room rates.
     *
     * @param roomId the ID of the room to be retrieved
     * @return the Room object with the specified ID, including layout configuration and rates
     */
    public Room getRoomById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        Room room = jdbcTemplate.queryForObject(sql, new Object[]{roomId}, (rs, rowNum) -> {
            Room r = new Room();
            r.setRoomId(rs.getInt("room_id"));
            r.setRoomName(rs.getString("room_name"));
            r.setCapacity(rs.getInt("capacity"));
            r.setRoomType(rs.getString("room_type"));
            r.setLocation(rs.getString("location"));
            r.setCurrentLayoutId(rs.getInt("current_layout_id"));

            // Set room rates from the result set
            r.setHourlyRate(rs.getDouble("hourly_rate"));
            r.setEveningRate(rs.getDouble("evening_rate"));
            r.setDailyRate(rs.getDouble("daily_rate"));
            r.setWeeklyRate(rs.getDouble("weekly_rate"));

            return r;
        });

        // Fetch the associated layout IDs for the room
        String layoutSql = "SELECT layout_id FROM room_layouts WHERE room_id = ?";
        List<Integer> layoutIds = jdbcTemplate.queryForList(layoutSql, new Object[]{roomId}, Integer.class);
        room.setLayoutIds(layoutIds);

        return room;
    }

    /**
     * Retrieves all rooms from the database, including rates and layout configurations.
     *
     * @return a list of all Room objects
     */
    public List<Room> getAllRooms() {
        String sql = "SELECT * FROM rooms";
        List<Room> rooms = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Room room = new Room();
            room.setRoomId(rs.getInt("room_id"));
            room.setRoomName(rs.getString("room_name"));
            room.setCapacity(rs.getInt("capacity"));
            room.setRoomType(rs.getString("room_type"));
            room.setLocation(rs.getString("location"));
            room.setCurrentLayoutId(rs.getInt("current_layout_id"));

            // Set room rates from the result set
            room.setHourlyRate(rs.getDouble("hourly_rate"));
            room.setEveningRate(rs.getDouble("evening_rate"));
            room.setDailyRate(rs.getDouble("daily_rate"));
            room.setWeeklyRate(rs.getDouble("weekly_rate"));

            return room;
        });

        // Fetch layout IDs for each room and set them
        for (Room room : rooms) {
            String layoutSql = "SELECT layout_id FROM room_layouts WHERE room_id = ?";
            List<Integer> layoutIds = jdbcTemplate.queryForList(layoutSql, new Object[]{room.getRoomId()}, Integer.class);
            room.setLayoutIds(layoutIds);
        }

        return rooms;
    }

    /**
     * Updates the details of an existing room in the database, including room rates.
     *
     * @param room the Room object containing updated room details
     * @return the number of rows affected in the database
     */
    public int updateRoom(Room room) {
        // Update the room details in the rooms table including rates
        String sql = "UPDATE rooms SET room_name = ?, capacity = ?, room_type = ?, location = ?, current_layout_id = ?, " +
                "hourly_rate = ?, evening_rate = ?, daily_rate = ?, weekly_rate = ?, vat_rate = ? WHERE room_id = ?";
        int rows = jdbcTemplate.update(sql,
                room.getRoomName(),
                room.getCapacity(),
                room.getRoomType(),
                room.getLocation(),
                room.getCurrentLayoutId(),
                room.getHourlyRate(),
                room.getEveningRate(),
                room.getDailyRate(),
                room.getWeeklyRate(),
                room.getRoomId());

        // Delete the current layout associations
        String deleteLayoutsSql = "DELETE FROM room_layouts WHERE room_id = ?";
        jdbcTemplate.update(deleteLayoutsSql, room.getRoomId());

        // Insert the new layout IDs into the room_layouts table
        String layoutSql = "INSERT INTO room_layouts (room_id, layout_id) VALUES (?, ?)";
        for (Integer layoutId : room.getLayoutIds()) {
            jdbcTemplate.update(layoutSql, room.getRoomId(), layoutId);
        }

        return rows;
    }

    /**
     * Deletes a room by its ID.
     *
     * @param roomId the ID of the room to be deleted
     * @return the number of rows affected in the database
     */
    public int deleteRoom(int roomId) {
        // First, delete the layout associations for this room
        String deleteLayoutsSql = "DELETE FROM room_layouts WHERE room_id = ?";
        jdbcTemplate.update(deleteLayoutsSql, roomId);

        // Then delete the room itself
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        return jdbcTemplate.update(sql, roomId);
    }

    /**
     * Checks if a room is available for a given time period.
     *
     * @param roomId the ID of the room to check availability for
     * @param startDate the start date of the time period
     * @param endDate the end date of the time period
     * @param eventStartTime the start time of the event
     * @param eventEndTime the end time of the event
     * @return true if the room is available, false otherwise
     */
    public boolean isRoomAvailableForTimePeriod(int roomId, LocalDate startDate, LocalDate endDate, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime) {
        // Convert LocalDate to java.sql.Date for comparison with booking date fields
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        // Convert ZonedDateTime to Timestamp for SQL compatibility
        Timestamp sqlEventStartTime = Timestamp.from(eventStartTime.toInstant());
        Timestamp sqlEventEndTime = Timestamp.from(eventEndTime.toInstant());

        // Check availability for bookings by referencing the booking_room_assignments table
        String bookingRoomAssignmentsSql = "SELECT COUNT(*) FROM booking_room_assignments WHERE room_id = ? " +
                "AND ((date BETWEEN ? AND ?) OR " +
                "(date = ? AND ? <= ?))";

        // Query the database to check if any room assignments overlap with the requested time period
        int bookingCount = jdbcTemplate.queryForObject(bookingRoomAssignmentsSql, new Object[]{
                roomId, sqlStartDate, sqlEndDate, sqlStartDate, sqlEventStartTime, sqlEventEndTime
        }, Integer.class);

        // Check availability for events (if there's an event that overlaps with the room booking time)
        String eventSql = "SELECT COUNT(*) FROM events WHERE room_id = ? AND " +
                "(event_date = ? AND (start_time < ? AND end_time > ?))";
        Date sqlEventDate = Date.valueOf(startDate);
        Timestamp sqlEventStart = Timestamp.from(eventStartTime.toInstant());
        Timestamp sqlEventEnd = Timestamp.from(eventEndTime.toInstant());

        // Query the database to check if there are any conflicting events
        int eventCount = jdbcTemplate.queryForObject(eventSql, new Object[]{roomId, sqlEventDate, sqlEventStart, sqlEventEnd}, Integer.class);

        // If either bookings or events exist that overlap, return false; otherwise, return true
        return bookingCount == 0 && eventCount == 0;
    }


    /**
     * Retrieves a list of available rooms for a given time period.
     *
     * @param startDate the start date of the time period
     * @param endDate the end date of the time period
     * @param eventStartTime the start time of the event
     * @param eventEndTime the end time of the event
     * @return a list of room IDs that are available for the given time period
     */
    public List<Integer> getAvailableRooms(LocalDate startDate, LocalDate endDate, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime) {
        // Query to get all room IDs
        String sql = "SELECT room_id FROM rooms";
        List<Integer> allRoomIds = jdbcTemplate.queryForList(sql, Integer.class);

        List<Integer> availableRooms = new ArrayList<>();

        // Check each room for availability
        for (Integer roomId : allRoomIds) {
            if (isRoomAvailableForTimePeriod(roomId, startDate, endDate, eventStartTime, eventEndTime)) {
                availableRooms.add(roomId);
            }
        }

        return availableRooms;
    }
}
