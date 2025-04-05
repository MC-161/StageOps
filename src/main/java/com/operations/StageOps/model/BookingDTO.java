package com.operations.StageOps.model;

import javafx.beans.property.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * A Data Transfer Object (DTO) representing a booking, designed for use with JavaFX properties.
 * This class provides observable properties for UI binding in JavaFX applications.
 */
public class BookingDTO {

    private final IntegerProperty bookingId;
    private final IntegerProperty clientId;
    private final ObjectProperty<ZonedDateTime> startTime;
    private final ObjectProperty<ZonedDateTime> endTime;
    private final StringProperty status;
    private final DoubleProperty totalCost;
    private final ListProperty<BookingRoomAssignment> roomAssignments;  // Add this for room assignments

    /**
     * Constructs a BookingDTO instance with the provided booking details.
     *
     * @param bookingId       The unique identifier for the booking.
     * @param clientId        The unique identifier for the client making the booking.
     * @param startTime       The start time of the booking.
     * @param endTime         The end time of the booking.
     * @param status          The current status of the booking (e.g., "confirmed", "cancelled").
     * @param totalCost       The total cost associated with the booking.
     * @param roomAssignments The list of room assignments associated with the booking.
     */
    public BookingDTO(int bookingId, int clientId, ZonedDateTime startTime, ZonedDateTime endTime,
                      String status, double totalCost, List<BookingRoomAssignment> roomAssignments) {
        this.bookingId = new SimpleIntegerProperty(bookingId);
        this.clientId = new SimpleIntegerProperty(clientId);
        this.startTime = new SimpleObjectProperty<>(startTime);
        this.endTime = new SimpleObjectProperty<>(endTime);
        this.status = new SimpleStringProperty(status);
        this.totalCost = new SimpleDoubleProperty(totalCost);
        this.roomAssignments = new SimpleListProperty<>(javafx.collections.FXCollections.observableArrayList(roomAssignments));  // Initialize the room assignments list
    }

    /**
     * Gets the booking ID.
     *
     * @return The unique identifier of the booking.
     */
    public int getBookingId() {
        return bookingId.get();
    }

    /**
     * Gets the booking ID property.
     *
     * @return The booking ID property.
     */
    public IntegerProperty bookingIdProperty() {
        return bookingId;
    }


    /**
     * Sets the booking ID.
     *
     * @param bookingId The unique identifier of the booking.
     */
    public void setBookingId(int bookingId) {
        this.bookingId.set(bookingId);
    }

    /**
     * Gets the client ID.
     *
     * @return The unique identifier of the client making the booking.
     */
    public int getClientId() {
        return clientId.get();
    }


    /**
     * Gets the client ID property.
     *
     * @return The client ID property.
     */
    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    /**
     * Sets the client ID.
     *
     * @param clientId The unique identifier of the client making the booking.
     */
    public void setClientId(int clientId) {
        this.clientId.set(clientId);
    }

    /**
     * Gets the start time of the booking.
     *
     * @return The start time of the booking.
     */
    public ZonedDateTime getStartTime() {
        return startTime.get();
    }

    /**
     * Gets the start time property.
     *
     * @return The start time property.
     */
    public ObjectProperty<ZonedDateTime> startTimeProperty() {
        return startTime;
    }

    /**
     * Sets the start time of the booking.
     *
     * @param startTime The start time of the booking.
     */
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime.set(startTime);
    }

    /**
     * Gets the end time of the booking.
     *
     * @return The end time of the booking.
     */
    public ZonedDateTime getEndTime() {
        return endTime.get();
    }

    /**
     * Gets the end time property.
     *
     * @return The end time property.
     */
    public ObjectProperty<ZonedDateTime> endTimeProperty() {
        return endTime;
    }

    /**
     * Sets the end time of the booking.
     *
     * @param endTime The end time of the booking.
     */
    public void setEndTime(ZonedDateTime endTime) {
        this.endTime.set(endTime);
    }

    /**
     * Gets the status of the booking.
     *
     * @return The status of the booking.
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * Gets the status property.
     *
     * @return The status property.
     */
    public StringProperty statusProperty() {
        return status;
    }

    /**
     * Sets the status of the booking.
     *
     * @param status The status of the booking.
     */
    public void setStatus(String status) {
        this.status.set(status);
    }

    /**
     * Gets the total cost of the booking.
     *
     * @return The total cost of the booking.
     */
    public double getTotalCost() {
        return totalCost.get();
    }

    /**
     * Gets the total cost property.
     *
     * @return The total cost property.
     */
    public DoubleProperty totalCostProperty() {
        return totalCost;
    }

    /**
     * Sets the total cost of the booking.
     *
     * @param totalCost The total cost of the booking.
     */
    public void setTotalCost(double totalCost) {
        this.totalCost.set(totalCost);
    }

    /**
     * Gets the list of room assignments associated with the booking.
     *
     * @return The list of room assignments associated with the booking.
     */
    public List<BookingRoomAssignment> getRoomAssignments() {
        return roomAssignments.get();
    }

    /**
     * Gets the list of room assignments property.
     *
     * @return The list of room assignments property.
     */
    public ListProperty<BookingRoomAssignment> roomAssignmentsProperty() {
        return roomAssignments;
    }

    /**
     * Sets the list of room assignments associated with the booking.
     *
     * @param roomAssignments The list of room assignments associated with the booking.
     */
    public void setRoomAssignments(List<BookingRoomAssignment> roomAssignments) {
        this.roomAssignments.setAll(roomAssignments);
    }
}
