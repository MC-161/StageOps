package com.operations.StageOps.model;

public class Room {
    private int roomId;
    private String roomName;
    private int capacity;
    private String roomType;
    private String location;

    private LayoutConfiguration layoutConfiguration;  // Link to the selected layout for the room

    // Constructors, Getters, and Setters
    public Room() {}

    public Room(int roomId, String roomName, int capacity, String roomType, String location, LayoutConfiguration layoutConfiguration) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.roomType = roomType;
        this.location = location;
        this.layoutConfiguration = layoutConfiguration;
    }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LayoutConfiguration getLayoutConfiguration() { return layoutConfiguration; }
    public void setLayoutConfiguration(LayoutConfiguration layoutConfiguration) { this.layoutConfiguration = layoutConfiguration; }
}
