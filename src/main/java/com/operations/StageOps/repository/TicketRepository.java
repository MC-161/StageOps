package com.operations.StageOps.repository;

import com.operations.StageOps.model.Ticket;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TicketRepository {

    private final JdbcTemplate jdbcTemplate;

    public TicketRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save a new ticket
    public int save(Ticket ticket) {
        String sql = "INSERT INTO tickets (event_id, seat_id, price, ticket_status, sale_date) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, ticket.getEventId(), ticket.getSeatId(), ticket.getPrice(), ticket.getTicketStatus(), ticket.getSaleDate());
    }

    // Get all tickets
    public List<Ticket> getAllTickets() {
        String sql = "SELECT * FROM tickets";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ticket(rs.getInt("ticket_id"), rs.getInt("event_id"), rs.getString("seat_id"), rs.getDouble("price"), rs.getString("ticket_status"), rs.getDate("sale_date")));
    }

    // Get ticket by ID
    public Ticket getTicketById(int ticketId) {
        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{ticketId}, (rs, rowNum) -> new Ticket(rs.getInt("ticket_id"), rs.getInt("event_id"), rs.getString("seat_id"), rs.getDouble("price"), rs.getString("ticket_status"), rs.getDate("sale_date")));
    }

    // Update ticket
    public int update(Ticket ticket) {
        String sql = "UPDATE tickets SET event_id = ?, seat_id = ?, price = ?, ticket_status = ?, sale_date = ? WHERE ticket_id = ?";
        return jdbcTemplate.update(sql, ticket.getEventId(), ticket.getSeatId(), ticket.getPrice(), ticket.getTicketStatus(), ticket.getSaleDate(), ticket.getTicketId());
    }

    // Delete ticket
    public int delete(int ticketId) {
        String sql = "DELETE FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.update(sql, ticketId);
    }
}
