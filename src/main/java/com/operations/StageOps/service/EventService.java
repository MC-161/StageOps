package com.operations.StageOps.service;


import com.operations.StageOps.model.Event;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.repository.EventRepository;
import com.operations.StageOps.repository.SeatingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private SeatingRepository seatingRepository;

    public EventService(EventRepository eventRepository, SeatingRepository seatingRepository) {
        this.eventRepository = eventRepository;
        this.seatingRepository = seatingRepository;
    }

    // Save a new event
    public int saveEvent(Event event) {
        return eventRepository.save(event);
    }

    // Get all events
    public List<Event> getAllEvents() {
        return eventRepository.getAllEvents();
    }

    // Get event by ID
    public Event getEventById(int eventId) {
        return eventRepository.getEventById(eventId);
    }

    // Update event
    // Update event
    public Event updateEvent(int eventId, Event updatedEvent) {
        // Fetch the existing event by eventId
        Event existingEvent = eventRepository.getEventById(eventId);

        if (existingEvent != null) {
            // Update fields
            existingEvent.setEventName(updatedEvent.getEventName());
            existingEvent.setEventDate(updatedEvent.getEventDate());
            existingEvent.setStartTime(updatedEvent.getStartTime());
            existingEvent.setEndTime(updatedEvent.getEndTime());
            existingEvent.setRoomId(updatedEvent.getRoomId());
            existingEvent.setTicketsAvailable(updatedEvent.getTicketsAvailable());
            existingEvent.setTicketsSold(updatedEvent.getTicketsSold());
            existingEvent.setEventType(updatedEvent.getEventType());
            existingEvent.setTotalRevenue(updatedEvent.getTotalRevenue());
            existingEvent.setLayoutId(updatedEvent.getLayoutId());

            // Save the updated event back to the database
            eventRepository.update(existingEvent);
        }

        return existingEvent;
    }
    public List<Seating> getAvailableSeats(int eventId) {
        // Fetch the available seats using the repository method
        return eventRepository.getAvailableSeatsForEvent(eventId);
    }
    public List<Seating> getSeatsForEvent(int eventId) {
        return eventRepository.getSeatsForEvent(eventId);
    }

    // Delete event
    public int deleteEvent(int eventId) {
        return eventRepository.delete(eventId);
    }

    // Get events for a specific date
    public List<Event> getEventsForDate(LocalDate date) {
        return eventRepository.getEventsForDate(date);  // Query the repository for events on the date
    }

    public List<Event> getUpcomingEvents() {
        return eventRepository.getUpcomingEvents();
    }

    // Get events within a date range (start date to end date)
    public List<Event> getEventsForDateRange(LocalDate startDate, LocalDate endDate) {
        return eventRepository.getEventsForDateRange(startDate, endDate);  // Query the repository for events in the range
    }

    public String scheduleForMarketing(Event event) {
        // Ensure the event's eventDate is a LocalDate (convert if necessary)
        LocalDate eventDate = event.getEventDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Get today's date and the date 3 weeks from today
        LocalDate today = LocalDate.now();
        LocalDate threeWeeksFromNow = today.plusWeeks(3);

        // Compare eventDate with the 3-week window
        if (eventDate.isAfter(threeWeeksFromNow)) {
            return "Error: Marketing can only schedule events within 3 weeks in advance.";
        }

        // Proceed with event creation
        int result = eventRepository.save(event);

        return result > 0 ? "Event scheduled successfully for marketing!" : "Error scheduling event for marketing.";
    }


    // Method to hold seats for marketing (group booking)
    public String holdSeatsForGroupBooking(int eventId, int groupSize, List<String> seatIds) {
        // Ensure the group size is at least 12
        if (groupSize < 12) {
            return "Error: Group booking must be for at least 12 people.";
        }

        // Get a list of seat IDs that are already reserved for this event
        List<String> reservedSeatIds = seatingRepository.getReservedSeatsForEvent(eventId);

        // Check if any requested seat is already reserved
        for (String seatId : seatIds) {
            if (reservedSeatIds.contains(seatId)) {
                return "Error: Some of the requested seats are already reserved.";
            }
        }

        // Mark the seats as reserved for this event
        seatingRepository.updateSeatsAsReserved(seatIds, eventId);

        return "Seats held successfully for group booking.";
    }

    public int getTotalEventsForWeek(LocalDate startOfWeek, LocalDate endOfWeek) {
        return eventRepository.countByEventDateBetween(startOfWeek, endOfWeek); // This assumes the `EventRepository` has a suitable method
    }

};

