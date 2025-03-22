package com.operations.StageOps.service;


import com.operations.StageOps.model.Review;
import com.operations.StageOps.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Save review
    public int saveReview(Review review) {
        return reviewRepository.save(review);
    }

    // Get all reviews
    public List<Review> getAllReviews() {
        return reviewRepository.getAllReviews();
    }

    // Get review by ID
    public Review getReviewById(int reviewId) {
        return reviewRepository.getReviewById(reviewId);
    }

    // Delete review by its ID
    public void deleteReview(int reviewId) {
        int rowsAffected = reviewRepository.deleteReview(reviewId);
    }

    // Get reviews related to events (reviews that have event_id not null)
    public List<Review> getEventReviews() {
        return reviewRepository.getEventReviews();
    }

    // Get reviews related to bookings (reviews that have booking_id not null)
    public List<Review> getBookingReviews() {
        return reviewRepository.getBookingReviews();
    }

    // Get recent reviews (limit results)
    public List<Review> getRecentReviews(int limit) {
        return reviewRepository.getRecentReviews(limit);
    }

    // Get reviews by specific event ID
    public List<Review> getReviewsByEventId(int eventId) {
        return reviewRepository.getReviewsByEventId(eventId);
    }

    // Get reviews by specific booking ID
    public List<Review> getReviewsByBookingId(int bookingId) {
        return reviewRepository.getReviewsByBookingId(bookingId);
    }

}
