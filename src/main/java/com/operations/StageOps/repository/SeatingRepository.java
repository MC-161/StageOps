package com.operations.StageOps.repository;

import com.operations.StageOps.model.Seating;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository class for managing Seating entities in the database.
 * It provides methods for CRUD operations (Create, Read, Update, Delete) on seating records.
 * The class interacts with the 'seating' table in the database.
 */
@Repository
public class SeatingRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for initializing the JdbcTemplate.
     *
     * @param jdbcTemplate the JdbcTemplate object used for interacting with the database.
     */
    public SeatingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new seating record into the 'seating' table in the database.
     * This method includes the section name along with seat-specific details.
     *
     * @param seating the Seating object containing the data to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int save(Seating seating) {
        String sql = "INSERT INTO seating (seat_id, room_id, seat_number, is_reserved, is_accessible, is_restricted, section_name) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, seating.getSeatId(), seating.getRoomId(), seating.getSeatNumber(), seating.isReserved(), seating.isAccessible(), seating.isRestricted(), seating.getSectionName());
    }

    /**
     * Retrieves all seats for a specific room and section from the database.
     *
     * @param roomId the ID of the room.
     * @param sectionName the name of the section.
     * @return a list of Seating objects corresponding to the given room and section.
     */
    public List<Seating> getAllSeatsByRoomAndSection(int roomId, String sectionName) {
        String sql = "SELECT * FROM seating WHERE room_id = ? AND section_name = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId, sectionName}, (rs, rowNum) ->
                new Seating(rs.getString("seat_id"), rs.getInt("room_id"), rs.getInt("seat_number"),
                        rs.getBoolean("is_reserved"), rs.getBoolean("is_accessible"), rs.getBoolean("is_restricted"), rs.getString("section_name")));
    }

    /**
     * Retrieves all seats for a specific room from the database.
     *
     * @param roomId the ID of the room.
     * @return a list of Seating objects corresponding to the given room.
     */
    public List<Seating> getAllSeatsByRoom(int roomId) {
        String sql = "SELECT * FROM seating WHERE room_id = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId}, (rs, rowNum) ->
                new Seating(rs.getString("seat_id"), rs.getInt("room_id"), rs.getInt("seat_number"),
                        rs.getBoolean("is_reserved"), rs.getBoolean("is_accessible"), rs.getBoolean("is_restricted"), rs.getString("section_name")));
    }

    /**
     * Retrieves a specific seat by its unique ID from the database.
     *
     * @param seatId the ID of the seat.
     * @return the Seating object corresponding to the given seat ID.
     */
    public Seating getSeatById(UUID seatId) {
        String sql = "SELECT * FROM seating WHERE seat_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{seatId}, (rs, rowNum) -> new Seating(rs.getString("seat_id"), rs.getInt("room_id"), rs.getInt("seat_number"), rs.getBoolean("is_reserved"), rs.getBoolean("is_accessible"), rs.getBoolean("is_restricted"), rs.getString("section_name")));
    }

    /**
     * Updates an existing seating record in the 'seating' table.
     *
     * @param seating the Seating object containing the updated data.
     * @return the number of rows affected by the update query.
     */
    public int update(Seating seating) {
        String sql = "UPDATE seating SET seat_id = ?,  room_id = ?, seat_number = ?, is_reserved = ?, is_accessible = ?, is_restricted = ?, section_name = ? WHERE seat_id = ?";
        return jdbcTemplate.update(sql,seating.getSeatId(), seating.getRoomId(), seating.getSeatNumber(), seating.isReserved(), seating.isAccessible(), seating.isRestricted(), seating.getSectionName(), seating.getSeatId());
    }

    /**
     * Deletes a seat record from the 'seating' table based on its seat ID.
     *
     * @param seatId the ID of the seat to be deleted.
     * @return the number of rows affected by the delete query (usually 1 if successful).
     */
    public int delete(int seatId) {
        String sql = "DELETE FROM seating WHERE seat_id = ?";
        return jdbcTemplate.update(sql, seatId);
    }

    /**
     * Retrieves all seats for a specific room from the database.
     *
     * @param roomId the ID of the room.
     * @return a list of Seating objects corresponding to the given room.
     */
    public List<Seating> findByRoomId(int roomId) {
        String sql = "SELECT * FROM seating WHERE room_id = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId}, (rs, rowNum) ->
                new Seating(rs.getString("seat_id"), rs.getInt("room_id"), rs.getInt("seat_number"),
                        rs.getBoolean("is_reserved"), rs.getBoolean("is_accessible"), rs.getBoolean("is_restricted"), rs.getString("section_name")));
    }

    /**
     * Updates the reservation status of a specific seat.
     *
     * @param seatId the ID of the seat to be updated.
     * @param isReserved the new reservation status for the seat.
     * @return the number of rows affected by the update query.
     */
    public int updateSeatStatus(String seatId, boolean isReserved) {
        String sql = "UPDATE seating SET is_reserved = ? WHERE seat_id = ?";
        return jdbcTemplate.update(sql, isReserved, seatId);
    }

    /**
     * Updates the reservation status to reserved for a list of seats.
     *
     * @param seatIds a list of seat IDs to be marked as reserved.
     */
    public void updateSeatsAsReserved(List<Integer> seatIds) {
        String sql = "UPDATE seating SET is_reserved = true WHERE seat_id = ?";
        for (Integer seatId : seatIds) {
            jdbcTemplate.update(sql, seatId);
        }
    }

    /**
     * Retrieves all available (not reserved) seats for a specific event.
     *
     * @param eventId the ID of the event.
     * @return a list of Seating objects corresponding to available seats for the event.
     */
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
