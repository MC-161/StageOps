package com.operations.StageOps.model;

import java.util.Date;

public class Review {
    private int reviewId;
    private int clientId;
    private int eventId;
    private int bookingId;
    private int rating;
    private String reviewText;
    private Date reviewDate;

    // Constructors, Getters, and Setters
    public Review() {}

    public Review(int reviewId, int clientId, int eventId, int bookingId, int rating, String reviewText, Date reviewDate) {
        this.reviewId = reviewId;
        this.clientId = clientId;
        this.eventId = eventId;
        this.bookingId = bookingId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getClientId() { return clientId; }
    public void setClientId(int userId) { this.clientId = userId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public Date getReviewDate() { return reviewDate; }
    public void setReviewDate(Date reviewDate) { this.reviewDate = reviewDate; }
}
