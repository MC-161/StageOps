package com.operations.StageOps.repository;

import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Room;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomRepository {

    private final JdbcTemplate jdbcTemplate;

    public RoomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save a new room
    public int save(Room room) {
        String sql = "INSERT INTO room (room_name, capacity, room_type, location, layout_id) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, room.getRoomName(), room.getCapacity(), room.getRoomType(), room.getLocation(), room.getLayoutConfiguration().getLayoutId());
    }

    // Get room by ID, including the layout configuration
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
            room.setLayoutConfiguration(new LayoutConfiguration(layoutId, "", 0, roomId, ""));  // Assuming LayoutConfig is fetched in a separate query
            return room;
        });
    }

    // Get all rooms
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
            room.setLayoutConfiguration(new LayoutConfiguration(layoutId, "", 0, room.getRoomId(), ""));
            return room;
        });
    }
    // Update room details in the database
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
    // Delete room by roomId
    public int deleteRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        return jdbcTemplate.update(sql, roomId);
    }
}
