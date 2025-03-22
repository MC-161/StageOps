package com.operations.StageOps.repository;

import com.operations.StageOps.model.RevenueTracking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class RevenueTrackingRepository {

    private final JdbcTemplate jdbcTemplate;

    public RevenueTrackingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a revenue tracking record in the database.
     *
     * @param revenueTracking the revenue tracking record to save.
     * @return the number of rows affected by the insert.
     */
    public int save(RevenueTracking revenueTracking) {
        String sql = "INSERT INTO revenue_tracking (room_id, event_id, booking_id, booking_date, ticket_sales, venue_hire, total_revenue, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, revenueTracking.getRoomId(), revenueTracking.getEventId(), revenueTracking.getBookingId(),
                revenueTracking.getBookingDate(), revenueTracking.getTicketSales(), revenueTracking.getVenueHire(),
                revenueTracking.getTotalRevenue(), revenueTracking.getStatus());
    }

    /**
     * Retrieves a revenue tracking record by its ID.
     *
     * @param revenueId the ID of the revenue tracking record.
     * @return the revenue tracking record.
     */
    public RevenueTracking getRevenueTrackingById(int revenueId) {
        String sql = "SELECT * FROM revenue_tracking WHERE revenue_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{revenueId}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));
            revenueTracking.setBookingId(rs.getObject("booking_id", Integer.class));
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    /**
     * Retrieves all revenue tracking records from the database.
     *
     * @return a list of all revenue tracking records.
     */
    public List<RevenueTracking> getAllRevenueTrackingEntries() {
        String sql = "SELECT * FROM revenue_tracking";
        return jdbcTemplate.query(sql, new Object[]{}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));
            revenueTracking.setBookingId(rs.getInt("booking_id"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    /**
     * Retrieves the total revenue for a specific date.
     *
     * @param date the date to filter by (in the format 'YYYY-MM-DD').
     * @return the total revenue for that date.
     */
    public double getTotalRevenueByDate(String date) {
        String sql = "SELECT SUM(total_revenue) FROM revenue_tracking WHERE DATE(booking_date) = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{date}, Double.class);
    }

    /**
     * Retrieves the total revenue for a specific month and year.
     *
     * @param month the month (1-12).
     * @param year the year (e.g., 2025).
     * @return the total revenue for that month and year.
     */
    public double getTotalRevenueByMonth(int month, int year) {
        String sql = "SELECT SUM(total_revenue) FROM revenue_tracking WHERE EXTRACT(MONTH FROM booking_date) = ? AND EXTRACT(YEAR FROM booking_date) = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{month, year}, Double.class);
    }

    /**
     * Retrieves the total revenue for a specific year.
     *
     * @param year the year (e.g., 2025).
     * @return the total revenue for that year.
     */
    public double getTotalRevenueByYear(int year) {
        String sql = "SELECT SUM(total_revenue) FROM revenue_tracking WHERE EXTRACT(YEAR FROM booking_date) = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{year}, Double.class);
    }

    /**
     * Retrieves all revenue tracking records for a specific event ID.
     *
     * @param eventId the event ID.
     * @return a list of revenue tracking records for that event.
     */
    public List<RevenueTracking> getRevenueTrackingEntriesByEventId(Integer eventId) {
        String sql;
        if (eventId == null) {
            sql = "SELECT * FROM revenue_tracking WHERE event_id IS NULL";
        } else {
            sql = "SELECT * FROM revenue_tracking WHERE event_id = ?";
        }

        return jdbcTemplate.query(sql, new Object[]{eventId}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));
            revenueTracking.setBookingId(rs.getObject("booking_id", Integer.class));
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    /**
     * Retrieves all revenue tracking records for a specific booking ID.
     *
     * @param bookingId the booking ID.
     * @return a list of revenue tracking records for that booking.
     */
    public List<RevenueTracking> getRevenueTrackingEntriesByBookingId(Integer bookingId) {
        String sql;
        if (bookingId == null) {
            sql = "SELECT * FROM revenue_tracking WHERE booking_id IS NULL";
        } else {
            sql = "SELECT * FROM revenue_tracking WHERE booking_id = ?";
        }

        return jdbcTemplate.query(sql, new Object[]{bookingId}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));
            revenueTracking.setBookingId(rs.getObject("booking_id", Integer.class));
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    /**
     * Retrieves all revenue tracking records for a specific date.
     *
     * @param date the date (in the format 'YYYY-MM-DD').
     * @return a list of revenue tracking records for that day.
     */
    public List<RevenueTracking> getRevenueTrackingEntriesByDay(String date) {
        String sql = "SELECT * FROM revenue_tracking WHERE DATE(booking_date) = ?";
        return jdbcTemplate.query(sql, new Object[]{date}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));
            revenueTracking.setBookingId(rs.getInt("booking_id"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    /**
     * Retrieves all revenue tracking records for a specific month and year.
     *
     * @param month the month (1-12).
     * @param year the year (e.g., 2025).
     * @return a list of revenue tracking records for that month and year.
     */
    public List<RevenueTracking> getRevenueTrackingEntriesByMonth(int month, int year) {
        String sql = "SELECT * FROM revenue_tracking WHERE EXTRACT(MONTH FROM booking_date) = ? AND EXTRACT(YEAR FROM booking_date) = ?";
        return jdbcTemplate.query(sql, new Object[]{month, year}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));
            revenueTracking.setBookingId(rs.getInt("booking_id"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    /**
     * Retrieves all revenue tracking records for a specific year.
     *
     * @param year the year (e.g., 2025).
     * @return a list of revenue tracking records for that year.
     */
    public List<RevenueTracking> getRevenueTrackingEntriesByYear(int year) {
        String sql = "SELECT * FROM revenue_tracking WHERE EXTRACT(YEAR FROM booking_date) = ?";
        return jdbcTemplate.query(sql, new Object[]{year}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));
            revenueTracking.setBookingId(rs.getInt("booking_id"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    /**
     * Retrieves revenue tracking records within a specific date range.
     *
     * @param startDate the start date (in 'YYYY-MM-DD' format).
     * @param endDate the end date (in 'YYYY-MM-DD' format).
     * @return a list of revenue tracking records within that range.
     */
    public List<RevenueTracking> getRevenueTrackingEntriesByDateRange(String startDate, String endDate) {
        String sql = "SELECT * FROM revenue_tracking WHERE booking_date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{startDate, endDate}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));
            revenueTracking.setBookingId(rs.getObject("booking_id", Integer.class));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            revenueTracking.setBookingDate(rs.getDate("booking_date").toLocalDate());
            revenueTracking.setStatus(rs.getString("status"));
            return revenueTracking;
        });
    }

    /**
     * Retrieves the total revenue for each month in a given year.
     *
     * @param year the year (e.g., 2025).
     * @return a list of maps containing month and total revenue for that year.
     */
    public List<Map<String, Object>> getTotalRevenueYear(int year) {
        String sql = "SELECT " +
                "EXTRACT(MONTH FROM booking_date) AS month, " +
                "SUM(total_revenue) AS total_revenue " +
                "FROM revenue_tracking " +
                "WHERE EXTRACT(YEAR FROM booking_date) = ? " +
                "GROUP BY EXTRACT(MONTH FROM booking_date) " +
                "ORDER BY month";
        return jdbcTemplate.queryForList(sql, year);
    }

    /**
     * Retrieves the total revenue for each day in a given month and year.
     *
     * @param month the month (1-12).
     * @param year the year (e.g., 2025).
     * @return a list of maps containing day and total revenue for that month and year.
     */
    public List<Map<String, Object>> getTotalRevenueByMonthAndYear(int month, int year) {
        String sql = "SELECT " +
                "EXTRACT(DAY FROM booking_date) AS day, " +
                "SUM(total_revenue) AS total_revenue " +
                "FROM revenue_tracking " +
                "WHERE EXTRACT(MONTH FROM booking_date) = ? " +
                "AND EXTRACT(YEAR FROM booking_date) = ? " +
                "GROUP BY EXTRACT(DAY FROM booking_date) " +
                "ORDER BY day";
        return jdbcTemplate.queryForList(sql, month, year);
    }

    /**
     * Retrieves the lifetime total revenue for each year.
     *
     * @return a list of maps containing year and total revenue for each year.
     */
    public List<Map<String, Object>> getLifetimeRevenueByYear() {
        String sql = "SELECT " +
                "EXTRACT(YEAR FROM booking_date) AS year, " +
                "SUM(total_revenue) AS total_revenue " +
                "FROM revenue_tracking " +
                "GROUP BY EXTRACT(YEAR FROM booking_date) " +
                "ORDER BY year";
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * Calculates the total revenue for a given week between the start and end date.
     *
     * @param startOfWeek the start date of the week.
     * @param endOfWeek the end date of the week.
     * @return the total revenue for that week.
     */
    public double sumRevenueByDateBetween(LocalDate startOfWeek, LocalDate endOfWeek) {
        String sql = "SELECT SUM(total_revenue) FROM revenue_tracking WHERE booking_date BETWEEN ? AND ?";
        Double result = jdbcTemplate.queryForObject(sql, Double.class, startOfWeek, endOfWeek);
        return (result != null) ? result : 0.0;
    }
}
