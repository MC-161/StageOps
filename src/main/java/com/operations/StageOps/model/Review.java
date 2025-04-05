package com.operations.StageOps.model;

import java.util.Date;

/**
 * Represents a review left by a client for an event.
 * This class stores details about the review, including the rating, text, and date.
 */
public class Review {
    private int reviewId;
    private int clientId;
    private int eventId;
    private int bookingId;
    private int rating;
    private String reviewText;
    private Date reviewDate;

    /**
     * Default constructor required for object creation and JSON deserialization.
     */
    public Review() {}

    /**
     * Constructor to initialize all fields of a review.
     *
     * @param reviewId   Unique identifier for the review.
     * @param clientId   ID of the client who submitted the review.
     * @param eventId    ID of the event being reviewed.
     * @param bookingId  ID of the booking associated with the review.
     * @param rating     Rating given by the client (e.g., 1 to 5 stars).
     * @param reviewText Text of the review provided by the client.
     * @param reviewDate Date when the review was submitted.
     */
    public Review(int reviewId, int clientId, int eventId, int bookingId, int rating, String reviewText, Date reviewDate) {
        this.reviewId = reviewId;
        this.clientId = clientId;
        this.eventId = eventId;
        this.bookingId = bookingId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }

    /** @return The unique ID of the review. */
    public int getReviewId() { return reviewId; }

    /** @param reviewId The unique ID of the review. */
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    /** @return The ID of the client who submitted the review. */
    public int getClientId() { return clientId; }

    /** @param clientId The ID of the client who submitted the review. */
    public void setClientId(int clientId) { this.clientId = clientId; }

    /** @return The ID of the event being reviewed. */
    public int getEventId() { return eventId; }

    /** @param eventId The ID of the event being reviewed. */
    public void setEventId(int eventId) { this.eventId = eventId; }

    /** @return The ID of the booking associated with the review. */
    public int getBookingId() { return bookingId; }

    /** @param bookingId The ID of the booking associated with the review. */
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    /** @return The rating given by the client (e.g., 1 to 5 stars). */
    public int getRating() { return rating; }

    /** @param rating The rating given by the client (e.g., 1 to 5 stars). */
    public void setRating(int rating) { this.rating = rating; }

    /** @return The text of the review provided by the client. */
    public String getReviewText() { return reviewText; }

    /** @param reviewText The text of the review provided by the client. */
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    /** @return The date when the review was submitted. */
    public Date getReviewDate() { return reviewDate; }

    /** @param reviewDate The date when the review was submitted. */
    public void setReviewDate(Date reviewDate) { this.reviewDate = reviewDate; }
}
