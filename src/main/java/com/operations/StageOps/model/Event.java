package com.operations.StageOps.model;

import java.time.ZonedDateTime;
import java.util.Date;

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
    private LayoutConfiguration layoutConfiguration;  // Link to the layout used for the event

    // Constructors, Getters, and Setters
    public Event() {}

    public Event(int eventId, String eventName, Date eventDate, ZonedDateTime startTime, ZonedDateTime endTime, int roomId, int ticketsAvailable, int ticketsSold, String eventType, double totalRevenue, LayoutConfiguration layoutConfiguration) {
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
        this.layoutConfiguration = layoutConfiguration;
    }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public int getTicketsAvailable() { return ticketsAvailable; }
    public void setTicketsAvailable(int ticketsAvailable) { this.ticketsAvailable = ticketsAvailable; }

    public int getTicketsSold() { return ticketsSold; }
    public void setTicketsSold(int ticketsSold) { this.ticketsSold = ticketsSold; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public double getTotalRevenue() { return calculateRevenue(25); }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }

    public LayoutConfiguration getLayoutConfiguration() { return layoutConfiguration; }
    public void setLayoutConfiguration(LayoutConfiguration layoutConfiguration) { this.layoutConfiguration = layoutConfiguration; }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }
    public double calculateRevenue(double ticketPrice) {
        return ticketsSold * ticketPrice;
    }
}
