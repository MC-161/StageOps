package com.operations.StageOps.repository;

import com.operations.StageOps.model.RevenueTracking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RevenueTrackingRepositoryTest {

    private JdbcTemplate jdbcTemplate;
    private RevenueTrackingRepository repository;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        repository = new RevenueTrackingRepository(jdbcTemplate);
    }

    @Test
    void testSave() {
        RevenueTracking rt = new RevenueTracking();
        rt.setRoomId(1);
        rt.setEventId(2);
        rt.setBookingId(3);
        rt.setBookingDate(LocalDate.now());
        rt.setTicketSales(100.0);
        rt.setVenueHire(200.0);
        rt.setTotalRevenue(300.0);
        rt.setStatus("CONFIRMED");

        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(1);

        int result = repository.save(rt);
        assertEquals(1, result);
    }

    @Test
    void testGetRevenueTrackingById() {
        int revenueId = 1;
        RevenueTracking expected = new RevenueTracking();
        expected.setRevenueId(revenueId);
        expected.setRoomId(10);
        expected.setBookingDate(LocalDate.of(2025, 4, 1));
        expected.setStatus("DONE");

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM revenue_tracking WHERE revenue_id = ?"),
                ArgumentMatchers.<Object[]>any(),
                ArgumentMatchers.<RowMapper<RevenueTracking>>any())
        ).thenReturn(expected);

        RevenueTracking actual = repository.getRevenueTrackingById(revenueId);
        assertNotNull(actual);
        assertEquals(expected.getRevenueId(), actual.getRevenueId());
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Test
    void testGetAllRevenueTrackingEntries() {
        List<RevenueTracking> mockList = List.of(new RevenueTracking(), new RevenueTracking());

        when(jdbcTemplate.query(
                eq("SELECT * FROM revenue_tracking"),
                ArgumentMatchers.<Object[]>any(),
                ArgumentMatchers.<RowMapper<RevenueTracking>>any())
        ).thenReturn(mockList);

        List<RevenueTracking> result = repository.getAllRevenueTrackingEntries();
        assertEquals(2, result.size());
    }

    @Test
    void testGetTotalRevenueByDate() {
        String date = "2025-04-07";
        when(jdbcTemplate.queryForObject(
                eq("SELECT SUM(total_revenue) FROM revenue_tracking WHERE DATE(booking_date) = ?"),
                any(Object[].class),
                eq(Double.class))
        ).thenReturn(500.0);

        double result = repository.getTotalRevenueByDate(date);
        assertEquals(500.0, result);
    }

    @Test
    void testGetRevenueTrackingEntriesByMonth() {
        int month = 4;
        int year = 2025;
        List<RevenueTracking> expected = List.of(new RevenueTracking());

        when(jdbcTemplate.query(
                eq("SELECT * FROM revenue_tracking WHERE EXTRACT(MONTH FROM booking_date) = ? AND EXTRACT(YEAR FROM booking_date) = ?"),
                any(Object[].class),
                ArgumentMatchers.<RowMapper<RevenueTracking>>any())
        ).thenReturn(expected);

        List<RevenueTracking> actual = repository.getRevenueTrackingEntriesByMonth(month, year);
        assertEquals(1, actual.size());
    }

    @Test
    void testSumRevenueByDateBetween() {
        LocalDate start = LocalDate.of(2025, 4, 1);
        LocalDate end = LocalDate.of(2025, 4, 7);

        when(jdbcTemplate.queryForObject(
                eq("SELECT SUM(total_revenue) FROM revenue_tracking WHERE booking_date BETWEEN ? AND ?"),
                eq(Double.class),
                eq(start), eq(end))
        ).thenReturn(1200.0);

        double total = repository.sumRevenueByDateBetween(start, end);
        assertEquals(1200.0, total);
    }

    @Test
    void testGetLifetimeRevenueByYear() {
        Map<String, Object> row = new HashMap<>();
        row.put("year", 2024);
        row.put("total_revenue", 10000.0);

        List<Map<String, Object>> mockData = List.of(row);

        when(jdbcTemplate.queryForList(anyString())).thenReturn(mockData);

        List<Map<String, Object>> result = repository.getLifetimeRevenueByYear();
        assertFalse(result.isEmpty());
        assertEquals(10000.0, result.get(0).get("total_revenue"));
    }
}
