package com.operations.StageOps.repository;

import com.operations.StageOps.model.Booking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for handling booking-related database operations.
 * This class provides methods to save, retrieve, update, and delete booking records
 * in the database. It interacts with the 'bookings' table and also updates the
 * revenue tracking for rooms when a booking is saved or updated.
 */
@Repository
public class BookingRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for creating a BookingRepository instance.
     *
     * @param jdbcTemplate JdbcTemplate instance for interacting with the database.
     */
    public BookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new booking to the database.
     *
     * @param booking The booking object to be saved.
     * @return The number of rows affected in the database.
     */
    public int save(Booking booking) {
        String sql = "INSERT INTO bookings (client_id, room_id, start_date, end_date, status, total_cost) VALUES (?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql,
                booking.getClientId(),
                booking.getRoomId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus(),
                booking.getTotalCost()
        );
        if (rowsAffected > 0) {
            updateRevenueTracking(booking.getRoomId(), null, booking.getTotalCost(), 0, 0);
        }
        return jdbcTemplate.update(sql, booking.getClientId(), booking.getRoomId(), booking.getStartDate(), booking.getEndDate(), booking.getStatus(), booking.getTotalCost());
    }

    /**
     * Retrieves all bookings from the database.
     *
     * @return A list of all bookings.
     */
    public List<Booking> getAllBookings() {
        String sql = "SELECT * FROM bookings";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Booking(rs.getInt("booking_id"), rs.getInt("client_id"), rs.getInt("room_id"), rs.getDate("start_date"), rs.getDate("end_date"), rs.getString("status"), rs.getDouble("total_cost")));
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId The ID of the booking to retrieve.
     * @return The booking object with the specified ID.
     */
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{bookingId}, (rs, rowNum) -> new Booking(rs.getInt("booking_id"), rs.getInt("client_id"), rs.getInt("room_id"), rs.getDate("start_date"), rs.getDate("end_date"), rs.getString("status"), rs.getDouble("total_cost")));
    }

    /**
     * Updates an existing booking in the database.
     *
     * @param booking The booking object with updated details.
     * @return The number of rows affected in the database.
     */
    public int update(Booking booking) {
        String sql = "UPDATE bookings SET client_id = ?, room_id = ?, start_date = ?, end_date = ?, status = ?, total_cost = ? WHERE booking_id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                booking.getClientId(),
                booking.getRoomId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus(),
                booking.getTotalCost()
        );
        if (rowsAffected > 0) {
            updateRevenueTracking(booking.getRoomId(), null, booking.getTotalCost(), 0, 0);
        }
        return jdbcTemplate.update(sql, booking.getClientId(), booking.getRoomId(), booking.getStartDate(), booking.getEndDate(), booking.getStatus(), booking.getTotalCost(), booking.getBookingId());
    }

    /**
     * Deletes a booking by its ID.
     *
     * @param bookingId The ID of the booking to delete.
     * @return The number of rows affected in the database.
     */
    public int delete(int bookingId) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        return jdbcTemplate.update(sql, bookingId);
    }

    /**
     * Retrieves all bookings for a specific room.
     *
     * @param roomId The ID of the room for which bookings are to be retrieved.
     * @return A list of bookings for the specified room.
     */
    public List<Booking> getBookingsForRoom(String roomId) {
        String sql = "SELECT * FROM bookings WHERE room_id = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId}, (rs, rowNum) -> new Booking(rs.getInt("booking_id"), rs.getInt("client_id"), rs.getInt("room_id"), rs.getDate("start_date"), rs.getDate("end_date"), rs.getString("status"), rs.getDouble("total_cost")));
    }

    /**
     * Updates the revenue tracking information for a room.
     * This method is called when a booking is saved or updated to track
     * revenue from bookings, ticket sales, and venue hire.
     *
     * @param roomId The ID of the room for which revenue tracking is updated.
     * @param eventId The ID of the event (can be null).
     * @param totalRevenue The total revenue to update.
     * @param ticketSales The ticket sales to update.
     * @param venueHire The venue hire to update.
     */
    private void updateRevenueTracking(int roomId, Integer eventId, double totalRevenue, double ticketSales, double venueHire) {
        String sql = "INSERT INTO revenue_tracking (room_id, event_id, total_revenue, ticket_sales, venue_hire) VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE total_revenue = total_revenue + VALUES(total_revenue), ticket_sales = ticket_sales + VALUES(ticket_sales), venue_hire = venue_hire + VALUES(venue_hire)";
        jdbcTemplate.update(sql, roomId, eventId, totalRevenue, ticketSales, venueHire);
    }
}
