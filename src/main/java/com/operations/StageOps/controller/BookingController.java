package com.operations.StageOps.controller;

import com.operations.StageOps.model.Booking;
import com.operations.StageOps.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is the REST controller for managing bookings in the system.
 * It handles requests related to creating, updating, retrieving, and deleting bookings.
 * It interacts with the {@link BookingService} to perform the necessary operations.
 */
@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Constructor for the BookingController class.
     * Initializes the controller with a BookingService instance.
     *
     * @param bookingService The service used to manage bookings
     */
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Creates a new booking.
     * This method receives a {@link Booking} object in the request body,
     * passes it to the {@link BookingService}, and returns a success or error message.
     *
     * @param booking The booking details to be saved
     * @return A message indicating whether the booking was created successfully
     */
    @PostMapping("/create")
    public String createBooking(@RequestBody Booking booking) {
        int result = bookingService.saveBooking(booking);
        return result > 0 ? "Booking created successfully!" : "Error creating booking!";
    }


    /**
     * Retrieves all bookings.
     * This method calls the {@link BookingService} to fetch all bookings from the database.
     *
     * @return A list of all bookings
     */
    @GetMapping("/all")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    /**
     * Retrieves a booking by its ID.
     * This method uses the booking ID passed in the URL path to fetch the corresponding booking.
     *
     * @param bookingId The ID of the booking to retrieve
     * @return The {@link Booking} object with the specified ID, or null if not found
     */
    @GetMapping("/{bookingId}")
    public Booking getBookingById(@PathVariable int bookingId) {
        return bookingService.getBookingById(bookingId);
    }

    /**
     * Updates an existing booking.
     * This method receives a {@link Booking} object in the request body and updates the corresponding booking.
     *
     * @param booking The updated booking details
     * @return A message indicating whether the booking was updated successfully
     */
    @PutMapping("/update")
    public String updateBooking(@RequestBody Booking booking) {
        int result = bookingService.updateBooking(booking);
        return result > 0 ? "Booking updated successfully!" : "Error updating booking!";
    }

    /**
     * Deletes a booking by its ID.
     * This method deletes the booking with the specified ID by calling the {@link BookingService}.
     *
     * @param bookingId The ID of the booking to delete
     * @return A message indicating whether the booking was deleted successfully
     */
    @DeleteMapping("/delete/{bookingId}")
    public String deleteBooking(@PathVariable int bookingId) {
        int result = bookingService.deleteBooking(bookingId);
        return result > 0 ? "Booking deleted successfully!" : "Error deleting booking!";
    }

    /**
     * Retrieves a list of upcoming bookings.
     *
     * @return A ResponseEntity containing a list of upcoming bookings.
     *         - Returns HTTP 200 (OK) with the list of bookings if found.
     *         - Returns HTTP 204 (No Content) if no upcoming bookings are available.
     */
    // Endpoint to get upcoming bookings
    @GetMapping("/upcoming")
    public ResponseEntity<List<Booking>> getUpcomingBookings() {
        List<Booking> upcomingBookings = bookingService.getUpcomingBookings();
        if (upcomingBookings.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no bookings are found
        }
        return ResponseEntity.ok(upcomingBookings); // Return 200 with bookings if found
    }
}
