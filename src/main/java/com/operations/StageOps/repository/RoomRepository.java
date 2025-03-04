package com.operations.StageOps.repository;

import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Room;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for managing Room entities in the database.
 * It provides methods for CRUD operations: create, read, update, and delete rooms.
 * The class interacts with the 'room' table in the database, and it also handles the relationship
 * between rooms and their associated layout configurations.
 */
@Repository
public class RoomRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for initializing the JdbcTemplate.
     *
     * @param jdbcTemplate the JdbcTemplate object used for interacting with the database.
     */
    public RoomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new room into the 'room' table in the database.
     *
     * @param room the Room object containing the data to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int save(Room room) {
        String sql = "INSERT INTO room (room_name, capacity, room_type, location, layout_id) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, room.getRoomName(), room.getCapacity(), room.getRoomType(),
                room.getLocation(), room.getLayoutConfiguration().getLayoutId());
    }

    /**
     * Retrieves a room by its ID, including the associated layout configuration.
     *
     * @param roomId the ID of the room to be fetched.
     * @return a Room object with the details of the room and its associated layout configuration.
     */
    public Room getRoomById(int roomId) {
        String sql = "SELECT * FROM room WHERE room_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{roomId}, (rs, rowNum) -> {
            Room room = new Room();
            room.setRoomId(rs.getInt("room_id"));
            room.setRoomName(rs.getString("room_name"));
            room.setCapacity(rs.getInt("capacity"));
            room.setRoomType(rs.getString("room_type"));
            room.setLocation(rs.getString("location"));
            int layoutId = rs.getInt("layout_id");
            room.setLayoutConfiguration(new LayoutConfiguration(layoutId, "", 0, roomId, "")); // The layout is set with placeholder data.
            return room;
        });
    }

    /**
     * Retrieves all rooms from the 'room' table in the database, including their layout configurations.
     *
     * @return a list of Room objects, each representing a room with its layout configuration.
     */
    public List<Room> getAllRooms() {
        String sql = "SELECT * FROM room";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Room room = new Room();
            room.setRoomId(rs.getInt("room_id"));
            room.setRoomName(rs.getString("room_name"));
            room.setCapacity(rs.getInt("capacity"));
            room.setRoomType(rs.getString("room_type"));
            room.setLocation(rs.getString("location"));
            int layoutId = rs.getInt("layout_id");
            room.setLayoutConfiguration(new LayoutConfiguration(layoutId, "", 0, room.getRoomId(), "")); // Placeholder for LayoutConfiguration.
            return room;
        });
    }

    /**
     * Updates the details of an existing room in the 'room' table.
     *
     * @param room the Room object containing the updated data.
     * @return the number of rows affected by the update query.
     */
    public int updateRoom(Room room) {
        String sql = "UPDATE room SET room_name = ?, capacity = ?, room_type = ?, location = ?, layout_id = ? WHERE room_id = ?";
        return jdbcTemplate.update(sql,
                room.getRoomName(),
                room.getCapacity(),
                room.getRoomType(),
                room.getLocation(),
                room.getLayoutConfiguration().getLayoutId(),  // Layout configuration is associated with the room.
                room.getRoomId());
    }

    /**
     * Deletes a room from the 'room' table by its ID.
     *
     * @param roomId the ID of the room to be deleted.
     * @return the number of rows affected by the delete query (usually 1 if successful).
     */
    public int deleteRoom(int roomId) {
        String sql = "DELETE FROM room WHERE room_id = ?";
        return jdbcTemplate.update(sql, roomId);  // Executes the delete operation for the given roomId.
    }
}
