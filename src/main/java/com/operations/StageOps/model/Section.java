package com.operations.StageOps.model;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private int sectionId;
    private String sectionName;  // E.g., "Row A", "Table 1"
    private String sectionType;  // E.g., "Row", "Table"
    private List<Seating> seats;  // List of seats in this section

    // Constructors, Getters, and Setters
    public Section() {}

    public Section(String sectionName, String sectionType) {
        this.sectionName = sectionName;
        this.sectionType = sectionType;
        this.seats = new ArrayList<>();
    }

    public Section(int sectionId, String sectionName, String sectionType, int layoutId) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.sectionType = sectionType;
        this.seats = new ArrayList<>();
    }

    public List<Seating> getSeats() { return seats; }
    public void setSeats(List<Seating> seats) { this.seats = seats; }

    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }

    public String getSectionType() { return sectionType; }
    public void setSectionType(String sectionType) { this.sectionType = sectionType; }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    // Other getters and setters...
}
