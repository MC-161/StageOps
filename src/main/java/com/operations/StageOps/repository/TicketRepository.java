package com.operations.StageOps.repository;

import com.operations.StageOps.model.Ticket;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for managing Ticket entities in the database.
 * It provides methods for CRUD operations (Create, Read, Update, Delete) on ticket records.
 * The class interacts with the 'tickets' table in the database.
 */
@Repository
public class TicketRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for initializing the JdbcTemplate.
     *
     * @param jdbcTemplate the JdbcTemplate object used for interacting with the database.
     */
    public TicketRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new ticket record into the 'tickets' table in the database.
     *
     * @param ticket the Ticket object containing the data to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int save(Ticket ticket) {
        String sql = "INSERT INTO tickets (event_id, seat_id, price, ticket_status, sale_date) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, ticket.getEventId(), ticket.getSeatId(), ticket.getPrice(), ticket.getTicketStatus(), ticket.getSaleDate());
    }

    /**
     * Retrieves all tickets from the 'tickets' table in the database.
     *
     * @return a list of Ticket objects representing all tickets.
     */
    public List<Ticket> getAllTickets() {
        String sql = "SELECT * FROM tickets";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ticket(rs.getInt("ticket_id"), rs.getInt("event_id"), rs.getString("seat_id"), rs.getDouble("price"), rs.getString("ticket_status"), rs.getDate("sale_date")));
    }

    /**
     * Retrieves a specific ticket by its ID from the 'tickets' table.
     *
     * @param ticketId the ID of the ticket.
     * @return the Ticket object corresponding to the given ticket ID.
     */
    public Ticket getTicketById(int ticketId) {
        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{ticketId}, (rs, rowNum) -> new Ticket(rs.getInt("ticket_id"), rs.getInt("event_id"), rs.getString("seat_id"), rs.getDouble("price"), rs.getString("ticket_status"), rs.getDate("sale_date")));
    }

    /**
     * Updates an existing ticket record in the 'tickets' table.
     *
     * @param ticket the Ticket object containing the updated data.
     * @return the number of rows affected by the update query.
     */
    public int update(Ticket ticket) {
        String sql = "UPDATE tickets SET event_id = ?, seat_id = ?, price = ?, ticket_status = ?, sale_date = ? WHERE ticket_id = ?";
        return jdbcTemplate.update(sql, ticket.getEventId(), ticket.getSeatId(), ticket.getPrice(), ticket.getTicketStatus(), ticket.getSaleDate(), ticket.getTicketId());
    }

    /**
     * Deletes a ticket record from the 'tickets' table based on the ticket ID.
     *
     * @param ticketId the ID of the ticket to be deleted.
     * @return the number of rows affected by the delete query (usually 1 if successful).
     */
    public int delete(int ticketId) {
        String sql = "DELETE FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.update(sql, ticketId);
    }
}
