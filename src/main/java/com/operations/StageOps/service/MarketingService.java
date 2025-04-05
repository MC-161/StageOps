package com.operations.StageOps.service;

import com.operations.StageOps.Interfaces.IMarketingService;
import com.operations.StageOps.model.Booking;
import com.operations.StageOps.model.Contract;
import com.operations.StageOps.model.Event;
import com.operations.StageOps.repository.BookingRepository;
import com.operations.StageOps.repository.ContractRepository;
import com.operations.StageOps.repository.EventRepository;
import com.operations.StageOps.repository.SeatingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class MarketingService  implements IMarketingService {

    private final EventRepository eventRepository;
    private final SeatingRepository seatingRepository;
    private final BookingRepository bookingRepository;
    private ContractRepository contractRepository;


    public MarketingService(EventRepository eventRepository, SeatingRepository seatingRepository, BookingRepository bookingRepository, ContractRepository contractRepository) {
        this.eventRepository = eventRepository;
        this.seatingRepository = seatingRepository;
        this.bookingRepository = bookingRepository;
        this.contractRepository = contractRepository;
    }

    /**
     * Retrieve all events.
     *
     * @return A list of all events.
     */
    @Override
    public List<Event> getAllEvents() {
        return eventRepository.getAllEvents();
    }


    /**
     * Schedule an event for marketing, ensuring it is within a 3-week window from today.
     *
     * @param event The event to be scheduled for marketing.
     * @return A success or error message indicating the result.
     */
    @Override
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
        Event result = eventRepository.save(event);

        return result.getEventId() > 0 ? "Event scheduled successfully for marketing!" : "Error scheduling event for marketing.";
    }

    /**
     * Hold seats for a group booking (requires at least 12 people).
     *
     * @param eventId The ID of the event for which seats are being held.
     * @param groupSize The size of the group booking.
     * @param seatIds A list of seat IDs to be held.
     * @return A success or error message indicating the result.
     */
    @Override
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



    /**
     * Retrieve events within a specific date range (inclusive).
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of events occurring between the start and end dates.
     */
    @Override
    public List<Event> getEventsForDateRange(LocalDate startDate, LocalDate endDate) {
        return eventRepository.getEventsForDateRange(startDate, endDate);  // Query the repository for events in the range
    }

    /**
     * Retrieves all bookings from the repository.
     *
     * @return A list of all bookings.
     */
    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.getAllBookings();
    }

    /**
     * Retrieves all bookings for a room from the repository.
     *
     * @param roomId The ID of the room for which bookings are to be retrieved.
     * @return A list of all bookings for the given room.
     */
    @Override
    public List<Booking> getBookingsForRoom(int roomId) {
        return bookingRepository.getBookingsForRoom(roomId);
    }


    /**
     * Save a new event.
     *
     * @param event The event object to be saved.
     * @return The ID of the saved event.
     */
    @Override
    @Transactional
    public int saveEvent(Event event) {
        // Step 1: Save the event
        Event savedEvent = eventRepository.save(event); // Assuming this returns the saved event with generated ID

        // Check if event is successfully saved and the eventId is populated
        if (savedEvent == null || savedEvent.getEventId() <= 0) {
            throw new RuntimeException("Event could not be saved or the eventId is invalid.");
        }
        int eventId = savedEvent.getEventId();

        // Log event save success
        System.out.println("Saved event with ID: " + eventId);

        // Step 2: Create a contract for this event
        Contract contract = new Contract();
        contract.setEventId(eventId);  // Link the contract to the event
        contract.setClientId(event.getClientId());  // Assuming event has a clientId
        contract.setTerms("Standard terms and conditions for event");  // Placeholder for terms
        contract.setStatus("pending");  // Default contract status
        contract.setContractDate(ZonedDateTime.now());  // Current date and time

        // Step 3: Save the contract
        contractRepository.createContract(contract);

        // Return the event ID (success)
        return eventId;
    }


    /**
     * Update an existing event.
     *
     * @param eventId The ID of the event to be updated.
     * @param updatedEvent The updated event object.
     * @return The updated event object.
     */
    @Override
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
     * Delete an event by its ID.
     *
     * @param eventId The ID of the event to be deleted.
     * @return The number of rows affected (1 if the event is deleted, 0 otherwise).
     */
    @Override
    public int deleteEvent(int eventId) {
        return eventRepository.delete(eventId);
    }
}
