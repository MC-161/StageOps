package com.operations.StageOps.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a section in a room. It contains information about the section's
 * ID, layout, section name, and its capacity.
 */
public class Section {

    private int sectionId;
    private String sectionName;
    private String sectionType;
    private List<Seating> seats;  // List of seats in this section

    public Section() {}

    public Section(String sectionName, String sectionType) {
        this.sectionName = sectionName;
        this.sectionType = sectionType;
        this.seats = new ArrayList<>();
    }

    /**
     * Constructs a new Section object with the given attributes.
     *
     * @param sectionId   the unique ID of the section
     * @param sectionName the name of the section
     * @param sectionType the type of the section
     */
    public Section(int sectionId, String sectionName, String sectionType, int layoutId) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.sectionType = sectionType;
        this.seats = new ArrayList<>();
    }

    /**
     * Gets the section ID.
     *
     * @return the section ID
     */
    public int getSectionId() {
        return sectionId;
    }

    /**
     * Sets the section ID.
     *
     * @param sectionId the new section ID
     */
    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * Gets the section name.
     *
     * @return the section name
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Sets the section name.
     *
     * @param sectionName the new section name
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }


    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public List<Seating> getSeats() {
        return seats;
    }

    public void setSeats(List<Seating> seats) {
        this.seats = seats;
    }
}