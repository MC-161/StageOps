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
}
