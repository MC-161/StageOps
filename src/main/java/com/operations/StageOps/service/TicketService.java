package com.operations.StageOps.service;

import com.operations.StageOps.model.Event;
import com.operations.StageOps.model.Ticket;
import com.operations.StageOps.repository.EventRepository;
import com.operations.StageOps.repository.SeatingRepository;
import com.operations.StageOps.repository.TicketRepository;
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
    private final SeatingRepository seatingRepository;

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
        this.seatingRepository = seatingRepository;
    }

    /**
     * Saves a new ticket (reserves a seat) and updates the event's available seats and revenue.
     * The ticket's reservation is recorded, and the event's details such as available tickets, tickets sold, and total revenue are updated.
     *
     * @param ticket the Ticket object containing the details to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int saveTicket(Ticket ticket) {
        // Save the ticket (i.e., reserve a seat)
        int result = ticketRepository.save(ticket);

        if (result > 0) {
            // Get the event by ID
            Event event = eventRepository.getEventById(ticket.getEventId());

            // Decrease available tickets by 1
            event.setTicketsAvailable(event.getTicketsAvailable() - 1);

            // Increase tickets sold by 1
            event.setTicketsSold(event.getTicketsSold() + 1);

            // Add the ticket price to the total revenue
            event.setTotalRevenue(event.getTotalRevenue() + ticket.getPrice());

            // Update the event
            eventRepository.update(event);

            // Mark the seat as reserved
            seatingRepository.updateSeatStatus(ticket.getSeatId(), true);

            return result;
        }

        return 0; // Error in ticket reservation
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
