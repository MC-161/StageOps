package com.operations.StageOps.service;


import com.operations.StageOps.model.Event;
import com.operations.StageOps.model.Ticket;
import com.operations.StageOps.repository.EventRepository;
import com.operations.StageOps.repository.SeatingRepository;
import com.operations.StageOps.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private SeatingRepository seatingRepository;

    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository, SeatingRepository seatingRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.seatingRepository = seatingRepository;
    }

    // Save a new ticket
    // Save a new ticket (reserve a seat) and update the event's available seats and revenue
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


    // Get all tickets
    public List<Ticket> getAllTickets() {
        return ticketRepository.getAllTickets();
    }

    // Get ticket by ID
    public Ticket getTicketById(int ticketId) {
        return ticketRepository.getTicketById(ticketId);
    }

    // Update ticket
    public int updateTicket(Ticket ticket) {
        return ticketRepository.update(ticket);
    }

    // Delete ticket
    public int deleteTicket(int ticketId) {
        return ticketRepository.delete(ticketId);
    }
}
