package com.operations.StageOps.service;

import com.operations.StageOps.model.Review;
import com.operations.StageOps.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Review entities.
 * Provides methods for CRUD operations (Create, Read, and Delete) on review records.
 * The class interacts with the `ReviewRepository` to perform database operations.
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * Constructor for initializing the ReviewRepository.
     *
     * @param reviewRepository the ReviewRepository used for interacting with the database.
     */
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Saves a new review record into the database.
     *
     * @param review the Review object containing the data to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int saveReview(Review review) {
        return reviewRepository.save(review);
    }

    /**
     * Retrieves all review records from the database.
     *
     * @return a list of Review objects representing all reviews.
     */
    public List<Review> getAllReviews() {
        return reviewRepository.getAllReviews();
    }

    /**
     * Retrieves a specific review record by its ID.
     *
     * @param reviewId the ID of the review.
     * @return the Review object corresponding to the given review ID.
     */
    public Review getReviewById(int reviewId) {
        return reviewRepository.getReviewById(reviewId);
    }

    /**
     * Deletes a review record by its ID.
     *
     * @param reviewId the ID of the review to be deleted.
     */
    public void deleteReview(int reviewId) {
        int rowsAffected = reviewRepository.deleteReview(reviewId);
    }
}
