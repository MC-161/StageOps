package com.operations.StageOps.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a layout configuration for a specific room.
 * A layout defines how seating or sections are arranged within a room.
 */
public class LayoutConfiguration {
    private int layoutId;
    private String layoutName;
    private int maxCapacity;
    private int roomId;
    private String layoutType;  // Could be types like "Theater", "Classroom", "Banquet", etc.

    private List<Section> sections;  // This will hold all the sections in the layout

    /**
     * Default constructor.
     */
    public LayoutConfiguration() {}


    /**
     * Constructor to create a layout with a specific ID.
     *
     * @param layoutId    Unique identifier for the layout.
     * @param layoutName  Name of the layout.
     * @param maxCapacity Maximum seating capacity.
     * @param roomId      ID of the room where this layout is applied.
     * @param layoutType  Type of layout (e.g., Theater, Classroom, Banquet).
     */
    public LayoutConfiguration(int layoutId, String layoutName, int maxCapacity, int roomId, String layoutType) {
        this.layoutId = layoutId;
        this.layoutName = layoutName;
        this.maxCapacity = maxCapacity;
        this.roomId = roomId;
        this.layoutType = layoutType;
        this.sections = sections;
    }

    /**
     * Constructor to create a layout with a specific ID.
     *
     * @param layoutName  Name of the layout.
     * @param maxCapacity Maximum seating capacity.
     * @param roomId      ID of the room where this layout is applied.
     * @param layoutType  Type of layout (e.g., Theater, Classroom, Banquet).
     */
    public LayoutConfiguration(String layoutName, int maxCapacity, int roomId, String layoutType) {
        this.layoutName = layoutName;
        this.maxCapacity = maxCapacity;
        this.roomId = roomId;
        this.layoutType = layoutType;
        this.sections = new ArrayList<>(); // Initialize the sections list
    }

    /**
     * Gets the layout ID.
     *
     * @return The unique identifier of the layout.
     */
    public int getLayoutId() { return layoutId; }

    /**
     * Sets the layout ID.
     *
     * @param layoutId The unique identifier of the layout.
     */
    public void setLayoutId(int layoutId) { this.layoutId = layoutId; }

    /**
     * Gets the name of the layout.
     *
     * @return The name of the layout.
     */
    public String getLayoutName() { return layoutName; }

    /**
     * Sets the name of the layout.
     *
     * @param layoutName The name of the layout.
     */
    public void setLayoutName(String layoutName) { this.layoutName = layoutName; }


    /**
     * Gets the maximum seating capacity.
     *
     * @return The maximum seating capacity.
     */
    public int getMaxCapacity() { return maxCapacity; }

    /**
     * Sets the maximum seating capacity.
     *
     * @param maxCapacity The maximum seating capacity.
     */
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    /**
     * Gets the room ID.
     *
     * @return The ID of the room where this layout is applied.
     */
    public int getRoomId() { return roomId; }

    /**
     * Sets the room ID.
     *
     * @param roomId The ID of the room where this layout is applied.
     */
    public void setRoomId(int roomId) { this.roomId = roomId; }

    /**
     * Gets the type of layout.
     *
     * @return The type of layout (e.g., Theater, Classroom, Banquet).
     */
    public String getLayoutType() { return layoutType; }

    /**
     * Sets the type of layout.
     *
     * @param layoutType The type of layout (e.g., Theater, Classroom, Banquet).
     */
    public void setLayoutType(String layoutType) { this.layoutType = layoutType; }


    /**
     * Gets the list of sections in the layout.
     *
     * @return The list of sections in the layout.
     */
    public List<Section> getSections() {
        return sections;
    }

    /**
     * Sets the list of sections in the layout.
     *
     * @param sections The list of sections in the layout.
     */
    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
