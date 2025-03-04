package com.operations.StageOps.service;



import com.operations.StageOps.model.Event;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.model.Section;
import com.operations.StageOps.repository.EventRepository;
import com.operations.StageOps.repository.SeatingRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

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
            existingEvent.setLayoutConfiguration(updatedEvent.getLayoutConfiguration());

            // Save the updated event back to the database
            eventRepository.update(existingEvent);
        }

        return existingEvent;
    }
    public List<Seating> getAvailableSeats(int eventId) {
        // Fetch the available seats using the repository method
        return eventRepository.getAvailableSeatsForEvent(eventId);
    }

    // Delete event
    public int deleteEvent(int eventId) {
        return eventRepository.delete(eventId);
    }

    // Get events for a specific date
    public List<Event> getEventsForDate(LocalDate date) {
        return eventRepository.getEventsForDate(date);  // Query the repository for events on the date
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
    public String holdSeatsForGroupBooking(int eventId, int groupSize, List<Integer> seatIds) {
        if (groupSize < 12) {
            return "Error: Group booking must be for at least 12 people.";
        }

        // Check if seats are available for holding
        List<Seating> availableSeats = seatingRepository.getAvailableSeatsForEvent(eventId);

        // Verify that the requested seats are available for holding
        for (Integer seatId : seatIds) {
            boolean isAvailable = availableSeats.stream()
                    .anyMatch(seat -> seat.getSeatId().equals(seatId) && !seat.isReserved());
            if (!isAvailable) {
                return "Error: Some of the requested seats are not available.";
            }
        }

        // Mark the seats as reserved (held by marketing)
        seatingRepository.updateSeatsAsReserved(seatIds);

        return "Seats held successfully for group booking.";
    }
};

