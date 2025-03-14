package com.operations.StageOps.model;

import java.time.LocalDate;

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

    // Constructors
    public RevenueTracking() {}
    public RevenueTracking(int revenueId, int roomId, int eventId, int bookingId, double totalRevenue,
                           double ticketSales, double venueHire, LocalDate bookingDate, String status){}


    // Getters and Setters
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

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
