package com.operations.StageOps.model;

import java.util.List;

public class LayoutConfiguration {
    private int layoutId;
    private String layoutName;
    private int maxCapacity;
    private int roomId;
    private String layoutType;  // Could be types like "Theater", "Classroom", "Banquet", etc.

    private List<Section> sections;  // This will hold all the sections in the layout
    public LayoutConfiguration() {}

    public LayoutConfiguration(int layoutId, String layoutName, int maxCapacity, int roomId, String layoutType) {
        this.layoutId = layoutId;
        this.layoutName = layoutName;
        this.maxCapacity = maxCapacity;
        this.roomId = roomId;
        this.layoutType = layoutType;
        this.sections = sections;
    }

    public int getLayoutId() { return layoutId; }
    public void setLayoutId(int layoutId) { this.layoutId = layoutId; }

    public String getLayoutName() { return layoutName; }
    public void setLayoutName(String layoutName) { this.layoutName = layoutName; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getLayoutType() { return layoutType; }
    public void setLayoutType(String layoutType) { this.layoutType = layoutType; }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
