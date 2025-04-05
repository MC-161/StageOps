package com.operations.StageOps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.Date;

/**
 * Data Transfer Object (DTO) for RevenueTracking.
 * Used for transferring revenue tracking details in a structured format.
 */
public class RevenueTrackingDTO {

    private IntegerProperty revenueId;
    private IntegerProperty roomId;
    private IntegerProperty eventId;
    private IntegerProperty bookingId;
    private DoubleProperty totalRevenue;
    private DoubleProperty ticketSales;
    private DoubleProperty venueHire;
    // Add createdAt and updatedAt fields
    private Date createdAt;   // You can use Date type if it's a timestamp
    private Date updatedAt;   // You can use Date type as well

    @JsonFormat(pattern = "yyyy-MM-dd") // Ensure proper date format for JSON
    private ObjectProperty<LocalDate> bookingDate;

    private StringProperty status;

    // Default constructor (needed for JSON deserialization)
    public RevenueTrackingDTO() {
        this.revenueId = new SimpleIntegerProperty();
        this.roomId = new SimpleIntegerProperty();
        this.eventId = new SimpleIntegerProperty();
        this.bookingId = new SimpleIntegerProperty();
        this.totalRevenue = new SimpleDoubleProperty();
        this.ticketSales = new SimpleDoubleProperty();
        this.venueHire = new SimpleDoubleProperty();
        this.bookingDate = new SimpleObjectProperty<>();
        this.status = new SimpleStringProperty();
    }

    /**
     * Constructor to initialize all fields.
     *
     * @param revenueId    Unique identifier for the revenue record.
     * @param roomId       ID of the room where the event/booking occurred.
     * @param eventId      ID of the associated event.
     * @param bookingId    ID of the associated booking.
     * @param totalRevenue Total revenue generated.
     * @param ticketSales  Revenue from ticket sales.
     * @param venueHire    Revenue from venue hire.
     * @param bookingDate  Date when the booking was made.
     * @param status       Current status of the revenue record.
     */
    public RevenueTrackingDTO(@JsonProperty("revenueId") int revenueId,
                              @JsonProperty("roomId") int roomId,
                              @JsonProperty("eventId") int eventId,
                              @JsonProperty("bookingId") int bookingId,
                              @JsonProperty("totalRevenue") double totalRevenue,
                              @JsonProperty("ticketSales") double ticketSales,
                              @JsonProperty("venueHire") double venueHire,
                              @JsonProperty("bookingDate") LocalDate bookingDate,
                              @JsonProperty("status") String status) {
        this.revenueId = new SimpleIntegerProperty(revenueId);
        this.roomId = new SimpleIntegerProperty(roomId);
        this.eventId = new SimpleIntegerProperty(eventId);
        this.bookingId = new SimpleIntegerProperty(bookingId);
        this.totalRevenue = new SimpleDoubleProperty(totalRevenue);
        this.ticketSales = new SimpleDoubleProperty(ticketSales);
        this.venueHire = new SimpleDoubleProperty(venueHire);
        this.bookingDate = new SimpleObjectProperty<>(bookingDate);
        this.status = new SimpleStringProperty(status);
    }

    /** @return Property for revenue ID. */
    public IntegerProperty revenueIdProperty() { return revenueId; }

    /** @return Revenue ID. */
    public int getRevenueId() { return revenueId.get(); }

    /** @param revenueId The revenue ID to set. */
    public void setRevenueId(int revenueId) { this.revenueId.set(revenueId); }

    /** @return Property for room ID. */
    public IntegerProperty roomIdProperty() { return roomId; }

    /** @return Room ID. */
    public int getRoomId() { return roomId.get(); }

    /** @param roomId The room ID to set. */
    public void setRoomId(int roomId) { this.roomId.set(roomId); }

    /** @return Property for event ID. */
    public IntegerProperty eventIdProperty() { return eventId; }

    /** @return Event ID. */
    public int getEventId() { return eventId.get(); }

    /** @param eventId The event ID to set. */
    public void setEventId(int eventId) { this.eventId.set(eventId); }

    /** @return Property for booking ID. */
    public IntegerProperty bookingIdProperty() { return bookingId; }

    /** @return Booking ID. */
    public int getBookingId() { return bookingId.get(); }

    /** @param bookingId The booking ID to set. */
    public void setBookingId(int bookingId) { this.bookingId.set(bookingId); }

    /** @return Property for total revenue. */
    public DoubleProperty totalRevenueProperty() { return totalRevenue; }

    /** @return Total revenue. */
    public double getTotalRevenue() { return totalRevenue.get(); }

    /** @param totalRevenue The total revenue to set. */
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue.set(totalRevenue); }

    /** @return Property for ticket sales. */
    public DoubleProperty ticketSalesProperty() { return ticketSales; }

    /** @return Revenue from ticket sales. */
    public double getTicketSales() { return ticketSales.get(); }

    /** @param ticketSales The revenue from ticket sales to set. */
    public void setTicketSales(double ticketSales) { this.ticketSales.set(ticketSales); }

    /** @return Property for venue hire. */
    public DoubleProperty venueHireProperty() { return venueHire; }

    /** @return Revenue from venue hire. */
    public double getVenueHire() { return venueHire.get(); }

    /** @param venueHire The revenue from venue hire to set. */
    public void setVenueHire(double venueHire) { this.venueHire.set(venueHire); }

    /** @return Property for booking date. */
    public ObjectProperty<LocalDate> bookingDateProperty() { return bookingDate; }

    /** @return Booking date. */
    public LocalDate getBookingDate() { return bookingDate.get(); }

    /** @param bookingDate The booking date to set. */
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate.set(bookingDate); }

    /** @return Property for status. */
    public StringProperty statusProperty() { return status; }

    /** @return Current status of the revenue record. */
    public String getStatus() { return status.get(); }

    /** @param status The status to set. */
    public void setStatus(String status) { this.status.set(status); }

    /** @return The date when the revenue record was created. */
    public Date getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt The date when the revenue record was created. */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /** @return The date when the revenue record was last updated. */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /** @param updatedAt The date when the revenue record was last updated. */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
