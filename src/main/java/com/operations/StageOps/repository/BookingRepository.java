package com.operations.StageOps.repository;

import com.operations.StageOps.model.Booking;
import com.operations.StageOps.model.BookingRoomAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(BookingRepository.class);

    public BookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a booking to the database within a transaction.
     * Ensures that the booking data is valid and that the rooms are available before inserting the booking.
     *
     * @param booking The booking object containing client details, time range, status, and room assignments.
     * @return The generated booking ID if successful.
     * @throws IllegalArgumentException If start or end time is null.
     * @throws IllegalStateException If any assigned room is unavailable or booking ID retrieval fails.
     * @throws RuntimeException If any unexpected error occurs during the transaction.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Booking save(Booking booking) {
        try {
            // Validate booking data
            if (booking.getStartTime() == null || booking.getEndTime() == null) {
                throw new IllegalArgumentException("Start and end times cannot be null.");
            }

            // Validate room availability
            for (BookingRoomAssignment roomAssignment : booking.getRoomAssignments()) {
                if (!isRoomAvailableForBooking(roomAssignment.getRoomId(), booking.getStartTime(), booking.getEndTime())) {
                    throw new IllegalStateException("The room " + roomAssignment.getRoomId() + " is not available.");
                }
            }

            // Start Transaction
            String sql = "INSERT INTO bookings (client_id, start_time, end_time, status, total_cost) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            int rowsAffected = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, booking.getClientId());
                ps.setTimestamp(2, Timestamp.from(booking.getStartTime().toInstant()));
                ps.setTimestamp(3, Timestamp.from(booking.getEndTime().toInstant()));
                ps.setString(4, booking.getStatus());
                ps.setDouble(5, booking.getTotalCost());
                return ps;
            }, keyHolder);


            if (rowsAffected > 0 && keyHolder.getKey() != null) {
                int generatedBookingId = keyHolder.getKey().intValue();

                // Insert room assignments inside the transaction, one for each room with the corresponding date
                List<BookingRoomAssignment> roomAssignments = booking.getRoomAssignments();
                int numRooms = roomAssignments.size();
                LocalDate startDate = booking.getStartTime().toLocalDate();
                LocalDate endDate = booking.getEndTime().toLocalDate();

                // Iterate over each room assignment and assign them to either start or end date
                for (int i = 0; i < numRooms; i++) {
                    BookingRoomAssignment roomAssignment = roomAssignments.get(i);
                    booking.setBookingId(generatedBookingId);
                    // Determine which date to assign the room based on the index in the list
                    LocalDateTime assignmentDateTime;
                    if (i == 0) {  // First room in the list gets the start date
                        assignmentDateTime = startDate.atStartOfDay().plusHours(1); // Use start date for the first room
                    } else {  // Subsequent rooms get the end date
                        assignmentDateTime = endDate.atStartOfDay().plusHours(1); // Use end date for the following rooms
                    }

                    Timestamp assignmentDate = Timestamp.valueOf(assignmentDateTime);

                    // Log the adjusted assignment date
                    logger.info("Creating room assignment for room: " + roomAssignment.getRoomId() + " on date: " + assignmentDateTime);

                    String insertSql = "INSERT INTO booking_room_assignments (booking_id, date, room_id) " +
                            "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE booking_id = booking_id";
                    jdbcTemplate.update(insertSql, generatedBookingId, assignmentDate, roomAssignment.getRoomId());
                }

                // ✅ Add revenue tracking after room assignments are added
                if (!booking.getRoomAssignments().isEmpty()) {
                    updateRevenueTracking(
                            booking.getRoomAssignments().get(0).getRoomId(), // Using first room for revenue tracking
                            generatedBookingId,
                            booking.getTotalCost(),
                            0, // Default ticket sales
                            0  // Default venue hire
                    );
                }

                return booking;
            } else {
                throw new IllegalStateException("Failed to retrieve the generated booking ID.");
            }
        } catch (Exception e) {
            logger.error("Error while saving booking", e);
            throw new RuntimeException("An error occurred while saving the booking. Please try again later.", e);
        }
    }




    /**
     * Updates an existing booking in the database.
     *
     * @param booking The updated booking object containing the new details.
     * @return The number of rows affected by the update operation.
     * @throws IllegalArgumentException If the booking ID, start time, or end time is null.
     * @throws RuntimeException If a database error occurs during the update.
     */
    public int update(Booking booking) {
        try {
            // Validate booking data
            if (booking.getStartTime() == null || booking.getEndTime() == null) {
                throw new IllegalArgumentException("Start and end times cannot be null.");
            }

            // Validate room availability for the updated booking dates and room assignments
            for (BookingRoomAssignment roomAssignment : booking.getRoomAssignments()) {
                if (!isRoomAvailableForBooking(roomAssignment.getRoomId(), booking.getStartTime(), booking.getEndTime())) {
                    throw new IllegalStateException("The room " + roomAssignment.getRoomId() + " is not available for the selected dates.");
                }
            }

            // 1. Update the booking details (but do not update the room_id here)
            String sql = "UPDATE bookings SET client_id = ?, start_time = ?, end_time = ?, status = ?, total_cost = ? WHERE booking_id = ?";

            int rowsAffected = jdbcTemplate.update(sql,
                    booking.getClientId(),
                    Timestamp.from(booking.getStartTime().toInstant()),  // Convert ZonedDateTime to Timestamp
                    Timestamp.from(booking.getEndTime().toInstant()),    // Convert ZonedDateTime to Timestamp
                    booking.getStatus(),
                    booking.getTotalCost(),
                    booking.getBookingId() // Ensure bookingId is included for the WHERE clause
            );

            // If booking update was successful, update the room assignments
            if (rowsAffected > 0) {
                // 2. First, delete the existing room assignments
                deleteExistingRoomAssignments(booking.getBookingId());

                // 3. Insert the updated room assignments for the booking
                insertRoomAssignments(booking.getBookingId(), booking);

                // 4. Update revenue tracking, assuming room assignments are included in revenue calculation
                // You can use the first room from the assignment list if necessary or modify the logic
                if (!booking.getRoomAssignments().isEmpty()) {
                    updateRevenueTracking(booking.getRoomAssignments().get(0).getRoomId(), booking.getBookingId(), booking.getTotalCost(), 0, 0);
                }
            }

            return rowsAffected;

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Log validation errors
            logger.error("Validation error while updating booking: {}", e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            // Log database errors
            logger.error("Database error while updating booking", e);
            throw new RuntimeException("An error occurred while updating the booking. Please try again later.", e);
        } catch (Exception e) {
            // Catch-all for unexpected errors
            logger.error("Unexpected error while updating booking", e);
            throw new RuntimeException("An unexpected error occurred. Please contact support.", e);
        }
    }


    private void updateRevenueTracking(int roomId, Integer bookingId, double totalRevenue, double ticketSales, double venueHire) {
        String sql = "INSERT INTO revenue_tracking (room_id, booking_id, total_revenue, ticket_sales, venue_hire) VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE total_revenue = total_revenue + VALUES(total_revenue), ticket_sales = ticket_sales + VALUES(ticket_sales), venue_hire = venue_hire + VALUES(venue_hire)";
        jdbcTemplate.update(sql, roomId, bookingId, totalRevenue, ticketSales, venueHire);
    }



    /**
     * Deletes the existing room assignments for a booking.
     *
     * @param bookingId The ID of the booking whose room assignments are to be deleted.
     */
    private void deleteExistingRoomAssignments(int bookingId) {
        String sql = "DELETE FROM booking_room_assignments WHERE booking_id = ?";
        jdbcTemplate.update(sql, bookingId);
    }

    /**
     * Inserts room assignments for the given booking into the booking_room_assignments table.
     *
     * @param bookingId The ID of the booking.
     * @param booking The booking object containing the room assignments and details.
     */
    private void insertRoomAssignments(int bookingId, Booking booking) {
        String sql = "INSERT INTO booking_room_assignments (booking_id, date, room_id) VALUES (?, ?, ?)";

        // Iterate through the room assignments and insert each one
        for (BookingRoomAssignment roomAssignment : booking.getRoomAssignments()) {
            // Insert each room assignment for the booking
            jdbcTemplate.update(sql,
                    bookingId,
                    Timestamp.from(roomAssignment.getDateTime().toInstant()),  // Convert LocalDate to Timestamp
                    roomAssignment.getRoomId() // Room ID from the room assignment
            );
        }
    }


    /**
     * Checks if the room is available for the specified booking time range.
     *
     * @param roomId The room ID to check availability for.
     * @param startTime The start time of the booking.
     * @param endTime The end time of the booking.
     * @return True if the room is available, false if not.
     * @throws DataAccessException If an error occurs during the database query.
     */
    public boolean isRoomAvailableForBooking(int roomId, ZonedDateTime startTime, ZonedDateTime endTime) {
        // Convert ZonedDateTime to Timestamp (be sure the time zones match what the DB expects)
        Timestamp sqlStartTime = Timestamp.from(startTime.toInstant());
        Timestamp sqlEndTime = Timestamp.from(endTime.toInstant());

        // SQL query to check if the room is already assigned for the given time range
        String sql = "SELECT COUNT(*) FROM booking_room_assignments WHERE room_id = ? AND date BETWEEN ? AND ?";

        try {
            // Query the database for any overlapping room assignments
            int count = jdbcTemplate.queryForObject(sql, new Object[]{roomId, sqlStartTime, sqlEndTime}, Integer.class);
            return count == 0;  // Return true if no conflicting room assignments are found
        } catch (DataAccessException e) {
            // Log the exception and rethrow if needed
            logger.error("Error checking room availability for roomId: " + roomId, e);
            throw e;  // Rethrow or handle as appropriate
        }
    }


    /**
     * Retrieves all bookings from the database.
     *
     * @return A list of all bookings, including room assignments.
     */
    public List<Booking> getAllBookings() {
        String sql = "SELECT * FROM bookings";
        String roomAssignmentsSql = "SELECT * FROM booking_room_assignments WHERE booking_id = ?";  // SQL to fetch room assignments for a specific booking

        try {
            List<Booking> bookings = jdbcTemplate.query(sql, (rs, bookingRowNum) -> { // Renaming the rowNum variable here
                // Retrieve the Timestamp values from the ResultSet
                Timestamp startTime = rs.getTimestamp("start_time");
                Timestamp endTime = rs.getTimestamp("end_time");

                // Convert Timestamp to ZonedDateTime
                ZonedDateTime startZonedDateTime = startTime.toInstant().atZone(ZoneId.systemDefault());
                ZonedDateTime endZonedDateTime = endTime.toInstant().atZone(ZoneId.systemDefault());

                // Retrieve booking details
                int bookingId = rs.getInt("booking_id");
                int clientId = rs.getInt("client_id");
                String status = rs.getString("status");
                double totalCost = rs.getDouble("total_cost");

                // Fetch the room assignments for the current booking
                List<BookingRoomAssignment> roomAssignments = jdbcTemplate.query(roomAssignmentsSql, new Object[]{bookingId}, (roomRs, roomRowNum) -> { // Renaming rowNum again here
                    // Retrieve the timestamp from the database and convert to ZonedDateTime
                    Timestamp assignmentTimestamp = roomRs.getTimestamp("date"); // Assuming the column is of type timestamp
                    ZonedDateTime assignmentDateTime = assignmentTimestamp.toInstant().atZone(ZoneId.systemDefault());  // Convert to ZonedDateTime

                    int roomId = roomRs.getInt("room_id");

                    // Return a BookingRoomAssignment object
                    return new BookingRoomAssignment(bookingId, assignmentDateTime, roomId);
                });

                // Return a new Booking object with the mapped values
                return new Booking(
                        bookingId,
                        clientId,
                        startZonedDateTime,
                        endZonedDateTime,
                        status,
                        totalCost,
                        roomAssignments // Add room assignments to the Booking object
                );
            });

            return bookings;

        } catch (DataAccessException e) {
            // Log the exception and rethrow
            logger.error("Error retrieving all bookings", e);
            throw e;  // Rethrow or handle as appropriate
        }
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId The ID of the booking to retrieve.
     * @return The booking with the specified ID, or null if no booking is found with that ID.
     * @throws DataAccessException If an error occurs during the database query.
     */
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT booking_id, client_id, start_time, end_time, status, total_cost FROM bookings WHERE booking_id = ?";
        String roomAssignmentsSql = "SELECT room_id, date FROM booking_room_assignments WHERE booking_id = ?";

        try {
            // Query the database for the booking details
            Booking booking = jdbcTemplate.queryForObject(sql, new Object[]{bookingId}, (rs, rowNum) -> {
                // Retrieve the Timestamp values from the ResultSet
                Timestamp startTime = rs.getTimestamp("start_time");
                Timestamp endTime = rs.getTimestamp("end_time");

                // Convert Timestamp to ZonedDateTime
                ZonedDateTime startZonedDateTime = startTime.toInstant().atZone(ZoneId.systemDefault());
                ZonedDateTime endZonedDateTime = endTime.toInstant().atZone(ZoneId.systemDefault());

                // Return a new Booking object with the mapped values, excluding the room_id here
                return new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("client_id"),
                        startZonedDateTime,
                        endZonedDateTime,
                        rs.getString("status"),
                        rs.getDouble("total_cost"),
                        new ArrayList<>() // Initialize an empty room assignments list (it will be populated later)
                );
            });

            // Query the database for the room assignments related to the booking
            List<BookingRoomAssignment> roomAssignments = jdbcTemplate.query(roomAssignmentsSql, new Object[]{bookingId}, (rs, rowNum) -> {
                // Retrieve the timestamp from the ResultSet
                Timestamp assignmentTimestamp = rs.getTimestamp("date");
                ZonedDateTime assignmentDateTime = assignmentTimestamp.toInstant().atZone(ZoneId.systemDefault());

                // Retrieve the room_id from the room assignments
                int roomId = rs.getInt("room_id");

                // Return a new BookingRoomAssignment object with bookingId, dateTime, and roomId
                return new BookingRoomAssignment(bookingId, assignmentDateTime, roomId);
            });

            // Set the room assignments in the booking object
            booking.setRoomAssignments(roomAssignments);

            return booking;

        } catch (EmptyResultDataAccessException e) {
            // Log the exception if no result is found, or return null if it's expected behavior
            logger.warn("Booking not found with ID: {}", bookingId);
            return null;  // Return null or throw a custom exception as needed
        } catch (DataAccessException e) {
            // Log and rethrow any database access errors
            logger.error("Error retrieving booking with ID: {}", bookingId, e);
            throw e;
        }
    }


    /**
     * Deletes a booking by its ID.
     *
     * @param bookingId The ID of the booking to delete.
     * @return The number of rows affected by the delete operation.
     */
    public int delete(int bookingId) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        return jdbcTemplate.update(sql, bookingId);
    }


    /**
     * Retrieves all bookings for a specific room using the room assignments table.
     *
     * @param roomId The room ID to retrieve bookings for.
     * @return A list of bookings for the specified room.
     */
    public List<Booking> getBookingsForRoom(int roomId) {
        // SQL query to fetch all bookings for the specified room_id using booking_room_assignments
        String sql = "SELECT b.booking_id, b.client_id, b.room_id, b.start_time, b.end_time, b.status, b.total_cost " +
                "FROM bookings b " +
                "JOIN booking_room_assignments bra ON b.booking_id = bra.booking_id " +
                "WHERE bra.room_id = ?";

        return jdbcTemplate.query(sql, new Object[]{roomId}, (rs, rowNum) -> {
            // Retrieve the Timestamp values from the ResultSet
            Timestamp startTime = rs.getTimestamp("start_time");
            Timestamp endTime = rs.getTimestamp("end_time");

            // Convert Timestamp to ZonedDateTime
            ZonedDateTime startZonedDateTime = startTime.toInstant().atZone(ZoneId.systemDefault());
            ZonedDateTime endZonedDateTime = endTime.toInstant().atZone(ZoneId.systemDefault());

            // Return a new Booking object with the mapped values
            return new Booking(
                    rs.getInt("booking_id"),
                    rs.getInt("client_id"),
                    startZonedDateTime,
                    endZonedDateTime,
                    rs.getString("status"),
                    rs.getDouble("total_cost"),
                    new ArrayList<>()  // Initialize room assignments list (it can be populated later if needed)
            );
        });
    }


    /**
     * Retrieves all upcoming bookings (i.e., bookings with a start time greater than the current date).
     *
     * @return A list of upcoming bookings.
     */
    public List<Booking> getUpcomingBookings() {
        ZonedDateTime currentDate = ZonedDateTime.now();
        Timestamp currentTimestamp = Timestamp.from(currentDate.toInstant());

        // SQL query to retrieve bookings where the start time is after the current date
        // We'll fetch booking details and later fetch room assignments separately
        String sql = "SELECT booking_id, client_id, start_time, end_time, status, total_cost " +
                "FROM bookings " +
                "WHERE start_time > ?";

        List<Booking> bookings = jdbcTemplate.query(sql, new Object[]{currentTimestamp}, (rs, rowNum) -> {
            // Retrieve the Timestamp values from the ResultSet
            Timestamp startTime = rs.getTimestamp("start_time");
            Timestamp endTime = rs.getTimestamp("end_time");

            // Convert Timestamp to ZonedDateTime
            ZonedDateTime startZonedDateTime = startTime.toInstant().atZone(ZoneId.systemDefault());
            ZonedDateTime endZonedDateTime = endTime.toInstant().atZone(ZoneId.systemDefault());

            // Return a new Booking object with the mapped values, no room_id here
            return new Booking(
                    rs.getInt("booking_id"),
                    rs.getInt("client_id"),
                    startZonedDateTime,
                    endZonedDateTime,
                    rs.getString("status"),
                    rs.getDouble("total_cost"),
                    new ArrayList<>()  // Initialize room assignments list (it will be populated later)
            );
        });

        // For each booking, retrieve the room assignments (using booking_id)
        for (Booking booking : bookings) {
            String roomAssignmentsSql = "SELECT room_id, date FROM booking_room_assignments WHERE booking_id = ?";

            List<BookingRoomAssignment> roomAssignments = jdbcTemplate.query(roomAssignmentsSql, new Object[]{booking.getBookingId()}, (rs, rowNum) -> {
                // Retrieve the timestamp from the ResultSet
                Timestamp assignmentTimestamp = rs.getTimestamp("date");
                ZonedDateTime assignmentDateTime = assignmentTimestamp.toInstant().atZone(ZoneId.systemDefault());

                // Retrieve the room_id
                int roomId = rs.getInt("room_id");

                // Return a new BookingRoomAssignment object
                return new BookingRoomAssignment(booking.getBookingId(), assignmentDateTime, roomId);
            });

            // Set the room assignments in the booking object
            booking.setRoomAssignments(roomAssignments);
        }

        return bookings;
    }

}
