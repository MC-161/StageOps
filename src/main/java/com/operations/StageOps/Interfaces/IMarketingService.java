package com.operations.StageOps.Interfaces;

import com.operations.StageOps.model.Booking;
import com.operations.StageOps.model.Event;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public interface IMarketingService {
    List<Event> getAllEvents();
    String scheduleForMarketing(Event event);
    String holdSeatsForGroupBooking(int eventId, int groupSize, List<String> seatIds);

    List<Event> getEventsForDateRange(LocalDate startDate, LocalDate endDate);

    List<Booking> getAllBookings();

    List<Booking> getBookingsForRoom(int roomId);

    int saveEvent(Event event);

    Event updateEvent(int eventId, Event updatedEvent);

    int deleteEvent(int eventId);

    boolean isRoomAvailableForTimePeriod(int roomId, LocalDate startDate, LocalDate endDate, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime);
    List<Integer> getAvailableRooms(LocalDate startDate, LocalDate endDate, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime);
}
