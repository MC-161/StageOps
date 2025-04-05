package com.operations.StageOps.model;

import java.util.List;

/**
 * Represents a room available for events, including its capacity, type, location,
 * associated layouts, and pricing details.
 */
public class Room {
    private int roomId;
    private String roomName;
    private int capacity;
    private String roomType;
    private String location;

    private List<Integer> layoutIds;  // Link to the selected layout for the room
    private int currentLayoutId;  // Link to the current layout for the room

    // Add rates for the room
    private double hourlyRate;   // Hourly rate
    private double eveningRate;  // Evening rate
    private double dailyRate;    // Daily rate
    private double weeklyRate;   // Weekly rate

    /**
     * Default constructor.
     */
    public Room() {}

    /**
     * Constructs a new Room with all attributes.
     *
     * @param roomId          The unique ID of the room.
     * @param roomName        The name of the room.
     * @param capacity        The maximum capacity of the room.
     * @param roomType        The type of room (e.g., "Conference Room", "Banquet Hall").
     * @param location        The location of the room.
     * @param layoutIds       List of layout IDs associated with the room.
     * @param currentLayoutId The currently selected layout ID.
     * @param hourlyRate      The hourly rental rate.
     * @param eveningRate     The evening rental rate.
     * @param dailyRate       The daily rental rate.
     * @param weeklyRate      The weekly rental rate.
     */
    public Room(int roomId, String roomName, int capacity, String roomType, String location,
                List<Integer> layoutIds, int currentLayoutId, double hourlyRate, double eveningRate,
                double dailyRate, double weeklyRate) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.roomType = roomType;
        this.location = location;
        this.layoutIds = layoutIds;
        this.currentLayoutId = currentLayoutId;
        this.hourlyRate = hourlyRate;
        this.eveningRate = eveningRate;
        this.dailyRate = dailyRate;
        this.weeklyRate = weeklyRate;
    }

    /**
     * Gets the unique ID of the room.
     *
     * @return The room ID.
     */
    public int getRoomId() { return roomId; }

    /**
     * Sets the unique ID of the room.
     *
     * @param roomId The room ID.
     */
    public void setRoomId(int roomId) { this.roomId = roomId; }

    /**
     * Gets the name of the room.
     *
     * @return The room name.
     */
    public String getRoomName() { return roomName; }

    /**
     * Sets the name of the room.
     *
     * @param roomName The room name.
     */
    public void setRoomName(String roomName) { this.roomName = roomName; }

    /**
     * Gets the maximum capacity of the room.
     *
     * @return The room capacity.
     */
    public int getCapacity() { return capacity; }

    /**
     * Sets the maximum capacity of the room.
     *
     * @param capacity The room capacity.
     */
    public void setCapacity(int capacity) { this.capacity = capacity; }

    /**
     * Gets the type of room.
     *
     * @return The room type.
     */
    public String getRoomType() { return roomType; }

    /**
     * Sets the type of room.
     *
     * @param roomType The room type.
     */
    public void setRoomType(String roomType) { this.roomType = roomType; }

    /**
     * Gets the location of the room.
     *
     * @return The room location.
     */
    public String getLocation() { return location; }

    /**
     * Sets the location of the room.
     *
     * @param location The room location.
     */
    public void setLocation(String location) { this.location = location; }

    /**
     * Gets the list of layout IDs associated with the room.
     *
     * @return The layout IDs.
     */
    public List<Integer> getLayoutIds() { return layoutIds; }

    /**
     * Sets the list of layout IDs associated with the room.
     *
     * @param layoutIds The layout IDs.
     */
    public void setLayoutIds(List<Integer> layoutIds) { this.layoutIds = layoutIds; }

    /**
     * Gets the currently selected layout ID.
     *
     * @return The current layout ID.
     */
    public int getCurrentLayoutId() { return currentLayoutId; }

    /**
     * Sets the currently selected layout ID.
     *
     * @param currentLayoutId The current layout ID.
     */
    public void setCurrentLayoutId(int currentLayoutId) { this.currentLayoutId = currentLayoutId; }

    /**
     * Gets the hourly rental rate.
     *
     * @return The hourly rate.
     */
    public double getHourlyRate() { return hourlyRate; }

    /**
     * Sets the hourly rental rate.
     *
     * @param hourlyRate The hourly rate.
     */
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    /**
     * Gets the evening rental rate.
     *
     * @return The evening rate.
     */
    public double getEveningRate() { return eveningRate; }

    /**
     * Sets the evening rental rate.
     *
     * @param eveningRate The evening rate.
     */
    public void setEveningRate(double eveningRate) { this.eveningRate = eveningRate; }

    /**
     * Gets the daily rental rate.
     *
     * @return The daily rate.
     */
    public double getDailyRate() { return dailyRate; }

    /**
     * Sets the daily rental rate.
     *
     * @param dailyRate The daily rate.
     */
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }

    /**
     * Gets the weekly rental rate.
     *
     * @return The weekly rate.
     */
    public double getWeeklyRate() { return weeklyRate; }

    /**
     * Sets the weekly rental rate.
     *
     * @param weeklyRate The weekly rate.
     */
    public void setWeeklyRate(double weeklyRate) { this.weeklyRate = weeklyRate; }

}
