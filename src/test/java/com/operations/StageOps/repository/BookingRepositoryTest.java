package com.operations.StageOps.repository;

import com.operations.StageOps.model.Booking;
import com.operations.StageOps.model.BookingRoomAssignment;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class BookingRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BookingRepository bookingRepository;

    private Booking testBooking;
    private List<BookingRoomAssignment> roomAssignments;

    @BeforeEach
    void setUp() {
        // Create test data
        ZonedDateTime startTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
        ZonedDateTime endTime = startTime.plusHours(2);

        roomAssignments = new ArrayList<>();
        roomAssignments.add(new BookingRoomAssignment(1, startTime, 101));
        roomAssignments.add(new BookingRoomAssignment(1, endTime, 102));

        testBooking = new Booking(
                1,
                100,
                startTime,
                endTime,
                "CONFIRMED",
                500.0,
                roomAssignments
        );

        // Set up common lenient stubs
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(roomAssignments);
    }

    @Test
    void save_ValidBooking_ReturnsSavedBooking() {
        // Arrange
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            Map<String, Object> keyMap = Collections.singletonMap("booking_id", 1);
            keyHolder.getKeyList().add(keyMap);
            return 1;
        });
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(0);
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Act
        Booking savedBooking = bookingRepository.save(testBooking);

        // Assert
        assertNotNull(savedBooking);
        assertEquals(testBooking.getClientId(), savedBooking.getClientId());
        assertEquals(testBooking.getStatus(), savedBooking.getStatus());
        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
    }

    @Test
    void update_ValidBooking_ReturnsUpdatedRows() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(0);

        // Act
        int updatedRows = bookingRepository.update(testBooking);

        // Assert
        assertEquals(1, updatedRows);
        verify(jdbcTemplate, atLeastOnce()).update(anyString(), any(Object[].class));
    }

    @Test
    void delete_ValidBookingId_ReturnsDeletedRows() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Act
        int deletedRows = bookingRepository.delete(1);

        // Assert
        assertEquals(1, deletedRows);
        verify(jdbcTemplate).update(anyString(), eq(1));
    }

    @Test
    void getBookingById_ValidId_ReturnsBooking() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(testBooking);

        // Act
        Booking foundBooking = bookingRepository.getBookingById(1);

        // Assert
        assertNotNull(foundBooking);
        assertEquals(testBooking.getBookingId(), foundBooking.getBookingId());
        assertEquals(testBooking.getClientId(), foundBooking.getClientId());
    }

    @Test
    void getAllBookings_ReturnsListOfBookings() {
        // Arrange
        List<Booking> expectedBookings = List.of(testBooking);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(expectedBookings);

        // Act
        List<Booking> bookings = bookingRepository.getAllBookings();

        // Assert
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(testBooking.getBookingId(), bookings.get(0).getBookingId());
    }

    @Test
    void getBookingsForRoom_ValidRoomId_ReturnsBookings() {
        // Arrange
        List<Booking> expectedBookings = List.of(testBooking);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(expectedBookings);

        // Act
        List<Booking> bookings = bookingRepository.getBookingsForRoom(101);

        // Assert
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(testBooking.getBookingId(), bookings.get(0).getBookingId());
    }

    @Test
    void getUpcomingBookings_ReturnsFutureBookings() {
        // Arrange
        List<Booking> expectedBookings = List.of(testBooking);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(expectedBookings);

        // Act
        List<Booking> bookings = bookingRepository.getUpcomingBookings();

        // Assert
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(testBooking.getBookingId(), bookings.get(0).getBookingId());
    }

    @Test
    void isRoomAvailableForBooking_RoomAvailable_ReturnsTrue() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class)))
                .thenReturn(0);

        // Act
        boolean isAvailable = bookingRepository.isRoomAvailableForBooking(
                101,
                testBooking.getStartTime(),
                testBooking.getEndTime()
        );

        // Assert
        assertTrue(isAvailable);
    }

    @Test
    void isRoomAvailableForBooking_RoomNotAvailable_ReturnsFalse() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class)))
                .thenReturn(1);

        // Act
        boolean isAvailable = bookingRepository.isRoomAvailableForBooking(
                101,
                testBooking.getStartTime(),
                testBooking.getEndTime()
        );

        // Assert
        assertFalse(isAvailable);
    }
} 