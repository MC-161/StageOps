package com.operations.StageOps.model;

/**
 * This class represents a section in a room. It contains information about the section's
 * ID, layout, section name, and its capacity.
 */
public class Section {

    private int sectionID;
    private int layoutID;
    private String sectionName;
    private int capacity;

    /**
     * Constructs a new Section object with the given attributes.
     *
     * @param sectionID   the unique ID of the section
     * @param layoutID    the ID of the layout associated with the section
     * @param sectionName the name of the section
     * @param capacity    the seating capacity of the section
     */
    public Section(int sectionID, int layoutID, String sectionName, int capacity) {
        this.sectionID = sectionID;
        this.layoutID = layoutID;
        this.sectionName = sectionName;
        this.capacity = capacity;
    }

    /**
     * Gets the section ID.
     *
     * @return the section ID
     */
    public int getSectionID() {
        return sectionID;
    }

    /**
     * Sets the section ID.
     *
     * @param sectionID the new section ID
     */
    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    /**
     * Gets the layout ID.
     *
     * @return the layout ID
     */
    public int getLayoutID() {
        return layoutID;
    }

    /**
     * Sets the layout ID.
     *
     * @param layoutID the new layout ID
     */
    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
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

    /**
     * Gets the seating capacity of the section.
     *
     * @return the seating capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the seating capacity of the section.
     *
     * @param capacity the new seating capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Provides a string representation of the Section object.
     *
     * @return a string describing the section object
     */
    @Override
    public String toString() {
        return "Section{" +
                "sectionID=" + sectionID +
                ", layoutID=" + layoutID +
                ", sectionName='" + sectionName + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}