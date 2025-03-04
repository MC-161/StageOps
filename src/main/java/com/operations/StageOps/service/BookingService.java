package com.operations.StageOps.service;


import com.operations.StageOps.model.Booking;
import com.operations.StageOps.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Save a new booking
    public int saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.getAllBookings();
    }

    // Get booking by ID
    public Booking getBookingById(int bookingId) {
        return bookingRepository.getBookingById(bookingId);
    }

    // Update booking
    public int updateBooking(Booking booking) {
        return bookingRepository.update(booking);
    }

    // Delete booking
    public int deleteBooking(int bookingId) {
        return bookingRepository.delete(bookingId);
    }

    public List<Booking> getBookingsForRoom(String roomId) {
        return bookingRepository.getBookingsForRoom(roomId);
    }
}
