package com.operations.StageOps.model;


import java.util.Date;


/**
 * Represents a booking in the system.
 */
public class Booking {
    private int bookingId;
    private int clientId;
    private int roomId;
    private Date startDate;
    private Date endDate;
    private String status;
    private double totalCost;


    /**
     * Default constructor for Booking.
     **/
    public Booking() {
    }

    /**
     * Parametereized constructure of booking.
     *
     * @param bookingId The unique identifier for the booking.
     * @param clientId The unique identifier for the client.
     * @param roomId The unique identifier for the room.
     * @param startDate The start date of the booking.
     * @param endDate The end date of the booking.
     * @param status The status of the booking (e.g., confirmed, pending, cancelled).
     * @param totalCost The total cost of the booking.
     */
    public Booking(int bookingId, int clientId, int roomId, Date startDate, Date endDate, String status, double totalCost) {
        this.bookingId = bookingId;
        this.clientId = clientId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.totalCost = totalCost;
    }

    /**
     * Gets the booking ID.
     *
     * @return The booking ID.
     */
    public int getBookingId() {
        return bookingId;
    }

    /**
     * Sets the booking ID.
     *
     * @param bookingId The booking ID is set.
     */
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    /**
     * Gets the client ID.
     *
     * @return The client ID
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the client ID.
     *
     * @param clientId The client ID to set.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the room ID.
     *
     * @return The room ID.
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Sets the room ID.
     *
     * @param roomId The room ID to set.
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Gets the start date of the booking.
     *
     * @return The start date.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the booking.
     *
     * @param startDate The start date to set.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the booking.
     *
     * @return The end date.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the booking.
     *
     * @param endDate The end date to set.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the status of the booking.
     *
     * @return The booking status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the booking.
     *
     * @param status The booking status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the total cost of the booking.
     *
     * @return The total cost.
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Sets the total cost of the booking.
     *
     * @param totalCost The total cost to set.
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
// Get all seats
