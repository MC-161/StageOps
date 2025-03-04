package com.operations.StageOps.model;

import java.util.Date;

/**
 * Represents a review given by a user for an event and booking.
 */
public class Review {
    private int reviewId;
    private int userId;
    private int eventId;
    private int bookingId;
    private int rating;
    private String reviewText;
    private Date reviewDate;

    /**
     * Default constructor.
     */
    public Review() {
    }

    /**
     * Parameterized constructor to initialize a review.
     *
     * @param reviewId   Unique identifier for the review.
     * @param userId     ID of the user who submitted the review.
     * @param eventId    ID of the event being reviewed.
     * @param bookingId  ID of the booking related to the review.
     * @param rating     Rating given by the user (e.g., 1-5 stars).
     * @param reviewText Textual review provided by the user.
     * @param reviewDate Date when the review was submitted.
     */
    public Review(int reviewId, int userId, int eventId, int bookingId, int rating, String reviewText, Date reviewDate) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.eventId = eventId;
        this.bookingId = bookingId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
}
