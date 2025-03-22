package com.operations.StageOps.model;

public class Seating {
    private String seatId;
    private int roomId;
    private int seatNumber;
    private boolean isAccessible;
    private boolean isRestricted;
    private String sectionName;
    private String status;

    // Constructors, Getters, and Setters
    public Seating() {}

    public Seating(String seatId, int roomId, int seatNumber,boolean isAccessible, boolean isRestricted, String sectionName) {
        this.seatId = seatId;
        this.roomId = roomId;
        this.seatNumber = seatNumber;
        this.isAccessible = isAccessible;
        this.isRestricted = isRestricted;
        this.sectionName = sectionName;
        this.status = "available";
    }
    // Method to add an event ID to the list

    public String getSeatId() { return seatId; }
    public void setSeatId(String seatId) { this.seatId = seatId; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }


    public boolean isAccessible() { return isAccessible; }
    public void setAccessible(boolean accessible) { isAccessible = accessible; }

    public boolean isRestricted() { return isRestricted; }
    public void setRestricted(boolean restricted) { isRestricted = restricted; }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setId(int seatId) {
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
