package com.operations.StageOps.model;

import com.operations.StageOps.util.CostCalculator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

/**
 * Represents a booking made by a client for a specific room.
 * This class contains information about the booking, including the booking ID,
 * client ID, room ID, start and end dates, status, and total cost.
 */
public class Booking {

    private int bookingId;
    private int clientId;
    private int roomId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String status;
    private double totalCost;

    /**
     * Default constructor for the Booking class.
     */
    public Booking() {
    }

    /**
     * Parameterized constructor for creating a Booking instance.
     *
     * @param bookingId  The unique ID of the booking
     * @param clientId   The ID of the client making the booking
     * @param roomId     The ID of the room being booked
     * @param startDate  The start date of the booking
     * @param endDate    The end date of the booking
     * @param status     The status of the booking (e.g., confirmed, cancelled)
     * @param totalCost  The total cost of the booking
     */
    public Booking(int bookingId, int clientId, int roomId, LocalDate startDate, LocalDate endDate, String status, double totalCost) {
        this.bookingId = bookingId;
        this.clientId = clientId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.totalCost = totalCost;
    }

    /**
     * Gets the unique ID of the booking.
     *
     * @return The booking ID
     */
    public int getBookingId() {
        return bookingId;
    }

    /**
     * Sets the booking ID.
     *
     * @param bookingId The booking ID to set
     */
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    /**
     * Gets the client ID of the booking.
     *
     * @return The client ID
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the client ID.
     *
     * @param clientId The client ID to set
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the room ID of the booking.
     *
     * @return The room ID
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Sets the room ID.
     *
     * @param roomId The room ID to set
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Gets the start date of the booking.
     *
     * @return The start date of the booking
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the booking.
     *
     * @param startDate The start date to set
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the booking.
     *
     * @return The end date of the booking
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the booking.
     *
     * @param endDate The end date to set
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the status of the booking.
     *
     * @return The status of the booking
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the booking.
     *
     * @param status The status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the total cost of the booking.
     *
     * @return The total cost of the booking
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Sets the total cost of the booking.
     *
     * @param totalCost The total cost to set
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Updates the total cost of the booking based on the price per day.
     * This method calculates the total cost using the start and end dates of the booking
     * and the price per day.
     *
     * @param pricePerDay The price of the room per day
     */
    public void updateTotalCost(double pricePerDay) {
        this.totalCost = CostCalculator.calculateTotalCost(this.startDate, this.endDate, pricePerDay);
    }
}
