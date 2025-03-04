package com.operations.StageOps.model;

/**
 * Represents a seat in a room, containing information about its unique ID, seating position,
 * and status such as whether the seat is reserved, accessible, or restricted.
 */
public class Seating {

    private String seatId;
    private int roomId;
    private int seatNumber;
    private boolean isReserved;
    private boolean isAccessible;
    private boolean isRestricted;
    private String sectionName;
    private String status;

    /**
     * Default constructor for creating an empty instance of the Seating class.
     * It initializes the object without setting any values.
     */
    public Seating() {}

    /**
     * Constructor for creating a new instance of the Seating class with specific values.
     *
     * @param seatId The unique identifier of the seat.
     * @param roomId The room ID where the seat is located.
     * @param seatNumber The seat number within the room.
     * @param isReserved Indicates whether the seat is reserved (true if reserved).
     * @param isAccessible Indicates whether the seat is accessible (e.g., for disabled users).
     * @param isRestricted Indicates whether the seat is restricted (e.g., VIP seating).
     * @param sectionName The name of the section where the seat is located.
     */
    public Seating(String seatId, int roomId, int seatNumber, boolean isReserved, boolean isAccessible, boolean isRestricted, String sectionName) {
        this.seatId = seatId;
        this.roomId = roomId;
        this.seatNumber = seatNumber;
        this.isReserved = isReserved;
        this.isAccessible = isAccessible;
        this.isRestricted = isRestricted;
    }

    /**
     * Gets the unique identifier of the seat.
     *
     * @return The seat ID.
     */
    public String getSeatId() {
        return seatId;
    }

    /**
     * Sets the unique identifier of the seat.
     *
     * @param seatId The seat ID to be set.
     */
    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    /**
     * Gets the room ID where the seat is located.
     *
     * @return The room ID of the seat.
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Sets the room ID where the seat is located.
     *
     * @param roomId The room ID to be set.
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Gets the seat number within the room.
     *
     * @return The seat number.
     */
    public int getSeatNumber() {
        return seatNumber;
    }

    /**
     * Sets the seat number within the room.
     *
     * @param seatNumber The seat number to be set.
     */
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * Indicates whether the seat is reserved.
     *
     * @return true if the seat is reserved; false otherwise.
     */
    public boolean isReserved() {
        return isReserved;
    }

    /**
     * Sets whether the seat is reserved.
     *
     * @param reserved true if the seat is reserved; false otherwise.
     */
    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    /**
     * Indicates whether the seat is accessible for disabled users.
     *
     * @return true if the seat is accessible; false otherwise.
     */
    public boolean isAccessible() {
        return isAccessible;
    }

    /**
     * Sets whether the seat is accessible for disabled users.
     *
     * @param accessible true if the seat is accessible; false otherwise.
     */
    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    /**
     * Indicates whether the seat is restricted, such as VIP seating.
     *
     * @return true if the seat is restricted; false otherwise.
     */
    public boolean isRestricted() {
        return isRestricted;
    }

    /**
     * Sets whether the seat is restricted, such as VIP seating.
     *
     * @param restricted true if the seat is restricted; false otherwise.
     */
    public void setRestricted(boolean restricted) {
        isRestricted = restricted;
    }

    /**
     * Gets the name of the section where the seat is located.
     *
     * @return The section name.
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Sets the name of the section where the seat is located.
     *
     * @param sectionName The section name to be set.
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * Sets the status of the seat.
     *
     * @param status The status to be set for the seat (e.g., "available", "booked").
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the status of the seat (e.g., "available", "booked").
     *
     * @return The current status of the seat.
     */
    public String getStatus() {
        return status;
    }
}
