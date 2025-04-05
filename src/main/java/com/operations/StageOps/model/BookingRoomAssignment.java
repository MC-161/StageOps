package com.operations.StageOps.model;

import java.time.ZonedDateTime;

/**
 * Represents the assignment of a room to a booking, including the booking ID,
 * assigned date and time, and the room ID.
 */
public class BookingRoomAssignment {
    private int bookingId;
    private ZonedDateTime dateTime;  // Changed to ZonedDateTime
    private int roomId;

    /**
     * Default constructor for BookingRoomAssignment.
     */
    public BookingRoomAssignment() {
    }

    /**
     * Constructs a BookingRoomAssignment with the specified booking ID, date/time, and room ID.
     *
     * @param bookingId The unique identifier for the booking.
     * @param dateTime  The date and time the room is assigned.
     * @param roomId    The unique identifier for the assigned room.
     */
    public BookingRoomAssignment(int bookingId, ZonedDateTime dateTime, int roomId) {
        this.bookingId = bookingId;
        this.dateTime = dateTime;
        this.roomId = roomId;
    }

    /**
     * Gets the booking ID associated with this room assignment.
     *
     * @return The booking ID.
     */
    public int getBookingId() {
        return bookingId;
    }

    /**
     * Sets the booking ID for this room assignment.
     *
     * @param bookingId The booking ID to be set.
     */
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    /**
     * Gets the date and time the room is assigned.
     *
     * @return The date and time of the room assignment.
     */
    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Sets the date and time the room is assigned.
     *
     * @param dateTime The date and time of the room assignment.
     */
    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Gets the room ID associated with this room assignment.
     *
     * @return The room ID.
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Sets the room ID for this room assignment.
     *
     * @param roomId The room ID to be set.
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
