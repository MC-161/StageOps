package com.operations.StageOps.controller;//package com.example.operations.stageOps.controller;
//
//import com.example.operations.stageOps.model.LayoutConfiguration;
//import com.example.operations.stageOps.model.Seating;
//import com.example.operations.stageOps.model.Ticket;
//import com.example.operations.stageOps.service.EventService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.example.operations.stageOps.service.TicketService;
//import com.example.operations.stageOps.service.LayoutService;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@RequestMapping("/box-office")
//public class BoxOfficeController {
//    private final TicketService ticketService;
//    private final LayoutService layoutService;
//
//    private final EventService eventService;
//
//    @Autowired
//    public BoxOfficeController(TicketService ticketService, LayoutService layoutService, EventService eventService) {
//        this.ticketService = ticketService;
//        this.layoutService = layoutService;
//        this.eventService = eventService;
//    }
//
//
//    @PostMapping
//    public ResponseEntity<String> saveTickets(@RequestBody List<Ticket> tickets) {
//        try {
//            ticketService.saveTickets(tickets);
//            return ResponseEntity.ok("Tickets reserved successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error reserving the tickets: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/{ticketId}")
//    public ResponseEntity<Ticket> getTicketById(@PathVariable int ticketId) {
//        Ticket ticket = ticketService.getTicketById(ticketId);
//        if (ticket != null) {
//            return ResponseEntity.ok(ticket);
//        } else {
//            return ResponseEntity.status(404).body(null);  // Not Found
//        }
//    }
//
//    // Get total tickets sold for the current week
//    @GetMapping("/this-week/sold")
//    public ResponseEntity<Integer> getTotalTicketsSoldThisWeek() {
//        LocalDate startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY); // Get the start of the current week (Monday)
//        LocalDate endOfWeek = startOfWeek.plusDays(6); // End of the week (Sunday)
//
//        int totalTicketsSold = ticketService.getTotalTicketsSoldForWeek(startOfWeek, endOfWeek);
//        return ResponseEntity.ok(totalTicketsSold);
//    }
//
//    @GetMapping("rooms/{roomId}")
//    public List<LayoutConfiguration> getLayoutsForRoom(@PathVariable int roomId) {
//        return layoutService.getLayoutsForRoom(roomId);
//    }
//
//    @GetMapping("/{eventId}/availableSeats")
//    public List<Seating> getEventAvailableSeats(@PathVariable int eventId) {
//        return eventService.getAvailableSeats(eventId);
//    }
//
//    @GetMapping("/{eventId}/seats")
//    public List<Seating> getEventSeats(@PathVariable int eventId) {
//        return eventService.getSeatsForEvent(eventId);
//    }
//
//}
