package com.operations.StageOps.controller;

import com.operations.StageOps.model.Review;
import com.operations.StageOps.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<String> createReview(@RequestBody Review review) {
        int result = reviewService.saveReview(review);
        if (result > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Review created successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Review!");
        }
    }

    @GetMapping("/{reviewId}")
    public Review getReview(@PathVariable int reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
    }

    // Endpoint to get reviews related to events
    @GetMapping("/events")
    public ResponseEntity<List<Review>> getEventReviews() {
        List<Review> reviews = reviewService.getEventReviews();
        return ResponseEntity.ok(reviews);
    }

    // Endpoint to get reviews related to bookings
    @GetMapping("/bookings")
    public ResponseEntity<List<Review>> getBookingReviews() {
        List<Review> reviews = reviewService.getBookingReviews();
        return ResponseEntity.ok(reviews);
    }

    // Endpoint to get recent reviews (limit results)
    @GetMapping("/recent")
    public ResponseEntity<List<Review>> getRecentReviews(@RequestParam int limit) {
        List<Review> reviews = reviewService.getRecentReviews(limit);
        return ResponseEntity.ok(reviews);
    }

    // Endpoint to get reviews by event ID
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Review>> getReviewsByEventId(@PathVariable int eventId) {
        List<Review> reviews = reviewService.getReviewsByEventId(eventId);
        return ResponseEntity.ok(reviews);
    }

    // Endpoint to get reviews by booking ID
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Review>> getReviewsByBookingId(@PathVariable int bookingId) {
        List<Review> reviews = reviewService.getReviewsByBookingId(bookingId);
        return ResponseEntity.ok(reviews);
    }
}
