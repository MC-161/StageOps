package com.operations.StageOps.model;

/**
 * This class represents a seat in a room. It contains information about the seat's
 * ID, room, seat number, reservation status, accessibility, restriction status,
 * and the section name to which it belongs.
 */
public class Seating {

    private int seatID;
    private int roomID;
    private int seatNumber;
    private boolean isReserved;
    private boolean isAccessible;
    private boolean isRestricted;
    private String sectionName;

    /**
     * Constructs a new Seating object with the given attributes.
     *
     * @param seatID        the unique ID of the seat
     * @param roomID        the ID of the room where the seat is located
     * @param seatNumber    the seat number within the room
     * @param isReserved    whether the seat is reserved
     * @param isAccessible  whether the seat is accessible
     * @param isRestricted  whether the seat is restricted
     * @param sectionName   the name of the section to which the seat belongs
     */
    public Seating(int seatID, int roomID, int seatNumber, boolean isReserved,
                   boolean isAccessible, boolean isRestricted, String sectionName) {
        this.seatID = seatID;
        this.roomID = roomID;
        this.seatNumber = seatNumber;
        this.isReserved = isReserved;
        this.isAccessible = isAccessible;
        this.isRestricted = isRestricted;
        this.sectionName = sectionName;
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
     * Gets the room ID.
     *
     * @return the room ID
     */
    public int getRoomID() {
        return roomID;
    }

    /**
     * Sets the room ID.
     *
     * @param roomID the new room ID
     */
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    /**
     * Gets the seat number.
     *
     * @return the seat number
     */
    public int getSeatNumber() {
        return seatNumber;
    }

    /**
     * Sets the seat number.
     *
     * @param seatNumber the new seat number
     */
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * Checks if the seat is reserved.
     *
     * @return true if the seat is reserved, false otherwise
     */
    public boolean isReserved() {
        return isReserved;
    }

    /**
     * Sets the reservation status of the seat.
     *
     * @param reserved true to reserve the seat, false otherwise
     */
    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    /**
     * Checks if the seat is accessible.
     *
     * @return true if the seat is accessible, false otherwise
     */
    public boolean isAccessible() {
        return isAccessible;
    }

    /**
     * Sets the accessibility status of the seat.
     *
     * @param accessible true if the seat is accessible, false otherwise
     */
    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    /**
     * Checks if the seat is restricted.
     *
     * @return true if the seat is restricted, false otherwise
     */
    public boolean isRestricted() {
        return isRestricted;
    }

    /**
     * Sets the restriction status of the seat.
     *
     * @param restricted true if the seat is restricted, false otherwise
     */
    public void setRestricted(boolean restricted) {
        isRestricted = restricted;
    }

    /**
     * Gets the section name of the seat.
     *
     * @return the section name
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Sets the section name of the seat.
     *
     * @param sectionName the new section name
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * Provides a string representation of the Seating object.
     *
     * @return a string describing the seating object
     */
    @Override
    public String toString() {
        return "Seating{" +
                "seatID=" + seatID +
                ", roomID=" + roomID +
                ", seatNumber=" + seatNumber +
                ", isReserved=" + isReserved +
                ", isAccessible=" + isAccessible +
                ", isRestricted=" + isRestricted +
                ", sectionName='" + sectionName + '\'' +
                '}';
    }
}
