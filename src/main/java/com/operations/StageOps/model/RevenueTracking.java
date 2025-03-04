package com.operations.StageOps.model;

/**
 * Represents revenue tracking for an event, including ticket sales and venue hire.
 */
public class RevenueTracking {
    private int revenueId;
    private int roomId;
    private int eventId;
    private double totalRevenue;
    private double ticketSales;
    private double venueHire;

    /**
     * Default constructor.
     */
    public RevenueTracking() {
    }

    /**
     * Parameterized constructor to initialize revenue tracking details.
     *
     * @param revenueId   Unique identifier for the revenue record.
     * @param roomId      Room associated with the revenue.
     * @param eventId     Event associated with the revenue.
     * @param totalRevenue Total revenue generated.
     * @param ticketSales Revenue from ticket sales.
     * @param venueHire   Revenue from venue hire.
     */
    public RevenueTracking(int revenueId, int roomId, int eventId, double totalRevenue, double ticketSales, double venueHire) {
        this.revenueId = revenueId;
        this.roomId = roomId;
        this.eventId = eventId;
        this.totalRevenue = totalRevenue;
        this.ticketSales = ticketSales;
        this.venueHire = venueHire;
    }

    public int getRevenueId() {
        return revenueId;
    }

    public void setRevenueId(int revenueId) {
        this.revenueId = revenueId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getTicketSales() {
        return ticketSales;
    }

    public void setTicketSales(double ticketSales) {
        this.ticketSales = ticketSales;
    }

    public double getVenueHire() {
        return venueHire;
    }

    public void setVenueHire(double venueHire) {
        this.venueHire = venueHire;
    }
}
