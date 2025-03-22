package com.operations.StageOps.model;

import java.util.Date;

public class Ticket {
    private int ticketId;
    private int eventId;
    private String seatId;
    private double price;
    private String ticketStatus;
    private Date saleDate;

    // Constructors, Getters, and Setters
    public Ticket() {}

    public Ticket(int ticketId, int eventId, String seatId, double price, String ticketStatus, Date saleDate) {
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.seatId = seatId;
        this.price = price;
        this.ticketStatus = ticketStatus;
        this.saleDate = saleDate;
    }

    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getSeatId() { return seatId; }
    public void setSeatId(String seatId) { this.seatId = seatId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getTicketStatus() { return ticketStatus; }
    public void setTicketStatus(String ticketStatus) { this.ticketStatus = ticketStatus; }

    public Date getSaleDate() { return saleDate; }
    public void setSaleDate(Date saleDate) { this.saleDate = saleDate; }
}
