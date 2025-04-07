package com.operations.StageOps.repository;

import com.operations.StageOps.model.Seating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SeatingRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SeatingRepository seatingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final Seating sampleSeating = new Seating("S1", 101, 1, true, false, "A");

    @Test
    void testSave() {
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        int result = seatingRepository.save(sampleSeating);
        assertEquals(1, result);
    }

    @Test
    void testGetAllSeatsByRoomAndSection() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(sampleSeating));

        List<Seating> results = seatingRepository.getAllSeatsByRoomAndSection(101, "A");
        assertEquals(1, results.size());
        assertEquals("S1", results.get(0).getSeatId());
    }

    @Test
    void testGetAllSeatsByRoom() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(sampleSeating));

        List<Seating> results = seatingRepository.getAllSeatsByRoom(101);
        assertFalse(results.isEmpty());
    }

    @Test
    void testGetSeatById() {
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(sampleSeating);

        Seating seat = seatingRepository.getSeatById("S1");
        assertNotNull(seat);
        assertEquals("S1", seat.getSeatId());
    }

    @Test
    void testUpdate() {
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        int result = seatingRepository.update(sampleSeating);
        assertEquals(1, result);
    }

    @Test
    void testDelete() {
        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);
        int result = seatingRepository.delete(1);
        assertEquals(1, result);
    }

    @Test
    void testFindByRoomId() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(sampleSeating));

        List<Seating> results = seatingRepository.findByRoomId(101);
        assertEquals(1, results.size());
    }

    @Test
    void testUpdateSeatsAsReserved() {
        // Mock the behavior of jdbcTemplate.update() to return 1 row affected
        when(jdbcTemplate.update(anyString(), any(), any())).thenReturn(1);

        // Call the method under test
        seatingRepository.updateSeatsAsReserved(Arrays.asList("S1", "S2"), 123);

        // Verify that the update method was called with the correct arguments
        verify(jdbcTemplate).update(
                eq("INSERT INTO SeatEvents (seat_id, event_id, reserved, reservation_time) VALUES (?, ?, true, NOW()) ON DUPLICATE KEY UPDATE reserved = true, reservation_time = NOW()"),
                eq("S1"),
                eq(123)
        );
        verify(jdbcTemplate).update(
                eq("INSERT INTO SeatEvents (seat_id, event_id, reserved, reservation_time) VALUES (?, ?, true, NOW()) ON DUPLICATE KEY UPDATE reserved = true, reservation_time = NOW()"),
                eq("S2"),
                eq(123)
        );
    }



    @Test
    void testGetReservedSeatsForEvent() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Arrays.asList("S1", "S2"));

        List<String> results = seatingRepository.getReservedSeatsForEvent(123);
        assertEquals(2, results.size());
    }

    @Test
    void testSaveSeatEventAssociation() {
        // Mock the behavior of jdbcTemplate.update() to return 1 row affected
        when(jdbcTemplate.update(anyString(), any(), any())).thenReturn(1);

        // Call the method under test
        seatingRepository.saveSeatEventAssociation(123, Arrays.asList("S1", "S2"));

        // Verify that the update method was called with the correct arguments
        verify(jdbcTemplate).update(
                eq("INSERT INTO SeatEvents (seat_id, event_id) VALUES (?, ?)"),
                eq("S1"),
                eq(123)
        );
        verify(jdbcTemplate).update(
                eq("INSERT INTO SeatEvents (seat_id, event_id) VALUES (?, ?)"),
                eq("S2"),
                eq(123)
        );
    }



    @Test
    void testGetAvailableSeatsForEvent() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(sampleSeating));

        List<Seating> results = seatingRepository.getAvailableSeatsForEvent(123);
        assertEquals(1, results.size());
    }
}
