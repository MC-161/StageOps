package com.operations.StageOps.repository;

import com.operations.StageOps.model.Seating;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SeatingRepository {

    private final JdbcTemplate jdbcTemplate;

    public SeatingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save a new seat (now includes sectionName)
    public int save(Seating seating) {
        String sql = "INSERT INTO seating (seat_id, room_id, seat_number, is_reserved, is_accessible, is_restricted, section_name) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, seating.getSeatId(), seating.getRoomId(), seating.getSeatNumber(), seating.isReserved(), seating.isAccessible(), seating.isRestricted(), seating.getSectionName());
    }

    // Get all seats by room and section (this now considers sections)
    public List<Seating> getAllSeatsByRoomAndSection(int roomId, String sectionName) {
        String sql = "SELECT * FROM seating WHERE room_id = ? AND section_name = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId, sectionName}, (rs, rowNum) ->
                new Seating(rs.getString("seat_id"), rs.getInt("room_id"), rs.getInt("seat_number"),
                        rs.getBoolean("is_reserved"), rs.getBoolean("is_accessible"), rs.getBoolean("is_restricted"), rs.getString("section_name")));
    }
    public List<Seating> getAllSeatsByRoom(int roomId) {
        String sql = "SELECT * FROM seating WHERE room_id = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId}, (rs, rowNum) ->
                new Seating(rs.getString("seat_id"), rs.getInt("room_id"), rs.getInt("seat_number"),
                        rs.getBoolean("is_reserved"), rs.getBoolean("is_accessible"), rs.getBoolean("is_restricted"), rs.getString("section_name")));
    }


    // Get seat by ID
    public Seating getSeatById(UUID seatId) {
        String sql = "SELECT * FROM seating WHERE seat_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{seatId}, (rs, rowNum) -> new Seating(rs.getString("seat_id"), rs.getInt("room_id"), rs.getInt("seat_number"), rs.getBoolean("is_reserved"), rs.getBoolean("is_accessible"), rs.getBoolean("is_restricted"), rs.getString("section_name")));
    }

    // Update seat
    public int update(Seating seating) {
        String sql = "UPDATE seating SET seat_id = ?,  room_id = ?, seat_number = ?, is_reserved = ?, is_accessible = ?, is_restricted = ?, section_name = ? WHERE seat_id = ?";
        return jdbcTemplate.update(sql,seating.getSeatId(), seating.getRoomId(), seating.getSeatNumber(), seating.isReserved(), seating.isAccessible(), seating.isRestricted(), seating.getSectionName(), seating.getSeatId());
    }

    // Delete seat
    public int delete(int seatId) {
        String sql = "DELETE FROM seating WHERE seat_id = ?";
        return jdbcTemplate.update(sql, seatId);
    }

    public List<Seating> findByRoomId(int roomId) {
        String sql = "SELECT * FROM seating WHERE room_id = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId}, (rs, rowNum) ->
                new Seating(rs.getString("seat_id"), rs.getInt("room_id"), rs.getInt("seat_number"),
                        rs.getBoolean("is_reserved"), rs.getBoolean("is_accessible"), rs.getBoolean("is_restricted"), rs.getString("section_name")));
    }

    public int updateSeatStatus(String seatId, boolean isReserved) {
        String sql = "UPDATE seating SET is_reserved = ? WHERE seat_id = ?";
        return jdbcTemplate.update(sql, isReserved, seatId);
    }

    // Method to update seat status to reserved (held by marketing)
    public void updateSeatsAsReserved(List<Integer> seatIds) {
        String sql = "UPDATE seating SET is_reserved = true WHERE seat_id = ?";
        for (Integer seatId : seatIds) {
            jdbcTemplate.update(sql, seatId);
        }
    }

    // Method to fetch available seats for a specific event
    public List<Seating> getAvailableSeatsForEvent(int eventId) {
        String sql = "SELECT * FROM seating WHERE event_id = ? AND is_reserved = false";
        return jdbcTemplate.query(sql, new Object[]{eventId}, (rs, rowNum) -> {
            return new Seating(
                    rs.getString("seat_id"),
                    rs.getInt("event_id"),
                    rs.getInt("seat_number"),
                    rs.getBoolean("is_reserved"),
                    rs.getBoolean("is_accessible"),
                    rs.getBoolean("is_restricted"),
                    rs.getString("section_name")
            );
        });
    }
}
