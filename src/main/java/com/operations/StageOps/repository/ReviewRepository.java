package com.operations.StageOps.repository;

import com.operations.StageOps.model.Review;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class to handle CRUD operations for review records in the database.
 * It provides methods to save, retrieve, and delete reviews.
 */
@Repository
public class ReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for initializing the JdbcTemplate.
     *
     * @param jdbcTemplate the JdbcTemplate object used to interact with the database.
     */
    public ReviewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new review record in the 'review' table.
     *
     * @param review the Review object containing the data to be saved.
     * @return the number of rows affected by the insert query.
     */
    public int save(Review review) {
        String sql = "INSERT INTO review (user_id, event_id, booking_id, rating, review_text, review_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        // Execute the SQL insert query and return the number of rows affected (usually 1).
        return jdbcTemplate.update(sql, review.getUserId(), review.getEventId(), review.getBookingId(),
                review.getRating(), review.getReviewText(), review.getReviewDate());
    }

    /**
     * Retrieves all review records from the 'review' table.
     *
     * @return a list of all Review objects in the database.
     */
    public List<Review> getAllReviews() {
        String sql = "SELECT * FROM review";
        // Query all reviews and map each result row to a Review object.
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Review review = new Review();
            review.setReviewId(rs.getInt("review_id"));
            review.setUserId(rs.getInt("user_id"));
            review.setEventId(rs.getInt("event_id"));
            review.setBookingId(rs.getInt("booking_id"));
            review.setRating(rs.getInt("rating"));
            review.setReviewText(rs.getString("review_text"));
            review.setReviewDate(rs.getDate("review_date"));
            return review;
        });
    }

    /**
     * Retrieves a single review by its ID.
     *
     * @param reviewId the ID of the review to be fetched.
     * @return the Review object corresponding to the given reviewId.
     */
    public Review getReviewById(int reviewId) {
        String sql = "SELECT * FROM review WHERE review_id = ?";
        // Query a single review using its ID and map the result to a Review object.
        return jdbcTemplate.queryForObject(sql, new Object[]{reviewId}, (rs, rowNum) -> {
            Review review = new Review();
            review.setReviewId(rs.getInt("review_id"));
            review.setUserId(rs.getInt("user_id"));
            review.setEventId(rs.getInt("event_id"));
            review.setBookingId(rs.getInt("booking_id"));
            review.setRating(rs.getInt("rating"));
            review.setReviewText(rs.getString("review_text"));
            review.setReviewDate(rs.getDate("review_date"));
            return review;
        });
    }

    /**
     * Deletes a review record by its ID.
     *
     * @param reviewId the ID of the review to be deleted.
     * @return the number of rows affected (should be 1 if successfully deleted).
     */
    public int deleteReview(int reviewId) {
        String sql = "DELETE FROM review WHERE review_id = ?";
        // Executes the delete query and returns the number of rows affected (typically 1 if successful).
        return jdbcTemplate.update(sql, reviewId);
    }
}
