package com.operations.StageOps.controller;

import com.operations.StageOps.model.Event;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    /**
     * Constructor for the EventController class.
     * Initializes the controller with an EventService instance.
     *
     * @param eventService The service used to manage events
     */
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Creates a new event.
     * This method receives an {@link Event} object in the request body,
     * passes it to the {@link EventService}, and returns a success or error message.
     *
     * @param event The event details to be saved
     * @return A message indicating whether the event was created successfully
     */
    @PostMapping("/schedule")
    public String createEvent(@RequestBody Event event) {
        int result = eventService.saveEvent(event);
        return result > 0 ? "Client created successfully!" : "Error creating Client!";
    }

    /**
     * Retrieves all upcoming events.
     * This method calls the {@link EventService} to fetch all upcoming events from the database.
     *
     * @return A list of all upcoming events
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        List<Event> events = eventService.getUpcomingEvents();
        return ResponseEntity.ok(events);
    }

    /**
     * Retrieves an event by its ID.
     * This method uses the event ID passed in the URL path to fetch the corresponding event.
     *
     * @param eventId The ID of the event to retrieve
     * @return The event with the specified ID
     */
    @GetMapping("/{eventId}")
    public Event getEvent(@PathVariable int eventId) {
        return eventService.getEventById(eventId);
    }


    /**
     * Retrieves the available seats for an event.
     * This method uses the event ID passed in the URL path to fetch the available seats for the event.
     *
     * @param eventId The ID of the event to retrieve available seats for
     * @return A list of available seats for the event
     */
    @GetMapping("/{eventId}/availableSeats")
    public List<Seating> getEventAvailableSeats(@PathVariable int eventId) {
        return eventService.getAvailableSeats(eventId);
    }

    /**
     * Retrieves the seats for an event.
     * This method uses the event ID passed in the URL path to fetch the seats for the event.
     *
     * @param eventId The ID of the event to retrieve seats for
     * @return A list of seats for the event
     */
    @GetMapping("/{eventId}/seats")
    public List<Seating> getEventSeats(@PathVariable int eventId) {
        return eventService.getSeatsForEvent(eventId);
    }


    /**
     * Retrieves all events.
     * This method calls the {@link EventService} to fetch all events from the database.
     *
     * @return A list of all events
     */
    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    /**
     * Updates an existing event.
     * This method receives an {@link Event} object in the request body and updates the corresponding event.
     *
     * @param eventId The ID of the event to update
     * @param event The updated event details
     * @return The updated event
     */
    @PutMapping("/{eventId}")
    public Event updateEvent(@PathVariable int eventId, @RequestBody Event event) {
        return eventService.updateEvent(eventId, event);
    }

    /**
     * Deletes an event by its ID.
     * This method deletes the event with the specified ID by calling the {@link EventService}.
     *
     * @param eventId The ID of the event to delete
     */
    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable int eventId) {
        eventService.deleteEvent(eventId);
    }

    /**
     * Schedules an event for marketing.
     * This method receives an {@link Event} object in the request body,
     * passes it to the {@link EventService}, and returns a success or error message.
     *
     * @param event The event details to be scheduled for marketing
     * @return A message indicating whether the event was scheduled for marketing successfully
     */
    @PostMapping("/scheduleForMarketing")
    public String scheduleForMarketing(@RequestBody Event event) {
        return eventService.scheduleForMarketing(event);
    }


    /**
     * Retrieves all events scheduled for marketing.
     * This method calls the {@link EventService} to fetch all events scheduled for marketing from the database.
     *
     * @return A list of all events scheduled for marketing
     */
    @PostMapping("/{eventId}/holdSeats")
    public String holdSeatsForGroupBooking(@PathVariable int eventId, @RequestParam int groupSize, @RequestBody List<String> seatIds) {
        return eventService.holdSeatsForGroupBooking(eventId, groupSize, seatIds);
    }

    /**
     * Retrieves the total number of events scheduled for the current week.
     * This method calls the {@link EventService} to fetch the total number of events scheduled for the current week.
     *
     * @return The total number of events scheduled for the current week
     */
    @GetMapping("/this-week/count")
    public ResponseEntity<Integer> getTotalEventsThisWeek() {
        LocalDate startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY); // Get the start of the current week (Monday)
        LocalDate endOfWeek = startOfWeek.plusDays(6); // End of the week (Sunday)

        int totalEvents = eventService.getTotalEventsForWeek(startOfWeek, endOfWeek);
        return ResponseEntity.ok(totalEvents);
    }


}
