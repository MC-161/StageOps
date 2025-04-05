package com.operations.StageOps.model;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Represents an event that takes place in a specific room at a scheduled time.
 */
public class Event {
    private int eventId;
    private String eventName;
    private Date eventDate;
    private int roomId;
    private int ticketsAvailable;
    private int ticketsSold;
    private String eventType;
    private double totalRevenue;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private int layoutId;  // Link to the layout used for the event
    private double ticketPrice;
    private double maxDiscount;
    private int clientId; // Assuming this is needed for the event



    /**
     * Default constructor.
     */
    public Event() {}

    /**
     * Constructs an Event with the specified details.
     *
     * @param eventId          The unique identifier for the event.
     * @param eventName        The name of the event.
     * @param eventDate        The date of the event.
     * @param startTime        The start time of the event.
     * @param endTime          The end time of the event.
     * @param roomId           The ID of the room where the event is held.
     * @param ticketsAvailable The number of available tickets for the event.
     * @param ticketsSold      The number of tickets sold for the event.
     * @param eventType        The type of event (e.g., concert, conference).
     * @param layoutId         The layout ID associated with the event.
     */
    public Event(int eventId, String eventName, Date eventDate, ZonedDateTime startTime, ZonedDateTime endTime, int roomId, int ticketsAvailable, int ticketsSold, String eventType, double totalRevenue, int layoutId, double ticketPrice, double maxDiscount, int clientId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomId = roomId;
        this.ticketsAvailable = ticketsAvailable;
        this.ticketsSold = ticketsSold;
        this.eventType = eventType;
        this.totalRevenue = totalRevenue;
        this.layoutId = layoutId;
        this.ticketPrice = ticketPrice;
        this.maxDiscount = maxDiscount;
        this.clientId = clientId;
    }


    /**
     * Gets the event ID.
     *
     * @return The unique identifier of the event.
     */
    public int getEventId() { return eventId; }

    /**
     * Sets the event ID.
     *
     * @param eventId The unique identifier of the event.
     */
    public void setEventId(int eventId) { this.eventId = eventId; }

    /**
     * Gets the event name.
     *
     * @return The name of the event.
     */
    public String getEventName() { return eventName; }

    /**
     * Sets the event name.
     *
     * @param eventName The name of the event.
     */
    public void setEventName(String eventName) { this.eventName = eventName; }



    /**
     * Gets the event date.
     *
     * @return The date of the event.
     */
    public Date getEventDate() { return eventDate; }

    /**
     * Sets the event date.
     *
     * @param eventDate The date of the event.
     */
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }

    /**
     * Gets the room ID where the event is held.
     *
     * @return The room ID.
     */
    public int getRoomId() { return roomId; }

    /**
     * Sets the room ID where the event is held.
     *
     * @param roomId The room ID.
     */
    public void setRoomId(int roomId) { this.roomId = roomId; }


    /**
     * Gets the number of available tickets for the event.
     *
     * @return The number of available tickets.
     */
    public int getTicketsAvailable() { return ticketsAvailable; }

    /**
     * Sets the number of available tickets for the event.
     *
     * @param ticketsAvailable The number of available tickets.
     */
    public void setTicketsAvailable(int ticketsAvailable) { this.ticketsAvailable = ticketsAvailable; }


    /**
     * Gets the number of tickets sold for the event.
     *
     * @return The number of tickets sold.
     */
    public int getTicketsSold() { return ticketsSold; }

    /**
     * Sets the number of tickets sold for the event.
     *
     * @param ticketsSold The number of tickets sold.
     */
    public void setTicketsSold(int ticketsSold) { this.ticketsSold = ticketsSold; }


    /**
     * Gets the type of the event.
     *
     * @return The event type (e.g., concert, conference).
     */
    public String getEventType() { return eventType; }

    /**
     * Sets the type of the event.
     *
     * @param eventType The type of event.
     */
    public void setEventType(String eventType) { this.eventType = eventType; }

    /**
     * Gets the total revenue generated from ticket sales.
     *
     * @return The total revenue.
     */
    public double getTotalRevenue() { return calculateRevenue(getTicketPrice()); }

    /**
     * Sets the total revenue for the event.
     *
     * @param totalRevenue The total revenue generated.
     */
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }


    /**
     * Gets the layout ID associated with the event.
     *
     * @return The layout ID.
     */
    public int getLayoutId() { return layoutId; }

    /**
     * Sets the layout ID for the event.
     *
     * @param layoutId The layout ID.
     */
    public void setLayoutId(int layoutId) { this.layoutId = layoutId; }


    /**
     * Gets the start time of the event.
     *
     * @return The start time as a {@link ZonedDateTime}.
     */
    public ZonedDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the event.
     *
     * @param endTime The end time as a {@link ZonedDateTime}.
     */
    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the end time of the event.
     *
     * @return The end time as a {@link ZonedDateTime}.
     */
    public ZonedDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the event.
     *
     * @param startTime The start time as a {@link ZonedDateTime}.
     */
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Calculates the total revenue based on the number of tickets sold and a given ticket price.
     *
     * @param ticketPrice The price of a single ticket.
     * @return The total revenue generated from ticket sales.
     */
    public double calculateRevenue(double ticketPrice) {
        return ticketsSold * ticketPrice;
    }

    /**
     * Gets the price of a single ticket for the event.
     *
     * @return The price of a single ticket, represented as a {@code double}.
     */
    public double getTicketPrice() {
        return ticketPrice;
    }

    /**
     * Sets the price of a single ticket for the event.
     *
     * @param ticketPrice The price of a single ticket, represented as a {@code double}.
     *                    This value should be a positive number.
     */
    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    /**
     * Gets the maximum discount allowed on the ticket price for the event.
     *
     * @return The maximum discount as a percentage, represented as a {@code double}.
     *         For example, a discount of 20% would be returned as {@code 20.0}.
     */
    public double getMaxDiscount() {
        return maxDiscount;
    }

    /**
     * Sets the maximum discount allowed on the ticket price for the event.
     *
     * @param maxDiscount The maximum discount as a percentage, represented as a {@code double}.
     *                    This value should be between 0 and 100 (inclusive), where 100 represents
     *                    a full discount.
     */
    public void setMaxDiscount(double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
