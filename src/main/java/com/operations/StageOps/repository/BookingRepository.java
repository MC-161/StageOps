package com.operations.StageOps.repository;

import com.operations.StageOps.model.Booking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class BookingRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor to initialize the BookingRepository with JdbcTemplate.
     *
     * @param jdbcTemplate JdbcTemplate for interacting with the database.
     */
    public BookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new booking to the database.
     *
     * @param booking The booking object containing details of the booking to be saved.
     * @return The number of rows affected by the insert operation.
     * @throws IllegalArgumentException If the room is not available or the booking dates are invalid.
     */
    public int save(Booking booking) {
        if (!isRoomAvailableForBooking(booking.getRoomId(), booking.getStartDate(), booking.getEndDate())) {
            throw new IllegalArgumentException("The room is not available for the selected dates.");
        }

        if (booking.getStartDate() == null || booking.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null.");
        }

        Date sqlStartDate = Date.valueOf(booking.getStartDate());
        Date sqlEndDate = Date.valueOf(booking.getEndDate());

        String sql = "INSERT INTO bookings (client_id, room_id, start_date, end_date, status, total_cost) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"booking_id"});
            ps.setInt(1, booking.getClientId());
            ps.setInt(2, booking.getRoomId());
            ps.setDate(3, sqlStartDate);
            ps.setDate(4, sqlEndDate);
            ps.setString(5, booking.getStatus());
            ps.setDouble(6, booking.getTotalCost());
            return ps;
        }, keyHolder);

        if (rowsAffected > 0) {
            // Get the newly generated booking_id
            int generatedBookingId = keyHolder.getKey().intValue();
            updateRevenueTracking(booking.getRoomId(), generatedBookingId, booking.getTotalCost(), 0, 0);
        }

        return rowsAffected;
    }

    /**
     * Retrieves all bookings from the database.
     *
     * @return A list of all bookings.
     */
    public List<Booking> getAllBookings() {
        String sql = "SELECT * FROM bookings";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Booking(
                rs.getInt("booking_id"),
                rs.getInt("client_id"),
                rs.getInt("room_id"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getString("status"),
                rs.getDouble("total_cost")
        ));
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId The ID of the booking to retrieve.
     * @return The booking with the specified ID.
     */
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{bookingId}, (rs, rowNum) -> new Booking(
                rs.getInt("booking_id"),
                rs.getInt("client_id"),
                rs.getInt("room_id"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getString("status"),
                rs.getDouble("total_cost")
        ));
    }

    /**
     * Updates an existing booking in the database.
     *
     * @param booking The updated booking object.
     * @return The number of rows affected by the update operation.
     * @throws IllegalArgumentException If the room is not available for the updated booking dates.
     */
    public int update(Booking booking) {
        if (!isRoomAvailableForBooking(booking.getRoomId(), booking.getStartDate(), booking.getEndDate())) {
            throw new IllegalArgumentException("The room is not available for the selected dates.");
        }

        String sql = "UPDATE bookings SET client_id = ?, room_id = ?, start_date = ?, end_date = ?, status = ?, total_cost = ? WHERE booking_id = ?";

        int rowsAffected = jdbcTemplate.update(sql,
                booking.getClientId(),
                booking.getRoomId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus(),
                booking.getTotalCost(),
                booking.getBookingId() // Ensure bookingId is included for the WHERE clause
        );

        if (rowsAffected > 0) {
            updateRevenueTracking(booking.getRoomId(), null, booking.getTotalCost(), 0, 0);
        }

        return rowsAffected;
    }

    /**
     * Checks if the room is available for the specified booking dates.
     *
     * @param roomId The room ID to check availability for.
     * @param startDate The start date of the booking.
     * @param endDate The end date of the booking.
     * @return True if the room is available, false if not.
     */
    public boolean isRoomAvailableForBooking(int roomId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE room_id = ? AND (start_date < ? AND end_date > ?)";
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        int count = jdbcTemplate.queryForObject(sql, new Object[]{roomId, sqlEndDate, sqlStartDate}, Integer.class);
        return count == 0;  // Return true if no conflicting bookings are found
    }

    /**
     * Deletes a booking by its ID.
     *
     * @param bookingId The ID of the booking to delete.
     * @return The number of rows affected by the delete operation.
     */
    public int delete(int bookingId) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        return jdbcTemplate.update(sql, bookingId);
    }

    /**
     * Retrieves all bookings for a specific room.
     *
     * @param roomId The room ID for which to retrieve bookings.
     * @return A list of bookings for the specified room.
     */
    public List<Booking> getBookingsForRoom(String roomId) {
        String sql = "SELECT * FROM bookings WHERE room_id = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId}, (rs, rowNum) -> new Booking(
                rs.getInt("booking_id"),
                rs.getInt("client_id"),
                rs.getInt("room_id"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getString("status"),
                rs.getDouble("total_cost")
        ));
    }

    /**
     * Updates the revenue tracking data for a room based on a booking.
     *
     * @param roomId The room ID to update revenue tracking for.
     * @param bookingId The booking ID related to the revenue.
     * @param totalRevenue The total revenue to update.
     * @param ticketSales The ticket sales amount to update.
     * @param venueHire The venue hire amount to update.
     */
    private void updateRevenueTracking(int roomId, Integer bookingId, double totalRevenue, double ticketSales, double venueHire) {
        String sql = "INSERT INTO revenue_tracking (room_id, booking_id, total_revenue, ticket_sales, venue_hire) VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE total_revenue = total_revenue + VALUES(total_revenue), ticket_sales = ticket_sales + VALUES(ticket_sales), venue_hire = venue_hire + VALUES(venue_hire)";
        jdbcTemplate.update(sql, roomId, bookingId, totalRevenue, ticketSales, venueHire);
    }

    /**
     * Retrieves all upcoming bookings (i.e., bookings with a start date greater than the current date).
     *
     * @return A list of upcoming bookings.
     */
    public List<Booking> getUpcomingBookings() {
        LocalDate currentDate = LocalDate.now();
        Date currentSqlDate = Date.valueOf(currentDate);

        String sql = "SELECT * FROM bookings WHERE start_date > ?";

        return jdbcTemplate.query(sql, new Object[]{currentSqlDate}, (rs, rowNum) -> new Booking(
                rs.getInt("booking_id"),
                rs.getInt("client_id"),
                rs.getInt("room_id"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getString("status"),
                rs.getDouble("total_cost")
        ));
    }
}
