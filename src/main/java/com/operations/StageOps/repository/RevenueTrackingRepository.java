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
        String sql = "INSERT INTO revenue_tracking (room_id, event_id, total_revenue, ticket_sales, venue_hire) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, revenueTracking.getRoomId(), revenueTracking.getEventId(),
                revenueTracking.getTotalRevenue(), revenueTracking.getTicketSales(), revenueTracking.getVenueHire());
    }

    // Get revenue tracking by ID
    public RevenueTracking getRevenueTrackingById(int revenueId) {
        String sql = "SELECT * FROM revenue_tracking WHERE revenue_id = ?";
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

    // Get all revenue tracking records
    public List<RevenueTracking> getAllRevenueTracking() {
        String sql = "SELECT * FROM revenue_tracking";
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
