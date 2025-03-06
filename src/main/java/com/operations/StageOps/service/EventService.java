package com.operations.StageOps.service;

import com.operations.StageOps.model.Event;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.repository.EventRepository;
import com.operations.StageOps.repository.SeatingRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.time.LocalDate;

/**
 * Service class for managing Event entities.
 * It provides methods for CRUD operations (Create, Read, Update, Delete) and other business logic related to events.
 * The class interacts with the `EventRepository` and `SeatingRepository` to perform database operations.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final SeatingRepository seatingRepository;

    /**
     * Constructor for initializing the EventRepository and SeatingRepository.
     *
     * @param eventRepository the EventRepository used for interacting with the event database.
     * @param seatingRepository the SeatingRepository used for interacting with the seating database.
     */
    public EventService(EventRepository eventRepository, SeatingRepository seatingRepository) {
        this.eventRepository = eventRepository;
        this.seatingRepository = seatingRepository;
    }

    /**
     * Saves a new event record into the database.
     *
     * @param event the Event object containing the data to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int saveEvent(Event event) {
        return eventRepository.save(event);
    }

    /**
     * Retrieves all events from the database.
     *
     * @return a list of Event objects representing all events in the database.
     */
    public List<Event> getAllEvents() {
        return eventRepository.getAllEvents();
    }

    /**
     * Retrieves a specific event by its ID from the database.
     *
     * @param eventId the ID of the event to retrieve.
     * @return the Event object corresponding to the given event ID.
     */
    public Event getEventById(int eventId) {
        return eventRepository.getEventById(eventId);
    }

    /**
     * Updates an existing event record in the database.
     *
     * @param eventId the ID of the event to be updated.
     * @param updatedEvent the Event object containing the updated data.
     * @return the updated Event object.
     */
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

    /**
     * Retrieves all available seats for a specific event.
     *
     * @param eventId the ID of the event for which available seats are to be fetched.
     * @return a list of Seating objects representing the available seats for the event.
     */
    public List<Seating> getAvailableSeats(int eventId) {
        return eventRepository.getAvailableSeatsForEvent(eventId);
    }

    /**
     * Deletes an event record from the database based on the event ID.
     *
     * @param eventId the ID of the event to be deleted.
     * @return the number of rows affected by the delete query (usually 1 if successful).
     */
    public int deleteEvent(int eventId) {
        return eventRepository.delete(eventId);
    }

    /**
     * Retrieves all events scheduled for a specific date.
     *
     * @param date the LocalDate representing the date to search for events.
     * @return a list of Event objects that are scheduled for the given date.
     */
    public List<Event> getEventsForDate(LocalDate date) {
        return eventRepository.getEventsForDate(date);  // Query the repository for events on the date
    }

    /**
     * Retrieves all events scheduled within a specific date range.
     *
     * @param startDate the starting date of the range.
     * @param endDate the ending date of the range.
     * @return a list of Event objects that are scheduled within the date range.
     */
    public List<Event> getEventsForDateRange(LocalDate startDate, LocalDate endDate) {
        return eventRepository.getEventsForDateRange(startDate, endDate);  // Query the repository for events in the range
    }

    /**
     * Schedules an event for marketing, allowing it to be created only if it's within a 3-week window.
     *
     * @param event the Event object to be scheduled for marketing.
     * @return a message indicating whether the event was successfully scheduled or not.
     */
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

    /**
     * Holds seats for a group booking, ensuring that the group size is at least 12 people and seats are available.
     *
     * @param eventId the ID of the event for which seats are being held.
     * @param groupSize the number of seats being held.
     * @param seatIds a list of seat IDs to be held.
     * @return a message indicating whether the seats were successfully held or not.
     */
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
}
