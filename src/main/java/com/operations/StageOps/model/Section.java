package com.operations.StageOps.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a section within a layout, containing a group of seats.
 * A section can be of different types, such as "Row" or "Table".
 */
public class Section {
    private int sectionId;
    private String sectionName;  // E.g., "Row A", "Table 1"
    private String sectionType;  // E.g., "Row", "Table"
    private List<Seating> seats;  // List of seats in this section


    /**
     * Default constructor.
     */
    public Section() {}

    /**
     * Constructs a section with the given name and type.
     *
     * @param sectionName The name of the section.
     * @param sectionType The type of the section (e.g., "Row", "Table").
     */
    public Section(String sectionName, String sectionType) {
        this.sectionName = sectionName;
        this.sectionType = sectionType;
        this.seats = new ArrayList<>();
    }

    /**
     * Constructs a section with a section ID, name, type, and layout ID.
     *
     * @param sectionId   The unique ID of the section.
     * @param sectionName The name of the section.
     * @param sectionType The type of the section.
     */
    public Section(int sectionId, String sectionName, String sectionType, int layoutId) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.sectionType = sectionType;
        this.seats = new ArrayList<>();
    }

    /**
     * Gets the unique ID of the section.
     *
     * @return The section ID.
     */
    public List<Seating> getSeats() { return seats; }

    /**
     * Sets the list of seats in this section.
     *
     * @param seats The list of seats.
     */
    public void setSeats(List<Seating> seats) { this.seats = seats; }

    /**
     * Gets the name of the section.
     *
     * @return The section name.
     */
    public String getSectionName() { return sectionName; }

    /**
     * Sets the name of the section.
     *
     * @param sectionName The section name.
     */
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }

    /**
     * Gets the type of the section.
     *
     * @return The section type.
     */
    public String getSectionType() { return sectionType; }

    /**
     * Sets the type of the section.
     *
     * @param sectionType The section type.
     */
    public void setSectionType(String sectionType) { this.sectionType = sectionType; }

    /**
     * Gets the unique ID of the section.
     *
     * @return The section ID.
     */
    public int getSectionId() {
        return sectionId;
    }

    /**
     * Sets the unique ID of the section.
     *
     * @param sectionId The section ID.
     */
    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    // Other getters and setters...
}
