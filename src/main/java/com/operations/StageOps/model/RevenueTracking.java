package com.operations.StageOps.model;

import java.time.LocalDate;

/**
 * Represents the revenue tracking for an event or booking.
 * This includes revenue from ticket sales, venue hire, and overall financial tracking.
 */
public class RevenueTracking {
    private int revenueId;
    private int roomId;
    private int eventId;
    private int bookingId;
    private double totalRevenue;
    private double ticketSales;
    private double venueHire;
    private LocalDate bookingDate;
    private String status;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    /**
     * Default constructor.
     */
    public RevenueTracking() {}

    /**
     * Constructor to initialize revenue tracking details.
     *
     * @param revenueId    Unique identifier for the revenue record.
     * @param roomId       ID of the room where the event/booking occurred.
     * @param eventId      ID of the associated event.
     * @param bookingId    ID of the associated booking.
     * @param totalRevenue Total revenue generated.
     * @param ticketSales  Revenue from ticket sales.
     * @param venueHire    Revenue from venue hire.
     * @param bookingDate  Date when the booking was made.
     * @param status       Current status of the revenue record (e.g., "Confirmed", "Pending").
     */
    public RevenueTracking(int revenueId, int roomId, int eventId, int bookingId, double totalRevenue,
                           double ticketSales, double venueHire, LocalDate bookingDate, String status){}

    /**
     * Gets the revenue ID.
     *
     * @return The unique identifier for this revenue record.
     */
    public int getRevenueId() {
        return revenueId;
    }

    /**
     * Sets the revenue ID.
     *
     * @param revenueId The unique identifier for this revenue record.
     */
    public void setRevenueId(int revenueId) {
        this.revenueId = revenueId;
    }

    /**
     * Gets the room ID.
     *
     * @return The ID of the room where the event/booking occurred.
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Sets the room ID.
     *
     * @param roomId The ID of the room where the event/booking occurred.
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Gets the event ID.
     *
     * @return The ID of the associated event.
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Sets the event ID.
     *
     * @param eventId The ID of the associated event.
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the total revenue.
     *
     * @return The total revenue generated.
     */
    public double getTotalRevenue() {
        return totalRevenue;
    }

    /**
     * Sets the total revenue.
     *
     * @param totalRevenue The total revenue generated.
     */
    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    /**
     * Gets the revenue from ticket sales.
     *
     * @return The revenue from ticket sales.
     */
    public double getTicketSales() {
        return ticketSales;
    }

    /**
     * Sets the revenue from ticket sales.
     *
     * @param ticketSales The revenue from ticket sales.
     */
    public void setTicketSales(double ticketSales) {
        this.ticketSales = ticketSales;
    }

    /**
     * Gets the revenue from venue hire.
     *
     * @return The revenue from venue hire.
     */
    public double getVenueHire() {
        return venueHire;
    }

    /**
     * Sets the revenue from venue hire.
     *
     * @param venueHire The revenue from venue hire.
     */
    public void setVenueHire(double venueHire) {
        this.venueHire = venueHire;
    }

    /**
     * Gets the booking ID.
     *
     * @return The ID of the associated booking.
     */
    public int getBookingId() {
        return bookingId;
    }

    /**
     * Sets the booking ID.
     *
     * @param bookingId The ID of the associated booking.
     */
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    /**
     * Gets the booking date.
     *
     * @return The date when the booking was made.
     */
    public LocalDate getBookingDate() {
        return bookingDate;
    }

    /**
     * Sets the booking date.
     *
     * @param bookingDate The date when the booking was made.
     */
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * Gets the status.
     *
     * @return The current status of the revenue record (e.g., "Confirmed", "Pending").
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status The current status of the revenue record (e.g., "Confirmed", "Pending").
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the date and time when the revenue record was created.
     *
     * @return The date and time when the revenue record was created.
     */
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the date and time when the revenue record was created.
     *
     * @param createdAt The date and time when the revenue record was created.
     */
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the date and time when the revenue record was last updated.
     *
     * @return The date and time when the revenue record was last updated.
     */
    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the date and time when the revenue record was last updated.
     *
     * @param updatedAt The date and time when the revenue record was last updated.
     */
    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
