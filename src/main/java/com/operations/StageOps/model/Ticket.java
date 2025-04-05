package com.operations.StageOps.model;

import java.util.Date;

/**
 * Represents a ticket for an event, including details about the assigned seat,
 * price, status, and sale date.
 */
public class Ticket {
    private int ticketId;
    private int eventId;
    private String seatId;
    private double price;
    private String ticketStatus;
    private Date saleDate;

    /**
     * Default constructor.
     */
    public Ticket() {}

    /**
     * Constructs a ticket with specified details.
     *
     * @param ticketId     The unique ID of the ticket.
     * @param eventId      The event ID associated with the ticket.
     * @param seatId       The seat ID assigned to the ticket.
     * @param price        The price of the ticket.
     * @param ticketStatus The status of the ticket (e.g., "Sold", "Available").
     * @param saleDate     The date the ticket was sold.
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
     * Gets the ticket ID.
     *
     * @return The ticket ID.
     */
    public int getTicketId() { return ticketId; }

    /**
     * Sets the ticket ID.
     *
     * @param ticketId The ticket ID.
     */
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    /**
     * Gets the event ID.
     *
     * @return The event ID.
     */
    public int getEventId() { return eventId; }

    /**
     * Sets the event ID.
     *
     * @param eventId The event ID.
     */
    public void setEventId(int eventId) { this.eventId = eventId; }

    /**
     * Gets the seat ID.
     *
     * @return The seat ID.
     */
    public String getSeatId() { return seatId; }

    /**
     * Sets the seat ID.
     *
     * @param seatId The seat ID.
     */
    public void setSeatId(String seatId) { this.seatId = seatId; }

    /**
     * Gets the price of the ticket.
     *
     * @return The price of the ticket.
     */
    public double getPrice() { return price; }

    /**
     * Sets the price of the ticket.
     *
     * @param price The price of the ticket.
     */
    public void setPrice(double price) { this.price = price; }

    /**
     * Gets the status of the ticket.
     *
     * @return The status of the ticket.
     */
    public String getTicketStatus() { return ticketStatus; }

    /**
     * Sets the status of the ticket.
     *
     * @param ticketStatus The status of the ticket.
     */
    public void setTicketStatus(String ticketStatus) { this.ticketStatus = ticketStatus; }

    /**
     * Gets the date the ticket was sold.
     *
     * @return The date the ticket was sold.
     */
    public Date getSaleDate() { return saleDate; }

    /**
     * Sets the date the ticket was sold.
     *
     * @param saleDate The date the ticket was sold.
     */
    public void setSaleDate(Date saleDate) { this.saleDate = saleDate; }
}
