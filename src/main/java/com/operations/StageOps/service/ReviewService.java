package com.operations.StageOps.service;


import com.operations.StageOps.model.Review;
import com.operations.StageOps.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing reviews.
 * Provides various methods to save, retrieve, delete, and manage reviews for events and bookings.
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;


    /**
     * Constructor to inject the ReviewRepository dependency.
     *
     * @param reviewRepository The repository that handles data operations for reviews.
     */
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Save a new review.
     *
     * @param review The review to be saved.
     * @return The result of the save operation (e.g., affected rows or review ID).
     */
    public int saveReview(Review review) {
        return reviewRepository.save(review);
    }

    /**
     * Get all reviews.
     *
     * @return A list of all reviews in the system.
     */
    public List<Review> getAllReviews() {
        return reviewRepository.getAllReviews();
    }

    /**
     * Get a review by its ID.
     *
     * @param reviewId The ID of the review to be retrieved.
     * @return The review with the specified ID.
     */
    public Review getReviewById(int reviewId) {
        return reviewRepository.getReviewById(reviewId);
    }

    /**
     * Get all reviews that are related to events (reviews with a non-null event_id).
     *
     * @return A list of reviews related to events.
     */
    public void deleteReview(int reviewId) {
        int rowsAffected = reviewRepository.deleteReview(reviewId);
    }

    /**
     * Get all reviews that are related to bookings (reviews with a non-null booking_id).
     *
     * @return A list of reviews related to bookings.
     */
    public List<Review> getEventReviews() {
        return reviewRepository.getEventReviews();
    }

    /**
     * Get all reviews that are related to bookings (reviews with a non-null booking_id).
     *
     * @return A list of reviews related to bookings.
     */
    public List<Review> getBookingReviews() {
        return reviewRepository.getBookingReviews();
    }

    /**
     * Get all reviews that are related to events (reviews with a non-null event_id).
     *
     * @return A list of reviews related to events.
     */
    public List<Review> getRecentReviews(int limit) {
        return reviewRepository.getRecentReviews(limit);
    }

    /**
     * Get reviews for a specific event, based on the event ID.
     *
     * @param eventId The event ID for which the reviews are requested.
     * @return A list of reviews associated with the specified event.
     */
    public List<Review> getReviewsByEventId(int eventId) {
        return reviewRepository.getReviewsByEventId(eventId);
    }

    /**
     * Get reviews for a specific booking, based on the booking ID.
     *
     * @param bookingId The booking ID for which the reviews are requested.
     * @return A list of reviews associated with the specified booking.
     */
    public List<Review> getReviewsByBookingId(int bookingId) {
        return reviewRepository.getReviewsByBookingId(bookingId);
    }

}
