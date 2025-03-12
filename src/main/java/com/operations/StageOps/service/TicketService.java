package com.operations.StageOps.service;

import com.operations.StageOps.model.Event;
import com.operations.StageOps.model.Ticket;
import com.operations.StageOps.repository.EventRepository;
import com.operations.StageOps.repository.SeatingRepository;
import com.operations.StageOps.repository.TicketRepository;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Ticket entities.
 * Provides methods for CRUD operations (Create, Read, Update, Delete) on ticket records.
 * The class interacts with the TicketRepository, EventRepository, and SeatingRepository.
 */
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private JdbcTemplate jdbcTemplate;

    /**
     * Constructor for initializing the TicketRepository, EventRepository, and SeatingRepository.
     *
     * @param ticketRepository the TicketRepository used for interacting with the database for ticket operations.
     * @param eventRepository the EventRepository used for interacting with the database for event operations.
     * @param seatingRepository the SeatingRepository used for interacting with the database for seating operations.
     */
    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository, SeatingRepository seatingRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.jdbcTemplate = new JdbcTemplate();
    }

    /**
     * Saves a new ticket (reserves a seat) and updates the event's available seats and revenue.
     * The ticket's reservation is recorded, and the event's details such as available tickets, tickets sold, and total revenue are updated.
     *
     * @param ticket the Ticket object containing the details to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int saveTicket(Ticket ticket) {
        // Get the event
        Event event = eventRepository.getEventById(ticket.getEventId());

        // Check if the seat is already reserved for the event by checking the SeatEvents table
        String checkSql = "SELECT COUNT(*) FROM SeatEvents WHERE event_id = ? AND seat_id = ? AND reserved = true";
        int reservedCount = jdbcTemplate.queryForObject(checkSql, new Object[]{ticket.getEventId(), ticket.getSeatId()}, Integer.class);

        if (reservedCount > 0) {
            throw new IllegalArgumentException("This seat is already reserved for this event.");
        }

        // Mark the seat as reserved for this event in the SeatEvents table
        // Insert into SeatEvents, but if the entry already exists, do nothing
        String insertSql = "INSERT INTO SeatEvents (seat_id, event_id, reserved, reservation_time) " +
                "VALUES (?, ?, true, NOW()) " +
                "ON DUPLICATE KEY UPDATE reserved = true, reservation_time = NOW()";

        jdbcTemplate.update(insertSql, ticket.getSeatId(), ticket.getEventId());


        // Update the event details (tickets sold, revenue, etc.)
        event.setTicketsSold(event.getTicketsSold() + 1);
        event.setTicketsAvailable(event.getTicketsAvailable() - 1);
        event.setTotalRevenue(event.getTotalRevenue() + ticket.getPrice());
        eventRepository.update(event);

        // Save the ticket record
        return ticketRepository.save(ticket);
    }

    /**
     * Retrieves all tickets from the 'tickets' table in the database.
     *
     * @return a list of Ticket objects representing all tickets.
     */
    public List<Ticket> getAllTickets() {
        return ticketRepository.getAllTickets();
    }

    /**
     * Retrieves a specific ticket by its ID.
     *
     * @param ticketId the ID of the ticket.
     * @return the Ticket object corresponding to the given ticket ID.
     */
    public Ticket getTicketById(int ticketId) {
        return ticketRepository.getTicketById(ticketId);
    }

    /**
     * Updates an existing ticket record in the 'tickets' table.
     *
     * @param ticket the Ticket object containing the updated data.
     * @return the number of rows affected by the update query.
     */
    public int updateTicket(Ticket ticket) {
        return ticketRepository.update(ticket);
    }

    /**
     * Deletes a ticket record from the 'tickets' table based on the ticket ID.
     *
     * @param ticketId the ID of the ticket to be deleted.
     * @return the number of rows affected by the delete query (usually 1 if successful).
     */
    public int deleteTicket(int ticketId) {
        return ticketRepository.delete(ticketId);
    }
}
