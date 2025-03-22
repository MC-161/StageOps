package com.operations.StageOps.repository;

import com.operations.StageOps.model.Ticket;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository class for interacting with the tickets table in the database.
 * Provides methods to save, retrieve, update, delete tickets and query ticket sales.
 */
@Repository
public class TicketRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for TicketRepository.
     *
     * @param jdbcTemplate the JdbcTemplate object for executing SQL queries
     */
    public TicketRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new ticket in the database.
     *
     * @param ticket the Ticket object containing the ticket details to be saved
     * @return the number of rows affected in the database
     */
    public int save(Ticket ticket) {
        String sql = "INSERT INTO tickets (event_id, seat_id, price, ticket_status, sale_date) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, ticket.getEventId(), ticket.getSeatId(), ticket.getPrice(),
                ticket.getTicketStatus(), ticket.getSaleDate());
    }

    /**
     * Retrieves all tickets in the database.
     *
     * @return a list of all Ticket objects in the database
     */
    public List<Ticket> getAllTickets() {
        String sql = "SELECT * FROM tickets";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ticket(rs.getInt("ticket_id"), rs.getInt("event_id"),
                rs.getString("seat_id"), rs.getDouble("price"), rs.getString("ticket_status"), rs.getDate("sale_date")));
    }

    /**
     * Retrieves a ticket by its ID.
     *
     * @param ticketId the ID of the ticket to be retrieved
     * @return the Ticket object for the specified ticket ID
     */
    public Ticket getTicketById(int ticketId) {
        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{ticketId}, (rs, rowNum) -> new Ticket(rs.getInt("ticket_id"),
                rs.getInt("event_id"), rs.getString("seat_id"), rs.getDouble("price"), rs.getString("ticket_status"), rs.getDate("sale_date")));
    }

    /**
     * Updates the details of an existing ticket.
     *
     * @param ticket the Ticket object containing updated ticket details
     * @return the number of rows affected in the database
     */
    public int update(Ticket ticket) {
        String sql = "UPDATE tickets SET event_id = ?, seat_id = ?, price = ?, ticket_status = ?, sale_date = ? WHERE ticket_id = ?";
        return jdbcTemplate.update(sql, ticket.getEventId(), ticket.getSeatId(), ticket.getPrice(),
                ticket.getTicketStatus(), ticket.getSaleDate(), ticket.getTicketId());
    }

    /**
     * Counts the total number of tickets sold during the current week.
     *
     * @param startOfWeek the start date of the week (inclusive)
     * @param endOfWeek   the end date of the week (inclusive)
     * @return the count of tickets sold during the week
     */
    public int countBySaleDateBetween(LocalDate startOfWeek, LocalDate endOfWeek) {
        String sql = "SELECT COUNT(*) FROM tickets WHERE sale_date BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, startOfWeek, endOfWeek);
    }

    /**
     * Deletes a ticket by its ID.
     *
     * @param ticketId the ID of the ticket to be deleted
     * @return the number of rows affected in the database
     */
    public int delete(int ticketId) {
        String sql = "DELETE FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.update(sql, ticketId);
    }
}
