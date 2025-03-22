package com.operations.StageOps.service;


import com.operations.StageOps.model.Event;
import com.operations.StageOps.model.Ticket;
import com.operations.StageOps.repository.EventRepository;
import com.operations.StageOps.repository.SeatingRepository;
import com.operations.StageOps.repository.TicketRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private JdbcTemplate jdbcTemplate;

    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository, SeatingRepository seatingRepository, JdbcTemplate jdbcTemplate) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save a new ticket
    // Save a new ticket (reserve a seat) and update the event's available seats and revenue
    // Save multiple tickets (reserve multiple seats)
    public void saveTickets(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            // Check if the seat is already reserved for the event
            String checkSql = "SELECT COUNT(*) FROM SeatEvents WHERE event_id = ? AND seat_id = ? AND reserved = true";
            int reservedCount = jdbcTemplate.queryForObject(checkSql, new Object[]{ticket.getEventId(), ticket.getSeatId()}, Integer.class);

            if (reservedCount > 0) {
                throw new IllegalArgumentException("Seat " + ticket.getSeatId() + " is already reserved for this event.");
            }

            // Mark the seat as reserved for the event in SeatEvents table
            String insertSql = "INSERT INTO SeatEvents (seat_id, event_id, reserved, reservation_time) " +
                    "VALUES (?, ?, true, NOW()) " +
                    "ON DUPLICATE KEY UPDATE reserved = true, reservation_time = NOW()";

            jdbcTemplate.update(insertSql, ticket.getSeatId(), ticket.getEventId());

            // Update the event's ticket sales and revenue
            Event event = eventRepository.getEventById(ticket.getEventId());
            event.setTicketsSold(event.getTicketsSold() + 1);
            event.setTicketsAvailable(event.getTicketsAvailable() - 1);
            event.setTotalRevenue(event.getTotalRevenue() + ticket.getPrice());
            eventRepository.update(event);

            // Save the ticket
            ticketRepository.save(ticket);
        }
    }
    public int getTotalTicketsSoldForWeek(LocalDate startOfWeek, LocalDate endOfWeek) {
        return ticketRepository.countBySaleDateBetween(startOfWeek, endOfWeek);
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
