package com.operations.StageOps.repository;

import com.operations.StageOps.model.RevenueTracking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for handling CRUD operations related to revenue tracking in the database.
 * It provides methods to save, retrieve, and list revenue tracking records.
 */
@Repository
public class RevenueTrackingRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for the RevenueTrackingRepository.
     *
     * @param jdbcTemplate the JdbcTemplate object used to interact with the database.
     */
    public RevenueTrackingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new revenue tracking record in the database.
     *
     * @param revenueTracking the RevenueTracking object containing data to be saved.
     * @return the number of rows affected by the insert operation.
     */
    public int save(RevenueTracking revenueTracking) {
        String sql = "INSERT INTO revenue_tracking (room_id, event_id, total_revenue, ticket_sales, venue_hire) " +
                "VALUES (?, ?, ?, ?, ?)";
        // Insert data into the 'revenue_tracking' table and return the number of rows affected.
        return jdbcTemplate.update(sql, revenueTracking.getRoomId(), revenueTracking.getEventId(),
                revenueTracking.getTotalRevenue(), revenueTracking.getTicketSales(), revenueTracking.getVenueHire());
    }

    /**
     * Retrieves a revenue tracking record by its ID.
     *
     * @param revenueId the ID of the revenue tracking record to be retrieved.
     * @return the RevenueTracking object corresponding to the provided ID.
     */
    public RevenueTracking getRevenueTrackingById(int revenueId) {
        String sql = "SELECT * FROM revenue_tracking WHERE revenue_id = ?";
        // Retrieve a single revenue tracking record from the database using the provided revenue ID.
        return jdbcTemplate.queryForObject(sql, new Object[]{revenueId}, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            return revenueTracking;
        });
    }

    /**
     * Retrieves all revenue tracking records.
     *
     * @return a list of all RevenueTracking objects in the database.
     */
    public List<RevenueTracking> getAllRevenueTracking() {
        String sql = "SELECT * FROM revenue_tracking";
        // Retrieve all revenue tracking records from the database and return them as a list.
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RevenueTracking revenueTracking = new RevenueTracking();
            revenueTracking.setRevenueId(rs.getInt("revenue_id"));
            revenueTracking.setRoomId(rs.getInt("room_id"));
            revenueTracking.setEventId(rs.getInt("event_id"));
            revenueTracking.setTotalRevenue(rs.getDouble("total_revenue"));
            revenueTracking.setTicketSales(rs.getDouble("ticket_sales"));
            revenueTracking.setVenueHire(rs.getDouble("venue_hire"));
            return revenueTracking;
        });
    }
}
