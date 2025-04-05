package com.operations.StageOps.service;


import com.operations.StageOps.model.*;
import com.operations.StageOps.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service class responsible for handling operations related to events.
 * Provides methods to save, update, retrieve, and delete events.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private SeatingRepository seatingRepository;
    private ContractRepository contractRepository;
    private RoomRepository roomRepository;
    private ClientRepository clientRepository;

    /**
     * Constructor for EventService.
     *
     * @param eventRepository The repository used to interact with event data.
     * @param seatingRepository The repository used to interact with seating data.
     * @param contractRepository The repository used to interact with contract data.
     * @param roomRepository The repository used to interact with room data.
     * @param clientRepository The repository used to interact with client data.
     */
    public EventService(EventRepository eventRepository, SeatingRepository seatingRepository, ContractRepository contractRepository,
                        RoomRepository roomRepository, ClientRepository clientRepository) {
        this.eventRepository = eventRepository;
        this.seatingRepository = seatingRepository;
        this.contractRepository = contractRepository;
        this.roomRepository = roomRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Schedules an event and automatically creates a contract for it.
     *
     * @param event The event object to be scheduled.
     * @return A success or error message.
     */

    @Transactional
    public int saveEvent(Event event) {
        // Step 1: Save the event
        Event savedEvent = eventRepository.save(event);

        // Check if event is successfully saved and the eventId is populated
        if (savedEvent == null || savedEvent.getEventId() <= 0) {
            throw new RuntimeException("Event could not be saved or the eventId is invalid.");
        }
        int eventId = savedEvent.getEventId();

        // Log event save success
        System.out.println("Saved event with ID: " + eventId);

        // Step 2: Get room information
        Room room = roomRepository.getRoomById(event.getRoomId());
        if (room == null) {
            throw new RuntimeException("Room not found with ID: " + event.getRoomId());
        }

        Client client = clientRepository.getClientById(event.getClientId());
        if (client == null) {
            throw new RuntimeException("Client not found with ID: " + event.getClientId());
        }


        // Step 3: Create a contract for this event with enhanced details
        Contract contract = new Contract();

        // Basic contract fields
        contract.setEventId(eventId);
        contract.setClientId(event.getClientId());
        contract.setTerms("Standard terms and conditions for event");
        contract.setStatus("to_sign");
        contract.setContractDate(ZonedDateTime.now());

        // Venue information - use room location and name for the address
        String venueAddress = room.getLocation() + " - " + room.getRoomName();
        // If location is empty, use a default address
        if (venueAddress == null || venueAddress.trim().isEmpty() || venueAddress.equals(" - " + room.getRoomName())) {
            venueAddress = "123 Event Place, Main Street, City";
        }
        contract.setVenueAddress(venueAddress);
        contract.setEventDescription(event.getEventName() + " - " + event.getEventType());

        // Rental period details
        LocalDateTime startDateTime = event.getStartTime() != null ?
                event.getStartTime().toLocalDateTime() :
                LocalDateTime.now();
        LocalDateTime endDateTime = event.getEndTime() != null ?
                event.getEndTime().toLocalDateTime() :
                startDateTime.plusHours(3);

        contract.setStartDateTime(startDateTime);
        contract.setEndDateTime(endDateTime);

        // Payment details - using hourlyRate from Room model
        BigDecimal baseAmount = calculateBaseAmount(event, room);
        contract.setTotalAmount(baseAmount);
        contract.setInitialAmount(baseAmount.multiply(BigDecimal.valueOf(0.5))); // 50% deposit
        contract.setFinalAmount(baseAmount.multiply(BigDecimal.valueOf(0.5))); // Remaining 50%
        contract.setFinalPaymentDate(startDateTime.toLocalDate().minusDays(7));
        contract.setGracePeriod(5);
        contract.setLateFee("10%");

        // Security deposit
        contract.setDepositAmount(baseAmount.multiply(BigDecimal.valueOf(0.25))); // 25% security deposit
        contract.setDepositReturnDays(14);

        // Cancellation terms
        contract.setCancellationNoticeDays(30);
        contract.setCancellationFee("50%");

        // Contract parties
        contract.setOwnerName("Venue Management Company");
        contract.setRenterName(client.getName());

        // Step 4: Save the contract
        contractRepository.createContract(contract);

        // Return the event ID
        return eventId;
    }

    // Helper method to calculate the base amount for the contract
    private BigDecimal calculateBaseAmount(Event event, Room room) {
        // Calculate duration in hours
        long hours = 0;
        if (event.getStartTime() != null && event.getEndTime() != null) {
            hours = ChronoUnit.HOURS.between(
                    event.getStartTime().toLocalDateTime(),
                    event.getEndTime().toLocalDateTime()
            );
        } else {
            // Default to 3 hours if times are not specified
            hours = 3;
        }

        // Ensure minimum of 1 hour
        hours = Math.max(1, hours);

        // Select appropriate rate based on duration
        double rate;
        if (hours <= 4) {
            // Hourly rate for short events
            rate = room.getHourlyRate() * hours;
        } else if (hours <= 8) {
            // Evening rate for medium events
            rate = room.getEveningRate();
        } else if (hours <= 24) {
            // Daily rate for full-day events
            rate = room.getDailyRate();
        } else {
            // Weekly rate prorated for multi-day events
            double days = Math.ceil(hours / 24.0);
            rate = (days / 7) * room.getWeeklyRate();
        }

        // Apply event type factor
        double eventTypeFactor = getEventTypeFactor(event.getEventType());

        // Calculate and round to 2 decimal places
        double amount = rate * eventTypeFactor;
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
    }

    // Helper method to determine factor based on event type
    private double getEventTypeFactor(String eventType) {
        if (eventType == null) {
            return 1.0;
        }

        switch(eventType.toLowerCase()) {
            case "wedding":
                return 1.5;
            case "corporate":
                return 1.3;
            case "concert":
                return 1.4;
            default:
                return 1.0;
        }
    }
    /**
     * Retrieve all events.
     *
     * @return A list of all events.
     */
    public List<Event> getAllEvents() {
        return eventRepository.getAllEvents();
    }

    /**
     * Retrieve an event by its ID.
     *
     * @param eventId The ID of the event to be retrieved.
     * @return The event associated with the provided ID.
     */
    public Event getEventById(int eventId) {
        return eventRepository.getEventById(eventId);
    }

    /**
     * Update an existing event.
     *
     * @param eventId The ID of the event to be updated.
     * @param updatedEvent The updated event object.
     * @return The updated event object.
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
     * Retrieve all available seats for a specific event.
     *
     * @param eventId The ID of the event for which seats are to be retrieved.
     * @return A list of available seats for the specified event.
     */
    public List<Seating> getAvailableSeats(int eventId) {
        // Fetch the available seats using the repository method
        return eventRepository.getAvailableSeatsForEvent(eventId);
    }

    /**
     * Retrieve all seats associated with a specific event.
     *
     * @param eventId The ID of the event for which seats are to be retrieved.
     * @return A list of all seats for the specified event.
     */
    public List<Seating> getSeatsForEvent(int eventId) {
        return eventRepository.getSeatsForEvent(eventId);
    }

    /**
     * Delete an event by its ID.
     *
     * @param eventId The ID of the event to be deleted.
     * @return The number of rows affected (1 if the event is deleted, 0 otherwise).
     */
    public int deleteEvent(int eventId) {
        return eventRepository.delete(eventId);
    }

    /**
     * Retrieve events for a specific date.
     *
     * @param date The date to filter events by.
     * @return A list of events occurring on the specified date.
     */
    public List<Event> getEventsForDate(LocalDate date) {
        return eventRepository.getEventsForDate(date);  // Query the repository for events on the date
    }

    /**
     * Retrieve upcoming events.
     *
     * @return A list of events that are upcoming.
     */
    public List<Event> getUpcomingEvents() {
        return eventRepository.getUpcomingEvents();
    }

    /**
     * Retrieve events within a specific date range (inclusive).
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of events occurring between the start and end dates.
     */
    public List<Event> getEventsForDateRange(LocalDate startDate, LocalDate endDate) {
        return eventRepository.getEventsForDateRange(startDate, endDate);  // Query the repository for events in the range
    }

    /**
     * Schedule an event for marketing, ensuring it is within a 3-week window from today.
     *
     * @param event The event to be scheduled for marketing.
     * @return A success or error message indicating the result.
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

