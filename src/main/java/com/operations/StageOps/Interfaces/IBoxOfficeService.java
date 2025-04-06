package com.operations.StageOps.Interfaces;


import com.operations.StageOps.model.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public interface IBoxOfficeService  {

    // Tickets
    void saveTickets(List<Ticket> tickets);
    Ticket getTicketById(int ticketId);
    int getTotalTicketsSoldForWeek(LocalDate startOfWeek, LocalDate endOfWeek);

    // Layouts
    List<LayoutConfiguration> getLayoutsForRoom(int roomId);

    // Events
    List<Event> getAllEvents();
    List<Event> getEventsForDateRange(LocalDate startDate, LocalDate endDate);
    int saveEvent(Event event);
    Event updateEvent(int eventId, Event updatedEvent);
    int deleteEvent(int eventId);

    // Bookings
    List<Booking> getAllBookings();
    List<Booking> getBookingsForRoom(int roomId);

    // Seating
    List<Seating> getAvailableSeats(int eventId);
    List<Seating> getSeatsForEvent(int eventId);

    boolean isRoomAvailableForTimePeriod(int roomId, LocalDate startDate, LocalDate endDate, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime);
    List<Integer> getAvailableRooms(LocalDate startDate, LocalDate endDate, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime);
}
