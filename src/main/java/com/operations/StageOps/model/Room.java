package com.operations.StageOps.model;

import java.util.List;

/**
 * Represents a room used for hosting events.
 */
public class Room {
    private int roomId;
    private String roomName;
    private int capacity;
    private String roomType;
    private String location;
    private List<Seating> seatingArrangements;
    private LayoutConfiguration layoutConfiguration;

    /**
     * Default constructor.
     */
    public Room() {
    }

    /**
     * Parameterized constructor to initialize a room.
     *
     * @param roomId              Unique identifier for the room.
     * @param roomName            Name of the room.
     * @param capacity            Maximum capacity of the room.
     * @param roomType            Type of the room (e.g., conference hall, theater).
     * @param location            Location of the room within the venue.
     * @param seatingArrangements List of seating arrangements for the room.
     * @param layoutConfiguration Layout configuration of the room.
     */
    public Room(int roomId, String roomName, int capacity, String roomType, String location,
                List<Seating> seatingArrangements, LayoutConfiguration layoutConfiguration) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.roomType = roomType;
        this.location = location;
        this.seatingArrangements = seatingArrangements;
        this.layoutConfiguration = layoutConfiguration;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Seating> getSeatingArrangements() {
        return seatingArrangements;
    }

    public void setSeatingArrangements(List<Seating> seatingArrangements) {
        this.seatingArrangements = seatingArrangements;
    }

    public LayoutConfiguration getLayoutConfiguration() {
        return layoutConfiguration;
    }

    public void setLayoutConfiguration(LayoutConfiguration layoutConfiguration) {
        this.layoutConfiguration = layoutConfiguration;
    }
}
