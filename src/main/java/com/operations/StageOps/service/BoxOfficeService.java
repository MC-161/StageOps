package com.operations.StageOps.service;

import com.operations.StageOps.Interfaces.IBoxOfficeService;
import com.operations.StageOps.model.*;
import com.operations.StageOps.repository.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;


@Service
public class BoxOfficeService implements IBoxOfficeService {
    private final SeatingRepository seatingRepository;
    private JdbcTemplate jdbcTemplate;
    private EventRepository eventRepository;
    private TicketRepository ticketRepository;
    private LayoutRepository layoutRepository;
    private BookingRepository bookingRepository;
    private ContractRepository contractRepository;
    private RoomRepository roomRepository;

    public BoxOfficeService(TicketRepository ticketRepository, EventRepository eventRepository, SeatingRepository seatingRepository, JdbcTemplate jdbcTemplate, BookingRepository bookingRepository, LayoutRepository layoutRepository, ContractRepository contractRepository, RoomRepository roomRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
        this.layoutRepository = layoutRepository;
        this.seatingRepository = seatingRepository;
        this.contractRepository = contractRepository;
        this.roomRepository = roomRepository;
    }

    // Implement the methods from IBoxOfficeService interface here

    @Override
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


    @Override
    public Ticket getTicketById(int ticketId) {
        return ticketRepository.getTicketById(ticketId);
    }


    @Override
    public int getTotalTicketsSoldForWeek(LocalDate startOfWeek, LocalDate endOfWeek) {
        return ticketRepository.countBySaleDateBetween(startOfWeek, endOfWeek);
    }

    /**
     * Fetch all layouts associated with a specific room.
     *
     * @param roomId The ID of the room for which layouts are to be fetched.
     * @return A list of layout configurations for the specified room.
     */
    @Override
    public List<LayoutConfiguration> getLayoutsForRoom(int roomId) {
        return layoutRepository.findLayoutsByRoomId(roomId);
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
     * Retrieve all available seats for a specific event.
     *
     * @param eventId The ID of the event for which seats are to be retrieved.
     * @return A list of available seats for the specified event.
     */
    @Override
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
    @Override
    public List<Seating> getSeatsForEvent(int eventId) {
        return eventRepository.getSeatsForEvent(eventId);
    }

    /**
     * Check if a room is available for a specific time period.
     *
     * @param roomId The ID of the room to check.
     * @param startDate The start date of the time period.
     * @param endDate The end date of the time period.
     * @param eventStartTime The start time of the event.
     * @param eventEndTime The end time of the event.
     * @return True if the room is available, false otherwise.
     */
    public boolean isRoomAvailableForTimePeriod(int roomId, LocalDate startDate, LocalDate endDate, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime) {
        return roomRepository.isRoomAvailableForTimePeriod(roomId, startDate, endDate, eventStartTime, eventEndTime);
    }

    /**
     * Get a list of available rooms for a specific time period.
     *
     * @param startDate The start date of the time period.
     * @param endDate The end date of the time period.
     * @param eventStartTime The start time of the event.
     * @param eventEndTime The end time of the event.
     * @return A list of available room IDs.
     */
    public List<Integer> getAvailableRooms(LocalDate startDate, LocalDate endDate, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime) {
        return roomRepository.getAvailableRooms(startDate, endDate, eventStartTime, eventEndTime);
    }

}
