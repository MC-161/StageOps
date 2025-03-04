package com.operations.StageOps.repository;


import com.operations.StageOps.model.Review;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReviewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save a new review
    public int save(Review review) {
        String sql = "INSERT INTO review (user_id, event_id, booking_id, rating, review_text, review_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, review.getUserId(), review.getEventId(), review.getBookingId(),
                review.getRating(), review.getReviewText(), review.getReviewDate());
    }

    // Get all reviews
    public List<Review> getAllReviews() {
        String sql = "SELECT * FROM review";
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

    // Get review by ID
    public Review getReviewById(int reviewId) {
        String sql = "SELECT * FROM review WHERE review_id = ?";
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
    // Delete a review by its ID
    public int deleteReview(int reviewId) {
        String sql = "DELETE FROM reviews WHERE review_id = ?";
        return jdbcTemplate.update(sql, reviewId);  // Returns the number of affected rows
    }
}
