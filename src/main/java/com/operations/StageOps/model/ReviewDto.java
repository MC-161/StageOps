package com.operations.StageOps.model;

/**
 * Data Transfer Object (DTO) for representing review details.
 * This class is used to transfer review data between the client and server.
 */
public class ReviewDto {
    private int reviewId;
    private int clientId;
    private int eventId;
    private int bookingId;
    private int rating;
    private String reviewText;
    private String reviewDate;
    private String eventName; // Will be set dynamically

    // Getters and Setters

    /**
     * Gets the unique ID of the review.
     *
     * @return The review ID.
     */
    public int getReviewId() { return reviewId; }

    /**
     * Sets the unique ID of the review.
     *
     * @param reviewId The review ID.
     */
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    /**
     * Gets the unique ID of the client who wrote the review.
     *
     * @return The client ID.
     */
    public int getClientId() { return clientId; }

    /**
     * Sets the unique ID of the client who wrote the review.
     *
     * @param clientId The client ID.
     */
    public void setClientId(int clientId) { this.clientId = clientId; }

    /**
     * Gets the unique ID of the event that the review is for.
     *
     * @return The event ID.
     */
    public int getEventId() { return eventId; }

    /**
     * Sets the unique ID of the event that the review is for.
     *
     * @param eventId The event ID.
     */
    public void setEventId(int eventId) { this.eventId = eventId; }

    /**
     * Gets the unique ID of the booking that the review is for.
     *
     * @return The booking ID.
     */
    public int getBookingId() { return bookingId; }

    /**
     * Sets the unique ID of the booking that the review is for.
     *
     * @param bookingId The booking ID.
     */
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    /**
     * Gets the rating given in the review.
     *
     * @return The rating.
     */
    public int getRating() { return rating; }

    /**
     * Sets the rating given in the review.
     *
     * @param rating The rating.
     */
    public void setRating(int rating) { this.rating = rating; }

    /**
     * Gets the text of the review.
     *
     * @return The review text.
     */
    public String getReviewText() { return reviewText; }

    /**
     * Sets the text of the review.
     *
     * @param reviewText The review text.
     */
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    /**
     * Gets the date that the review was written.
     *
     * @return The review date.
     */
    public String getReviewDate() { return reviewDate; }

    /**
     * Sets the date that the review was written.
     *
     * @param reviewDate The review date.
     */
    public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }

    /**
     * Gets the name of the event that the review is for.
     *
     * @return The event name.
     */
    public String getEventName() { return eventName; }

    /**
     * Sets the name of the event that the review is for.
     *
     * @param eventName The event name.
     */
    public void setEventName(String eventName) { this.eventName = eventName; }
}
