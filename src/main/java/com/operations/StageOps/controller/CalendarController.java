package com.operations.StageOps.controller;

import com.operations.StageOps.model.Booking;
import com.operations.StageOps.model.Event;
import com.operations.StageOps.service.BookingService;
import com.operations.StageOps.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")

    public class CalendarController {

        @Autowired
        private EventService eventService;

        @Autowired
        private BookingService bookingService;

    // Retrieve all events for CalendarFX
    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // Retrieve events within a specific date range
    @GetMapping("/events/date-range")
    public ResponseEntity<List<Event>> getEventsByDateRange(@RequestParam String start, @RequestParam String end) {
        // Parse the strings into LocalDate objects
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        // Call the service method with LocalDate objects
        List<Event> events = eventService.getEventsForDateRange(startDate, endDate);

        return ResponseEntity.ok(events);
    }


    // Retrieve all confirmed bookings for CalendarFX
    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Retrieve bookings for a specific room
    @GetMapping("/bookings/{roomId}")
    public ResponseEntity<List<Booking>> getBookingsForRoom(@PathVariable int roomId) {
        List<Booking> bookings = bookingService.getBookingsForRoom(roomId);
        return ResponseEntity.ok(bookings);
    }

    // Add a new event (linked to CalendarFX)
    @PostMapping("/add-event")
    public String addEvent(@RequestBody Event event) {
        int result = eventService.saveEvent(event);
        return result > 0 ? "Booking created successfully!" : "Error creating booking!";
    }

    // Update an event
    @PutMapping("/update-event/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable int eventId, @RequestBody Event updatedEvent) {
        Event event = eventService.updateEvent(eventId, updatedEvent);
        return ResponseEntity.ok(event);
    }

    // Delete an event
    @DeleteMapping("/delete-event/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable int eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
