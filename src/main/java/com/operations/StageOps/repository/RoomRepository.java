package com.operations.StageOps.repository;

import com.operations.StageOps.model.LayoutConfiguration;
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
        String sql = "INSERT INTO rooms (room_name, capacity, room_type, location, layout_id) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, room.getRoomName(), room.getCapacity(), room.getRoomType(), room.getLocation(), room.getLayoutConfiguration().getLayoutId());
    }

    /**
     * Retrieves a room by its ID, including the layout configuration.
     *
     * @param roomId the ID of the room to be retrieved
     * @return the Room object with the specified ID, including layout configuration
     */
    public Room getRoomById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{roomId}, (rs, rowNum) -> {
            Room room = new Room();
            room.setRoomId(rs.getInt("room_id"));
            room.setRoomName(rs.getString("room_name"));
            room.setCapacity(rs.getInt("capacity"));
            room.setRoomType(rs.getString("room_type"));
            room.setLocation(rs.getString("location"));
            int layoutId = rs.getInt("layout_id");
            room.setLayoutConfiguration(new LayoutConfiguration(layoutId, "", 0, roomId, ""));  // Assuming LayoutConfig is fetched in a separate query
            return room;
        });
    }

    /**
     * Retrieves all rooms from the database.
     *
     * @return a list of all Room objects
     */
    public List<Room> getAllRooms() {
        String sql = "SELECT * FROM rooms";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Room room = new Room();
            room.setRoomId(rs.getInt("room_id"));
            room.setRoomName(rs.getString("room_name"));
            room.setCapacity(rs.getInt("capacity"));
            room.setRoomType(rs.getString("room_type"));
            room.setLocation(rs.getString("location"));
            int layoutId = rs.getInt("layout_id");
            room.setLayoutConfiguration(new LayoutConfiguration(layoutId, "", 0, room.getRoomId(), ""));
            return room;
        });
    }

    /**
     * Updates the details of an existing room in the database.
     *
     * @param room the Room object containing updated room details
     * @return the number of rows affected in the database
     */
    public int updateRoom(Room room) {
        String sql = "UPDATE rooms SET room_name = ?, capacity = ?, room_type = ?, location = ?, layout_id = ? WHERE room_id = ?";
        return jdbcTemplate.update(sql,
                room.getRoomName(),
                room.getCapacity(),
                room.getRoomType(),
                room.getLocation(),
                room.getLayoutConfiguration().getLayoutId(),  // Assuming layout configuration is linked to room
                room.getRoomId());
    }

    /**
     * Deletes a room by its ID.
     *
     * @param roomId the ID of the room to be deleted
     * @return the number of rows affected in the database
     */
    public int deleteRoom(int roomId) {
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
        // Check availability for bookings
        String bookingSql = "SELECT COUNT(*) FROM bookings WHERE room_id = ? AND (start_date < ? AND end_date > ?)";
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        int bookingCount = jdbcTemplate.queryForObject(bookingSql, new Object[]{roomId, sqlEndDate, sqlStartDate}, Integer.class);

        // Check availability for events
        String eventSql = "SELECT COUNT(*) FROM events WHERE room_id = ? AND (event_date = ? AND (start_time < ? AND end_time > ?))";
        Date sqlEventDate = Date.valueOf(startDate);
        Timestamp sqlEventStartTime = Timestamp.from(eventStartTime.toInstant());
        Timestamp sqlEventEndTime = Timestamp.from(eventEndTime.toInstant());

        int eventCount = jdbcTemplate.queryForObject(eventSql, new Object[]{roomId, sqlEventDate, sqlEventEndTime, sqlEventStartTime}, Integer.class);

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
