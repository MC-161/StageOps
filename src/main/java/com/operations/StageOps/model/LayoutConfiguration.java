package com.operations.StageOps.model;

import java.util.List;

/**
 * Represents the layout configuration of a room for an event.
 */
public class LayoutConfiguration {
    private int layoutId;
    private String layoutName;
    private int maxCapacity;
    private int roomId;
    private List<Section> sections;
    private String layoutType;

    /**
     * Default constructor.
     */
    public LayoutConfiguration() {
    }

    /**
     * Parameterized constructor to initialize a layout configuration.
     *
     * @param layoutId   Unique identifier for the layout.
     * @param layoutName Name of the layout configuration.
     * @param maxCapacity Maximum number of people the layout can accommodate.
     * @param roomId     Room associated with this layout.
     * @param layoutType Type of layout (e.g., theater, classroom, banquet).
     */
    public LayoutConfiguration(int layoutId, String layoutName, int maxCapacity, int roomId, String layoutType) {
        this.layoutId = layoutId;
        this.layoutName = layoutName;
        this.maxCapacity = maxCapacity;
        this.roomId = roomId;
        this.layoutType = layoutType;
        this.sections = sections;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
