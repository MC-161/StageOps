package com.operations.StageOps.repository;

import com.operations.StageOps.model.Event;
import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Seating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SeatingRepository seatingRepository;

    @InjectMocks
    private EventRepository eventRepository;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Event();
        testEvent.setEventId(1);
        testEvent.setEventName("Test Event");
        testEvent.setEventDate(Date.valueOf(LocalDate.now()));
        testEvent.setStartTime(ZonedDateTime.now());
        testEvent.setEndTime(ZonedDateTime.now().plusHours(2));
        testEvent.setRoomId(101);
        testEvent.setTicketsAvailable(100);
        testEvent.setTicketsSold(20);
        testEvent.setEventType("Music");
        testEvent.setTotalRevenue(5000.0);
        testEvent.setLayoutId(1);
        testEvent.setTicketPrice(50.0);
        testEvent.setMaxDiscount(10.0);
        testEvent.setClientId(200);
    }

    @Test
    void save_ValidEvent_SuccessfulInsert() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(0); // room is available
        when(jdbcTemplate.query(eq("SELECT * FROM layouts WHERE room_id = ?"), any(Object[].class), any(RowMapper.class)))
                .thenReturn(List.of(new LayoutConfiguration(1, "Standard", 200, 101, "Theater"))); // valid layout
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            ((GeneratedKeyHolder) keyHolder).getKeyList().add(Collections.singletonMap("GENERATED_KEY", 1));
            return 1;
        });
        when(seatingRepository.getAllSeatsByRoom(anyInt())).thenReturn(List.of(new Seating("A1", 101, 1, false, false, "Main")));
        doNothing().when(seatingRepository).saveSeatEventAssociation(anyInt(), anyList());

        // Act
        Event result = eventRepository.save(testEvent);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getEventId());
    }

    @Test
    void save_InvalidRoom_ThrowsException() {
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(1); // room not available

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> eventRepository.save(testEvent));
        assertEquals("The room is not available for the selected event time.", exception.getMessage());
    }

    @Test
    void save_InvalidLayout_ThrowsException() {
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(0); // room is available
        when(jdbcTemplate.query(
                eq("SELECT * FROM layouts WHERE room_id = ?"),
                any(Object[].class),
                any(RowMapper.class))
        ).thenReturn(Collections.emptyList()); // No layout found → should trigger IllegalArgumentException
        // no layouts

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> eventRepository.save(testEvent));
        assertEquals("The selected layout is not associated with the specified room.", exception.getMessage());
    }

    @Test
    void delete_ValidId_DeletesEvent() {
        when(jdbcTemplate.update(anyString(), eq(1))).thenReturn(1);
        int result = eventRepository.delete(1);
        assertEquals(1, result);
    }

    @Test
    void getEventById_ReturnsEvent() {
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(testEvent);
        Event result = eventRepository.getEventById(1);
        assertNotNull(result);
        assertEquals("Test Event", result.getEventName());
    }

    @Test
    void isRoomAvailableForEvent_ReturnsTrueWhenAvailable() {
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(0);
        boolean available = eventRepository.isRoomAvailableForEvent(101, testEvent.getStartTime(), testEvent.getEndTime());
        assertTrue(available);
    }

    @Test
    void isRoomAvailableForEvent_ReturnsFalseWhenNotAvailable() {
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(1);
        boolean available = eventRepository.isRoomAvailableForEvent(101, testEvent.getStartTime(), testEvent.getEndTime());
        assertFalse(available);
    }
}
