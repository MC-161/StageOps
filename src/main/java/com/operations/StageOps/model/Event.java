package com.operations.StageOps.model;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Represents an event in the system.
 */
public class Event {
    private int eventId;
    private String eventName;
    private Date eventDate;
    private int roomId;
    private int ticketsSold;
    private int ticketsAvailable;
    private String eventType;
    private double totalRevenue;
    private String layoutConfiguration;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    /**
     * Default constructor.
     */
    public Event() {
    }

    /**
     * Parameterized constructor to initialize an event.
     *
     * @param eventId            Unique identifier for the event.
     * @param eventName          Name of the event.
     * @param eventDate          Date of the event.
     * @param roomId             Room where the event is held.
     * @param ticketsSold        Number of tickets sold.
     * @param ticketsAvailable   Number of tickets available.
     * @param eventType          Type of the event.
     * @param totalRevenue       Total revenue generated from ticket sales.
     * @param layoutConfiguration Layout configuration of the venue.
     * @param startTime          Event start time with time zone.
     * @param endTime            Event end time with time zone.
     */
    public Event(int eventId, String eventName, Date eventDate, int roomId, int ticketsSold, int ticketsAvailable,
                 String eventType, double totalRevenue, String layoutConfiguration, ZonedDateTime startTime, ZonedDateTime endTime) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.roomId = roomId;
        this.ticketsSold = ticketsSold;
        this.ticketsAvailable = ticketsAvailable;
        this.eventType = eventType;
        this.totalRevenue = totalRevenue;
        this.layoutConfiguration = layoutConfiguration;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(int ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public void setTicketsAvailable(int ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public double getTotalRevenue() { return calculateRevenue(25); }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getLayoutConfiguration() {
        return layoutConfiguration;
    }

    public void setLayoutConfiguration(String layoutConfiguration) {
        this.layoutConfiguration = layoutConfiguration;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public double calculateRevenue(double ticketPrice) {
        return ticketsSold * ticketPrice;
    }
}