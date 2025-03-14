package com.operations.StageOps.repository;

import com.operations.StageOps.model.RevenueTracking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RevenueTrackingRepository {

    private final JdbcTemplate jdbcTemplate;

    public RevenueTrackingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save revenue tracking
    public int save(RevenueTracking revenueTracking) {
        String sql = "INSERT INTO revenue_tracking (room_id, event_id, booking_id, booking_date, ticket_sales, venue_hire, total_revenue, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, revenueTracking.getRoomId(), revenueTracking.getEventId(), revenueTracking.getBookingId(),
                revenueTracking.getBookingDate(), revenueTracking.getTicketSales(), revenueTracking.getVenueHire(),
                revenueTracking.getTotalRevenue(), revenueTracking.getStatus());
    }

    // Get a single revenue tracking record by ID
    public RevenueTracking getRevenueTrackingById(int revenueId) {
        String sql = "SELECT * FROM revenue_tracking WHERE revenue_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{revenueId}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));

            // Safely handle nullable event_id and booking_id
            revenueTracking.setEventId(rs.getInt("event_id"));  // Will return null if event_id is NULL
            revenueTracking.setBookingId(rs.getObject("booking_id", Integer.class)); // Will return null if booking_id is NULL

            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    // Get all revenue tracking records (entries)
    public List<RevenueTracking> getAllRevenueTrackingEntries() {
        String sql = "SELECT * FROM revenue_tracking";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));  // Safely handle nullable event_id
            revenueTracking.setBookingId(rs.getObject("booking_id", Integer.class)); // Safely handle nullable booking_id
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    // Get total revenue by date
    public double getTotalRevenueByDate(String date) {
        String sql = "SELECT SUM(total_revenue) \n" +
                "FROM revenue_tracking \n" +
                "WHERE DATE(booking_date) = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{date}, Double.class);
    }

    // Get total revenue by month and year
    public double getTotalRevenueByMonth(int month, int year) {
        String sql = "SELECT SUM(total_revenue) FROM revenue_tracking WHERE EXTRACT(MONTH FROM booking_date) = ? AND EXTRACT(YEAR FROM booking_date) = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{month, year}, Double.class);
    }

    // Get total revenue by year
    public double getTotalRevenueByYear(int year) {
        String sql = "SELECT SUM(total_revenue) FROM revenue_tracking WHERE EXTRACT(YEAR FROM booking_date) = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{year}, Double.class);
    }

    // Get revenue tracking entries by event ID
    public List<RevenueTracking> getRevenueTrackingEntriesByEventId(Integer eventId) {
        String sql;
        if (eventId == null) {
            sql = "SELECT * FROM revenue_tracking WHERE event_id IS NULL"; // Adjust query for NULL values
        } else {
            sql = "SELECT * FROM revenue_tracking WHERE event_id = ?";
        }

        return jdbcTemplate.query(sql, new Object[]{eventId}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));  // Safely handle nullable event_id
            revenueTracking.setBookingId(rs.getObject("booking_id", Integer.class)); // Safely handle nullable booking_id
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    // Get revenue tracking entries by booking ID
    public List<RevenueTracking> getRevenueTrackingEntriesByBookingId(Integer bookingId) {
        String sql;
        if (bookingId == null) {
            sql = "SELECT * FROM revenue_tracking WHERE booking_id IS NULL"; // Adjust query for NULL values
        } else {
            sql = "SELECT * FROM revenue_tracking WHERE booking_id = ?";
        }

        return jdbcTemplate.query(sql, new Object[]{bookingId}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));  // Safely handle nullable event_id
            revenueTracking.setBookingId(rs.getObject("booking_id", Integer.class)); // Safely handle nullable booking_id
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    // Get revenue tracking entries by day
    public List<RevenueTracking> getRevenueTrackingEntriesByDay(String date) {
        String sql = "SELECT * FROM revenue_tracking WHERE DATE(booking_date) = ?";
        return jdbcTemplate.query(sql, new Object[]{date}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));  // Safely handle nullable event_id
            revenueTracking.setBookingId(rs.getInt("booking_id")); // Safely handle nullable booking_id
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    // Get revenue tracking entries by month and year
    public List<RevenueTracking> getRevenueTrackingEntriesByMonth(int month, int year) {
        String sql = "SELECT * FROM revenue_tracking WHERE EXTRACT(MONTH FROM booking_date) = ? AND EXTRACT(YEAR FROM booking_date) = ?";
        return jdbcTemplate.query(sql, new Object[]{month, year}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id")); // Safely handle nullable event_id
            revenueTracking.setBookingId(rs.getInt("booking_id")); // Safely handle nullable booking_id
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    // Get revenue tracking entries by year
    public List<RevenueTracking> getRevenueTrackingEntriesByYear(int year) {
        String sql = "SELECT * FROM revenue_tracking WHERE EXTRACT(YEAR FROM booking_date) = ?";
        return jdbcTemplate.query(sql, new Object[]{year}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));  // Safely handle nullable event_id
            revenueTracking.setBookingId(rs.getInt("booking_id")); // Safely handle nullable booking_id
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    // Get revenue tracking entries by date range
    public List<RevenueTracking> getRevenueTrackingEntriesByDateRange(String startDate, String endDate) {
        String sql = "SELECT * FROM revenue_tracking WHERE booking_date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{startDate, endDate}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id")); ; // Safely handle nullable event_id
            revenueTracking.setBookingId(rs.getObject("booking_id", Integer.class)); // Safely handle nullable booking_id
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }
}
