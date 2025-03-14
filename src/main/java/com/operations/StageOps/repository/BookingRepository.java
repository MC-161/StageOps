package com.operations.StageOps.repository;


import com.operations.StageOps.model.Booking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class BookingRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save a new booking
    public int save(Booking booking) {
        if (booking.getStartDate() == null || booking.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null.");
        }

        java.sql.Date sqlStartDate = java.sql.Date.valueOf(booking.getStartDate());
        java.sql.Date sqlEndDate = java.sql.Date.valueOf(booking.getEndDate());

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


    // Get all bookings
    public List<Booking> getAllBookings() {
        String sql = "SELECT * FROM bookings";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Booking(rs.getInt("booking_id"), rs.getInt("client_id"), rs.getInt("room_id"), rs.getDate("start_date").toLocalDate(), rs.getDate("end_date").toLocalDate(), rs.getString("status"), rs.getDouble("total_cost")));
    }

    // Get booking by ID
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{bookingId}, (rs, rowNum) -> new Booking(rs.getInt("booking_id"), rs.getInt("client_id"), rs.getInt("room_id"), rs.getDate("start_date").toLocalDate(), rs.getDate("end_date").toLocalDate(), rs.getString("status"), rs.getDouble("total_cost")));
    }

    // Update a booking
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

    // Delete a booking
    public int delete(int bookingId) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        return jdbcTemplate.update(sql, bookingId);
    }

    public List<Booking> getBookingsForRoom(String roomId) {
        String sql = "SELECT * FROM bookings WHERE room_id = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId}, (rs, rowNum) -> new Booking(rs.getInt("booking_id"), rs.getInt("client_id"), rs.getInt("room_id"), rs.getDate("start_date").toLocalDate(), rs.getDate("end_date").toLocalDate(), rs.getString("status"), rs.getDouble("total_cost")));
    }

    private void updateRevenueTracking(int roomId, Integer booking_id, double totalRevenue, double ticketSales, double venueHire) {
        String sql = "INSERT INTO revenue_tracking (room_id, booking_id, total_revenue, ticket_sales, venue_hire) VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE total_revenue = total_revenue + VALUES(total_revenue), ticket_sales = ticket_sales + VALUES(ticket_sales), venue_hire = venue_hire + VALUES(venue_hire)";
        jdbcTemplate.update(sql, roomId, booking_id, totalRevenue, ticketSales, venueHire);
    }

    //    CHANGES
    // Get upcoming bookings
    public List<Booking> getUpcomingBookings() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        Date currentSqlDate = Date.valueOf(currentDate);

        // Query to select bookings with start date greater than the current date
        String sql = "SELECT * FROM bookings WHERE start_date > ?";

        // Execute query and return list of upcoming bookings
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
