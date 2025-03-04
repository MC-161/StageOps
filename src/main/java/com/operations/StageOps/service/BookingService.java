package com.operations.StageOps.service;

import com.operations.StageOps.model.Booking;
import com.operations.StageOps.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Booking entities.
 * It provides methods for CRUD operations (Create, Read, Update, Delete) on bookings.
 * The class interacts with the `BookingRepository` for database operations.
 */
@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    /**
     * Constructor for initializing the BookingRepository.
     *
     * @param bookingRepository the BookingRepository used for interacting with the database.
     */
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Saves a new booking record into the database.
     *
     * @param booking the Booking object containing the data to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    /**
     * Retrieves all bookings from the database.
     *
     * @return a list of Booking objects representing all bookings in the database.
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.getAllBookings();
    }

    /**
     * Retrieves a specific booking by its ID from the database.
     *
     * @param bookingId the ID of the booking to retrieve.
     * @return the Booking object corresponding to the given booking ID.
     */
    public Booking getBookingById(int bookingId) {
        return bookingRepository.getBookingById(bookingId);
    }

    /**
     * Updates an existing booking record in the database.
     *
     * @param booking the Booking object containing the updated data.
     * @return the number of rows affected by the update query.
     */
    public int updateBooking(Booking booking) {
        return bookingRepository.update(booking);
    }

    /**
     * Deletes a booking record from the database based on the booking ID.
     *
     * @param bookingId the ID of the booking to be deleted.
     * @return the number of rows affected by the delete query (usually 1 if successful).
     */
    public int deleteBooking(int bookingId) {
        return bookingRepository.delete(bookingId);
    }

    /**
     * Retrieves all bookings for a specific room.
     *
     * @param roomId the ID of the room for which bookings are to be fetched.
     * @return a list of Booking objects associated with the given room ID.
     */
    public List<Booking> getBookingsForRoom(String roomId) {
        return bookingRepository.getBookingsForRoom(roomId);
    }
}
