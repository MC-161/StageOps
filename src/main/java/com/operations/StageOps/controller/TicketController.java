package com.operations.StageOps.controller;

import com.operations.StageOps.model.Ticket;
import com.operations.StageOps.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for managing ticket operations such as reserving, retrieving, updating, and deleting tickets.
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    /**
     * Constructor for TicketController.
     *
     * @param ticketService The service handling ticket operations.
     */
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Reserves multiple tickets.
     *
     * @param tickets The list of tickets to be reserved.
     * @return ResponseEntity with success message or error message in case of failure.
     */
    @PostMapping
    public ResponseEntity<String> saveTickets(@RequestBody List<Ticket> tickets) {
        try {
            ticketService.saveTickets(tickets);
            return ResponseEntity.ok("Tickets reserved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error reserving the tickets: " + e.getMessage());
        }
    }

    /**
     * Get all tickets.
     *
     * @return List of all tickets.
     */
    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    /**
     * Retrieves a specific ticket by its ID.
     *
     * @param ticketId The ID of the ticket.
     * @return ResponseEntity containing the ticket if found, or 404 Not Found if not.
     */
    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable int ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.status(404).body(null);  // Not Found
        }
    }

    /**
     * Updates an existing ticket.
     *
     * @param ticketId The ID of the ticket to update.
     * @param ticket   The updated ticket object.
     * @return ResponseEntity indicating success or failure.
     */
    @PutMapping("/{ticketId}")
    public ResponseEntity<String> updateTicket(@PathVariable int ticketId, @RequestBody Ticket ticket) {
        ticket.setTicketId(ticketId); // Set the ID of the ticket to update
        int result = ticketService.updateTicket(ticket);
        if (result > 0) {
            return ResponseEntity.ok("Ticket updated successfully.");
        } else {
            return ResponseEntity.status(500).body("Error updating the ticket.");
        }
    }

    /**
     * Deletes a ticket by its ID.
     *
     * @param ticketId The ID of the ticket to be deleted.
     * @return ResponseEntity indicating success or failure.
     */
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<String> deleteTicket(@PathVariable int ticketId) {
        int result = ticketService.deleteTicket(ticketId);
        if (result > 0) {
            return ResponseEntity.ok("Ticket deleted successfully.");
        } else {
            return ResponseEntity.status(500).body("Error deleting the ticket.");
        }
    }

    /**
     * Retrieves the total number of tickets sold for the current week.
     *
     * @return ResponseEntity containing the total number of tickets sold.
     */
    @GetMapping("/this-week/sold")
    public ResponseEntity<Integer> getTotalTicketsSoldThisWeek() {
        LocalDate startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY); // Get the start of the current week (Monday)
        LocalDate endOfWeek = startOfWeek.plusDays(6); // End of the week (Sunday)

        int totalTicketsSold = ticketService.getTotalTicketsSoldForWeek(startOfWeek, endOfWeek);
        return ResponseEntity.ok(totalTicketsSold);
    }
}
