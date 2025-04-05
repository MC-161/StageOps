package com.operations.StageOps.service;


import com.operations.StageOps.model.Booking;
import com.operations.StageOps.model.Invoice;
import com.operations.StageOps.repository.BookingRepository;
import com.operations.StageOps.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service class for managing bookings.
 * It provides methods to save, retrieve, update, and delete bookings.
 */
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private InvoiceRepository invoiceRepository;


    /**
     * Constructor to initialize BookingService with a BookingRepository.
     *
     * @param bookingRepository The repository to interact with the booking data.
     */
    @Autowired
    public BookingService(BookingRepository bookingRepository, InvoiceRepository invoiceRepository) {
        this.bookingRepository = bookingRepository;
        this.invoiceRepository = invoiceRepository;
    }


    /**
     * Saves a new booking and automatically creates an invoice for it.
     *
     * @param booking The booking object to be saved.
     * @return The ID of the saved booking.
     */
    @Transactional
    public int saveBooking(Booking booking) {
        // Step 1: Save the booking
        Booking savedBooking = bookingRepository.save(booking);

        int bookingId = savedBooking.getBookingId();

        // Step 2: Create an invoice for this booking
        Invoice invoice = new Invoice();
        invoice.setBookingId(bookingId);  // Link invoice to the booking
        invoice.setClientId(booking.getClientId());
        invoice.setTotalAmount(booking.getTotalCost()); // Set the total cost from booking
        invoice.setStatus("pending");  // Default invoice status
        invoice.setIssueDate(ZonedDateTime.now());  // Current date and time
        invoice.setDueDate(booking.getEndTime().plusDays(7));  // Due in 7 days after booking end

        // Step 3: Save the invoice
        invoiceRepository.createInvoice(invoice);

        return bookingId;
    }

    /**
     * Retrieves all bookings from the repository.
     *
     * @return A list of all bookings.
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.getAllBookings();
    }

    /**
     * Retrieves a booking by its ID from the repository.
     *
     * @param bookingId The ID of the booking to be retrieved.
     * @return The booking object with the given ID.
     */
    public Booking getBookingById(int bookingId) {
        return bookingRepository.getBookingById(bookingId);
    }


    /**
     * Updates an existing booking in the repository.
     *
     * @param booking The booking object to be updated.
     * @return The number of rows affected by the update operation.
     */
    public int updateBooking(Booking booking) {
        return bookingRepository.update(booking);
    }


    /**
     * Deletes a booking from the repository.
     *
     * @param bookingId The ID of the booking to be deleted.
     * @return The number of rows affected by the delete operation.
     */
    public int deleteBooking(int bookingId) {
        return bookingRepository.delete(bookingId);
    }

    /**
     * Retrieves all bookings for a room from the repository.
     *
     * @param roomId The ID of the room for which bookings are to be retrieved.
     * @return A list of all bookings for the given room.
     */
    public List<Booking> getBookingsForRoom(int roomId) {
        return bookingRepository.getBookingsForRoom(roomId);
    }

    /**
     * Retrieves all upcoming bookings from the repository.
     *
     * @return A list of all upcoming bookings.
     */
    public List<Booking> getUpcomingBookings() {
        return bookingRepository.getUpcomingBookings();
    }
}
