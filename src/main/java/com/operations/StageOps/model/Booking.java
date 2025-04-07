package com.operations.StageOps.model;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Represents a booking entity containing details about a reservation,
 * including client information, booking times, status, cost, and room assignments.
 */
public class Booking {
    private int bookingId;
    private int clientId;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private String status;
    private double totalCost;
    private List<BookingRoomAssignment> roomAssignments;



    /**
     * Constructs a Booking instance with all necessary details.
     *
     * @param bookingId        The unique identifier for the booking.
     * @param clientId         The unique identifier for the client making the booking.
     * @param startTime        The start time of the booking.
     * @param endTime          The end time of the booking.
     * @param status           The current status of the booking (e.g., "confirmed", "cancelled").
     * @param totalCost        The total cost associated with the booking.
     * @param roomAssignments  The list of room assignments associated with the booking.
     */// Constructor that matches the parameters used in the JDBC query
    public Booking(int bookingId, int clientId, ZonedDateTime startTime, ZonedDateTime endTime, String status, double totalCost, List<BookingRoomAssignment> roomAssignments) {
        this.bookingId = bookingId;
        this.clientId = clientId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.totalCost = totalCost;
        this.roomAssignments = roomAssignments;
    }

    public Booking() {

    }

    /**
     * Gets the booking ID.
     *
     * @return The unique identifier of the booking.
     */
    public int getBookingId() {
        return bookingId;
    }

    /**
     * Sets the booking ID.
     *
     * @param bookingId The unique identifier of the booking.
     */
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    /**
     * Gets the client ID.
     *
     * @return The unique identifier of the client making the booking.
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the client ID.
     *
     * @param clientId The unique identifier of the client making the booking.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }


    /**
     * Gets the start time of the booking.
     *
     * @return The start time of the booking.
     */
    public ZonedDateTime getStartTime() {
        return startTime;
    }


    /**
     * Sets the start time of the booking.
     *
     * @param startTime The start time of the booking.
     */
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }


    /**
     * Gets the end time of the booking.
     *
     * @return The end time of the booking.
     */
    public ZonedDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the booking.
     *
     * @param endTime The end time of the booking.
     */
    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the status of the booking.
     *
     * @return The status of the booking.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the booking.
     *
     * @param status The status of the booking.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the total cost of the booking.
     *
     * @return The total cost of the booking.
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Sets the total cost of the booking.
     *
     * @param totalCost The total cost of the booking.
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Gets the list of room assignments associated with the booking.
     *
     * @return The list of room assignments associated with the booking.
     */
    public List<BookingRoomAssignment> getRoomAssignments() {
        return roomAssignments;
    }

    /**
     * Sets the list of room assignments associated with the booking.
     *
     * @param roomAssignments The list of room assignments associated with the booking.
     */
    public void setRoomAssignments(List<BookingRoomAssignment> roomAssignments) {
        this.roomAssignments = roomAssignments;
    }
}
