package com.operations.StageOps.model;

import java.util.Date;

/**
 * Represents a ticket purchased for an event. The class includes details like
 * the ticket ID, associated event ID, seat ID, price, ticket status, and sale date.
 */
public class Ticket {

    private int ticketId;
    private int eventId;
    private String seatId;
    private double price;
    private String ticketStatus;
    private Date saleDate;

    /**
     * Default constructor for creating an empty instance of the Ticket class.
     * Initializes all attributes to their default values.
     */
    public Ticket() {}

    /**
     * Constructor for creating a new instance of the Ticket class with specific values.
     *
     * @param ticketId The unique identifier for the ticket.
     * @param eventId The ID of the event for which this ticket is purchased.
     * @param seatId The seat ID associated with the ticket.
     * @param price The price of the ticket.
     * @param ticketStatus The current status of the ticket (e.g., "Sold", "Reserved").
     * @param saleDate The date when the ticket was sold.
     */
    public Ticket(int ticketId, int eventId, String seatId, double price, String ticketStatus, Date saleDate) {
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.seatId = seatId;
        this.price = price;
        this.ticketStatus = ticketStatus;
        this.saleDate = saleDate;
    }

    /**
     * Gets the unique identifier of the ticket.
     *
     * @return The ticket ID.
     */
    public int getTicketId() {
        return ticketId;
    }

    /**
     * Sets the unique identifier for the ticket.
     *
     * @param ticketId The ticket ID to be set.
     */
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * Gets the event ID associated with this ticket.
     *
     * @return The event ID.
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Sets the event ID for the ticket.
     *
     * @param eventId The event ID to be set.
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the seat ID for the ticket.
     *
     * @return The seat ID.
     */
    public String getSeatId() {
        return seatId;
    }

    /**
     * Sets the seat ID for the ticket.
     *
     * @param seatId The seat ID to be set.
     */
    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    /**
     * Gets the price of the ticket.
     *
     * @return The ticket price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the ticket.
     *
     * @param price The price to be set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the current status of the ticket.
     * This could represent whether the ticket is sold, reserved, etc.
     *
     * @return The ticket status.
     */
    public String getTicketStatus() {
        return ticketStatus;
    }

    /**
     * Sets the status of the ticket.
     *
     * @param ticketStatus The ticket status to be set (e.g., "Sold", "Reserved").
     */
    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    /**
     * Gets the sale date of the ticket.
     *
     * @return The date the ticket was sold.
     */
    public Date getSaleDate() {
        return saleDate;
    }

    /**
     * Sets the sale date of the ticket.
     *
     * @param saleDate The date to be set as the sale date.
     */
    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }
}
