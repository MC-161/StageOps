package com.operations.StageOps.model;

import java.util.Date;

/**
 * This class represents a ticket for an event. It contains information about the ticket's
 * ID, associated event, seat, price, and status.
 */
public class Ticket {

    private int ticketID;
    private int eventID;
    private int seatID;
    private double price;
    private String ticketStatus;
    private Date saleDate;

    /**
     * Constructs a new Ticket object with the given attributes.
     *
     * @param ticketID      the unique ID of the ticket
     * @param eventID       the ID of the event associated with the ticket
     * @param seatID        the ID of the seat associated with the ticket
     * @param price         the price of the ticket
     * @param ticketStatus  the current status of the ticket (e.g., "Reserved", "Sold")
     */
    public Ticket(int ticketID, int eventID, int seatID, double price, String ticketStatus, Date saleDate) {
        this.ticketID = ticketID;
        this.eventID = eventID;
        this.seatID = seatID;
        this.price = price;
        this.ticketStatus = ticketStatus;
        this.saleDate = saleDate;
    }

    /**
     * Gets the ticket ID.
     *
     * @return the ticket ID
     */
    public int getTicketID() {
        return ticketID;
    }

    /**
     * Sets the ticket ID.
     *
     * @param ticketID the new ticket ID
     */
    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    /**
     * Gets the event ID.
     *
     * @return the event ID
     */
    public int getEventID() {
        return eventID;
    }

    /**
     * Sets the event ID.
     *
     * @param eventID the new event ID
     */
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    /**
     * Gets the seat ID.
     *
     * @return the seat ID
     */
    public int getSeatID() {
        return seatID;
    }

    /**
     * Sets the seat ID.
     *
     * @param seatID the new seat ID
     */
    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    /**
     * Gets the price of the ticket.
     *
     * @return the price of the ticket
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the ticket.
     *
     * @param price the new price of the ticket
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the ticket status.
     *
     * @return the status of the ticket (e.g., "Reserved", "Sold")
     */
    public String getTicketStatus() {
        return ticketStatus;
    }

    /**
     * Sets the ticket status.
     *
     * @param ticketStatus the new ticket status
     */
    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    /**
     * Provides a string representation of the Ticket object.
     *
     * @return a string describing the ticket object
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketID=" + ticketID +
                ", eventID=" + eventID +
                ", seatID=" + seatID +
                ", price=" + price +
                ", ticketStatus='" + ticketStatus + '\'' +
                '}';
    }
}
