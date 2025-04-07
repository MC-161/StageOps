package com.operations.StageOps.repository;

import com.operations.StageOps.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveReview_Success() {
        Review review = new Review();
        review.setClientId(1);
        review.setEventId(2);
        review.setBookingId(0);
        review.setRating(5);
        review.setReviewText("Great!");

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(1))).thenReturn(1);
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any())).thenReturn(1);

        int result = reviewRepository.save(review);
        assertEquals(1, result);

        verify(jdbcTemplate).update(anyString(), any(), any(), any(), any(), any());
    }

    @Test
    void testSaveReview_ClientNotExists_ThrowsException() {
        Review review = new Review();
        review.setClientId(999);
        review.setEventId(2);
        review.setBookingId(0);
        review.setRating(5);
        review.setReviewText("Not valid");

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(999))).thenReturn(0);

        assertThrows(IllegalArgumentException.class, () -> reviewRepository.save(review));
    }

    @Test
    void testGetAllReviews() {
        Review review = new Review();
        review.setReviewId(1);
        review.setClientId(1);
        review.setEventId(2);
        review.setBookingId(3);
        review.setRating(5);
        review.setReviewText("Excellent!");
        review.setReviewDate(new Date(System.currentTimeMillis()));

        when(jdbcTemplate.query(anyString(), ArgumentMatchers.<RowMapper<Review>>any()))
                .thenReturn(Arrays.asList(review));

        List<Review> result = reviewRepository.getAllReviews();

        assertEquals(1, result.size());
        assertEquals("Excellent!", result.get(0).getReviewText());
    }

    @Test
    void testGetReviewById() {
        Review review = new Review();
        review.setReviewId(1);
        review.setClientId(1);

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), ArgumentMatchers.<RowMapper<Review>>any()))
                .thenReturn(review);

        Review result = reviewRepository.getReviewById(1);
        assertEquals(1, result.getReviewId());
    }

    @Test
    void testDeleteReview() {
        when(jdbcTemplate.update(anyString(), eq(1))).thenReturn(1);
        int result = reviewRepository.deleteReview(1);
        assertEquals(1, result);
    }

    @Test
    void testGetRecentReviews() {
        Review review = new Review();
        review.setReviewText("Recent review");

        when(jdbcTemplate.query(anyString(), any(Object[].class), ArgumentMatchers.<RowMapper<Review>>any()))
                .thenReturn(List.of(review));

        List<Review> result = reviewRepository.getRecentReviews(5);
        assertEquals(1, result.size());
        assertEquals("Recent review", result.get(0).getReviewText());
    }

    @Test
    void testGetReviewsByEventId() {
        Review review = new Review();
        review.setEventId(10);

        when(jdbcTemplate.query(anyString(), any(Object[].class), ArgumentMatchers.<RowMapper<Review>>any()))
                .thenReturn(List.of(review));

        List<Review> result = reviewRepository.getReviewsByEventId(10);
        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getEventId());
    }

    @Test
    void testGetReviewsByBookingId() {
        Review review = new Review();
        review.setBookingId(20);

        when(jdbcTemplate.query(anyString(), any(Object[].class), ArgumentMatchers.<RowMapper<Review>>any()))
                .thenReturn(List.of(review));

        List<Review> result = reviewRepository.getReviewsByBookingId(20);
        assertEquals(1, result.size());
        assertEquals(20, result.get(0).getBookingId());
    }
}
