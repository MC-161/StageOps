package com.operations.StageOps.repository;

import com.operations.StageOps.model.Review;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for interacting with the reviews table in the database.
 * Provides methods to save, retrieve, and delete reviews for events and bookings.
 */
@Repository
public class ReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for ReviewRepository.
     *
     * @param jdbcTemplate the JdbcTemplate object for executing SQL queries
     */
    public ReviewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new review in the database.
     *
     * @param review the Review object containing the review details to be saved
     * @return the number of rows affected in the database
     * @throws IllegalArgumentException if the client_id does not exist in the clients table
     */
    public int save(Review review) {
        // Check if the client_id exists in the clients table
        String clientCheckSql = "SELECT COUNT(*) FROM clients WHERE client_id = ?";
        int clientExists = jdbcTemplate.queryForObject(clientCheckSql, Integer.class, review.getClientId());

        if (clientExists == 0) {
            throw new IllegalArgumentException("Client with ID " + review.getClientId() + " does not exist.");
        }

        // If client exists, proceed with inserting the review
        String sql = "INSERT INTO review (client_id, event_id, booking_id, rating, review_text, review_date) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";

        // Use null for event_id and booking_id if they are set to 0
        return jdbcTemplate.update(sql,
                review.getClientId(),
                review.getEventId() != 0 ? review.getEventId() : null, // Handle 0 as NULL for event_id
                review.getBookingId() != 0 ? review.getBookingId() : null, // Handle 0 as NULL for booking_id
                review.getRating(),
                review.getReviewText()
        );
    }

    /**
     * Retrieves all reviews from the database.
     *
     * @return a list of all Review objects
     */
    public List<Review> getAllReviews() {
        String sql = "SELECT * FROM review";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Review review = new Review();
            review.setReviewId(rs.getInt("review_id"));
            review.setClientId(rs.getInt("client_id"));
            review.setEventId(rs.getInt("event_id"));
            review.setBookingId(rs.getInt("booking_id"));
            review.setRating(rs.getInt("rating"));
            review.setReviewText(rs.getString("review_text"));
            review.setReviewDate(rs.getDate("review_date"));
            return review;
        });
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param reviewId the ID of the review to be retrieved
     * @return the Review object with the specified ID
     */
    public Review getReviewById(int reviewId) {
        String sql = "SELECT * FROM review WHERE review_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{reviewId}, (rs, rowNum) -> {
            Review review = new Review();
            review.setReviewId(rs.getInt("review_id"));
            review.setClientId(rs.getInt("client_id"));
            review.setEventId(rs.getInt("event_id"));
            review.setBookingId(rs.getInt("booking_id"));
            review.setRating(rs.getInt("rating"));
            review.setReviewText(rs.getString("review_text"));
            review.setReviewDate(rs.getDate("review_date"));
            return review;
        });
    }

    /**
     * Deletes a review by its ID.
     *
     * @param reviewId the ID of the review to be deleted
     * @return the number of rows affected in the database
     */
    public int deleteReview(int reviewId) {
        String sql = "DELETE FROM review WHERE review_id = ?";
        return jdbcTemplate.update(sql, reviewId);  // Returns the number of affected rows
    }

    /**
     * Retrieves all reviews related to events (reviews with a non-null event_id).
     *
     * @return a list of reviews related to events
     */
    public List<Review> getEventReviews() {
        String sql = "SELECT * FROM review WHERE event_id IS NOT NULL";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Review review = new Review();
            review.setReviewId(rs.getInt("review_id"));
            review.setClientId(rs.getInt("client_id"));
            review.setEventId(rs.getInt("event_id"));
            review.setBookingId(rs.getInt("booking_id"));
            review.setRating(rs.getInt("rating"));
            review.setReviewText(rs.getString("review_text"));
            review.setReviewDate(rs.getDate("review_date"));
            return review;
        });
    }

    /**
     * Retrieves all reviews related to bookings (reviews with a non-null booking_id).
     *
     * @return a list of reviews related to bookings
     */
    public List<Review> getBookingReviews() {
        String sql = "SELECT * FROM review WHERE booking_id IS NOT NULL";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Review review = new Review();
            review.setReviewId(rs.getInt("review_id"));
            review.setClientId(rs.getInt("client_id"));
            review.setEventId(rs.getInt("event_id"));
            review.setBookingId(rs.getInt("booking_id"));
            review.setRating(rs.getInt("rating"));
            review.setReviewText(rs.getString("review_text"));
            review.setReviewDate(rs.getDate("review_date"));
            return review;
        });
    }

    /**
     * Retrieves a list of recent reviews, limited by the specified number.
     *
     * @param limit the maximum number of recent reviews to retrieve
     * @return a list of recent reviews
     */
    public List<Review> getRecentReviews(int limit) {
        String sql = "SELECT * FROM review ORDER BY review_date DESC LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{limit}, (rs, rowNum) -> {
            Review review = new Review();
            review.setReviewId(rs.getInt("review_id"));
            review.setClientId(rs.getInt("client_id"));
            review.setEventId(rs.getInt("event_id"));
            review.setBookingId(rs.getInt("booking_id"));
            review.setRating(rs.getInt("rating"));
            review.setReviewText(rs.getString("review_text"));
            review.setReviewDate(rs.getDate("review_date"));
            return review;
        });
    }

    /**
     * Retrieves all reviews for a specific event.
     *
     * @param eventId the ID of the event for which reviews are to be retrieved
     * @return a list of reviews for the specified event
     */
    public List<Review> getReviewsByEventId(int eventId) {
        String sql = "SELECT * FROM review WHERE event_id = ?";
        return jdbcTemplate.query(sql, new Object[]{eventId}, (rs, rowNum) -> {
            Review review = new Review();
            review.setReviewId(rs.getInt("review_id"));
            review.setClientId(rs.getInt("client_id"));
            review.setEventId(rs.getInt("event_id"));
            review.setBookingId(rs.getInt("booking_id"));
            review.setRating(rs.getInt("rating"));
            review.setReviewText(rs.getString("review_text"));
            review.setReviewDate(rs.getDate("review_date"));
            return review;
        });
    }

    /**
     * Retrieves all reviews for a specific booking.
     *
     * @param bookingId the ID of the booking for which reviews are to be retrieved
     * @return a list of reviews for the specified booking
     */
    public List<Review> getReviewsByBookingId(int bookingId) {
        String sql = "SELECT * FROM review WHERE booking_id = ?";
        return jdbcTemplate.query(sql, new Object[]{bookingId}, (rs, rowNum) -> {
            Review review = new Review();
            review.setReviewId(rs.getInt("review_id"));
            review.setClientId(rs.getInt("client_id"));
            review.setEventId(rs.getInt("event_id"));
            review.setBookingId(rs.getInt("booking_id"));
            review.setRating(rs.getInt("rating"));
            review.setReviewText(rs.getString("review_text"));
            review.setReviewDate(rs.getDate("review_date"));
            return review;
        });
    }
}
