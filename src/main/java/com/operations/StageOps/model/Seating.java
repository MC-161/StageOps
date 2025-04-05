package com.operations.StageOps.model;

/**
 * Represents a seating arrangement within a room, including seat attributes such as
 * accessibility, restrictions, section, and availability status.
 */
public class Seating {
    private String seatId;
    private int roomId;
    private int seatNumber;
    private boolean isAccessible;
    private boolean isRestricted;
    private String sectionName;
    private String status;

    /**
     * Default constructor.
     */
    public Seating() {}

    /**
     * Constructs a new Seating instance with specified attributes.
     *
     * @param seatId       The unique identifier of the seat.
     * @param roomId       The ID of the room where the seat is located.
     * @param seatNumber   The seat number within the room.
     * @param isAccessible Indicates whether the seat is accessible for disabled individuals.
     * @param isRestricted Indicates whether the seat has any restrictions (e.g., VIP-only).
     * @param sectionName  The section where the seat is located (e.g., "Front Row", "Balcony").
     */
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

    /**
     * Gets the unique seat ID.
     *
     * @return The seat ID.
     */
    public String getSeatId() { return seatId; }

    /**
     * Sets the unique seat ID.
     *
     * @param seatId The seat ID.
     */
    public void setSeatId(String seatId) { this.seatId = seatId; }

    /**
     * Gets the room ID where the seat is located.
     *
     * @return The room ID.
     */
    public int getRoomId() { return roomId; }

    /**
     * Sets the room ID where the seat is located.
     *
     * @param roomId The room ID.
     */
    public void setRoomId(int roomId) { this.roomId = roomId; }

    /**
     * Gets the seat number within the room.
     *
     * @return The seat number.
     */
    public int getSeatNumber() { return seatNumber; }

    /**
     * Sets the seat number within the room.
     *
     * @param seatNumber The seat number.
     */
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }


    /**
     * Indicates whether the seat is accessible for disabled individuals.
     *
     * @return True if the seat is accessible; false otherwise.
     */
    public boolean isAccessible() { return isAccessible; }

    /**
     * Sets whether the seat is accessible for disabled individuals.
     *
     * @param accessible True if the seat is accessible; false otherwise.
     */
    public void setAccessible(boolean accessible) { isAccessible = accessible; }

    /**
     * Indicates whether the seat has any restrictions (e.g., VIP-only).
     *
     * @return True if the seat is restricted; false otherwise.
     */
    public boolean isRestricted() { return isRestricted; }

    /**
     * Sets whether the seat has any restrictions (e.g., VIP-only).
     *
     * @param restricted True if the seat is restricted; false otherwise.
     */
    public void setRestricted(boolean restricted) { isRestricted = restricted; }

    /**
     * Gets the section where the seat is located (e.g., "Front Row", "Balcony").
     *
     * @return The section name.
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Sets the section where the seat is located (e.g., "Front Row", "Balcony").
     *
     * @param sectionName The section name.
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * Gets the availability status of the seat.
     *
     * @return The status of the seat.
     */
    public void setId(int seatId) {
    }

    /**
     * Sets the availability status of the seat.
     *
     * @param status The status of the seat.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the availability status of the seat.
     *
     * @return The status of the seat.
     */
    public String getStatus() {
        return status;
    }
}
