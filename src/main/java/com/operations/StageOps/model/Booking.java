package com.operations.StageOps.model;


import java.util.Date;

public class Booking {
    private int bookingId;
    private int clientId;
    private int roomId;
    private Date startDate;
    private Date endDate;
    private String status;
    private double totalCost;

    public Booking() {
    }
    public Booking(int bookingId, int clientId, int roomId, Date startDate, Date endDate, String status, double totalCost) {
        this.bookingId = bookingId;
        this.clientId = clientId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.totalCost = totalCost;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
    // Get all seats
