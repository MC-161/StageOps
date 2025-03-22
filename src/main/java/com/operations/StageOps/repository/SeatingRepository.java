package com.operations.StageOps.repository;

import com.operations.StageOps.model.Seating;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for interacting with the seating table in the database.
 * Provides methods to save, retrieve, update, delete, and manage seat availability.
 */
@Repository
public class SeatingRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for SeatingRepository.
     *
     * @param jdbcTemplate the JdbcTemplate object for executing SQL queries
     */
    public SeatingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new seat in the database.
     *
     * @param seating the Seating object containing the seat details to be saved
     * @return the number of rows affected in the database
     */
    public int save(Seating seating) {
        String sql = "INSERT INTO seating (seat_id, room_id, seat_number, is_accessible, is_restricted, section_name) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, seating.getSeatId(), seating.getRoomId(), seating.getSeatNumber(),
                seating.isAccessible(), seating.isRestricted(), seating.getSectionName());
    }

    /**
     * Retrieves all seats for a given room and section.
     *
     * @param roomId      the ID of the room
     * @param sectionName the section name of the seating
     * @return a list of Seating objects for the specified room and section
     */
    public List<Seating> getAllSeatsByRoomAndSection(int roomId, String sectionName) {
        String sql = "SELECT * FROM seating WHERE room_id = ? AND section_name = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId, sectionName}, (rs, rowNum) ->
                new Seating(rs.getString("seat_id"), rs.getInt("room_id"), rs.getInt("seat_number"),
                        rs.getBoolean("is_accessible"), rs.getBoolean("is_restricted"), rs.getString("section_name")));
    }

    /**
     * Retrieves all seats for a given room.
     *
     * @param roomId the ID of the room
     * @return a list of all Seating objects for the specified room
     */
    public List<Seating> getAllSeatsByRoom(int roomId) {
        String sql = "SELECT * FROM seating WHERE room_id = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId}, (rs, rowNum) ->
                new Seating(rs.getString("seat_id"), rs.getInt("room_id"), rs.getInt("seat_number"),
                        rs.getBoolean("is_accessible"), rs.getBoolean("is_restricted"), rs.getString("section_name")));
    }

    /**
     * Retrieves a seat by its ID.
     *
     * @param seatId the ID of the seat to be retrieved
     * @return the Seating object for the specified seat ID
     */
    public Seating getSeatById(String seatId) {
        String sql = "SELECT * FROM seating WHERE seat_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{seatId}, (rs, rowNum) ->
                new Seating(rs.getString("seat_id"), rs.getInt("room_id"), rs.getInt("seat_number"),
                        rs.getBoolean("is_accessible"), rs.getBoolean("is_restricted"), rs.getString("section_name")));
    }

    /**
     * Updates the details of an existing seat.
     *
     * @param seating the Seating object containing updated seat details
     * @return the number of rows affected in the database
     */
    public int update(Seating seating) {
        String sql = "UPDATE seating SET seat_id = ?, room_id = ?, seat_number = ?, is_accessible = ?, is_restricted = ?, section_name = ? WHERE seat_id = ?";
        return jdbcTemplate.update(sql, seating.getSeatId(), seating.getRoomId(), seating.getSeatNumber(),
                seating.isAccessible(), seating.isRestricted(), seating.getSectionName(), seating.getSeatId());
    }

    /**
     * Deletes a seat by its ID.
     *
     * @param seatId the ID of the seat to be deleted
     * @return the number of rows affected in the database
     */
    public int delete(int seatId) {
        String sql = "DELETE FROM seating WHERE seat_id = ?";
        return jdbcTemplate.update(sql, seatId);
    }

    /**
     * Retrieves all seats for a given room ID.
     *
     * @param roomId the ID of the room
     * @return a list of Seating objects for the specified room ID
     */
    public List<Seating> findByRoomId(int roomId) {
        String sql = "SELECT * FROM seating WHERE room_id = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId}, (rs, rowNum) ->
                new Seating(rs.getString("seat_id"), rs.getInt("room_id"), rs.getInt("seat_number"),
                        rs.getBoolean("is_accessible"), rs.getBoolean("is_restricted"), rs.getString("section_name")));
    }

    /**
     * Updates the status of seats to reserved for a specific event.
     *
     * @param seatIds a list of seat IDs to be reserved
     * @param eventId the event ID associated with the reservation
     */
    public void updateSeatsAsReserved(List<String> seatIds, int eventId) {
        String sql = "INSERT INTO SeatEvents (seat_id, event_id, reserved, reservation_time) VALUES (?, ?, true, NOW()) " +
                "ON DUPLICATE KEY UPDATE reserved = true, reservation_time = NOW()";

        for (String seatId : seatIds) {
            jdbcTemplate.update(sql, seatId, eventId);
        }
    }

    /**
     * Retrieves a list of reserved seat IDs for a specific event.
     *
     * @param eventId the ID of the event
     * @return a list of seat IDs that are reserved for the event
     */
    public List<String> getReservedSeatsForEvent(int eventId) {
        String sql = "SELECT seat_id FROM SeatEvents WHERE event_id = ? AND reserved = true";
        return jdbcTemplate.query(sql, new Object[]{eventId}, (rs, rowNum) -> rs.getString("seat_id"));
    }

    /**
     * Saves the association between seats and an event.
     *
     * @param eventId the event ID
     * @param seatIds a list of seat IDs to be associated with the event
     */
    public void saveSeatEventAssociation(int eventId, List<String> seatIds) {
        String sql = "INSERT INTO SeatEvents (seat_id, event_id) VALUES (?, ?)";
        for (String seatId : seatIds) {
            jdbcTemplate.update(sql, seatId, eventId);
        }
    }

    /**
     * Retrieves all available seats for a specific event (seats that are not yet reserved).
     *
     * @param eventId the event ID for which to check seat availability
     * @return a list of available Seating objects for the specified event
     */
    public List<Seating> getAvailableSeatsForEvent(int eventId) {
        // Get seats that are not reserved for the specific event
        String sql = "SELECT * FROM seating s " +
                "LEFT JOIN SeatEvents se ON s.seat_id = se.seat_id AND se.event_id = ? " +
                "WHERE se.reserved = false OR se.seat_id IS NULL";

        return jdbcTemplate.query(sql, new Object[]{eventId}, (rs, rowNum) -> new Seating(
                rs.getString("seat_id"),
                rs.getInt("room_id"),
                rs.getInt("seat_number"),
                rs.getBoolean("is_accessible"),
                rs.getBoolean("is_restricted"),
                rs.getString("section_name")
        ));
    }
}
