package com.operations.StageOps.service;

import com.operations.StageOps.model.Seating;
import com.operations.StageOps.repository.SeatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing Seating entities.
 * Provides methods for CRUD operations (Create, Read, Update, Delete) on seating records.
 * The class interacts with the SeatingRepository to perform database operations.
 */
@Service
public class SeatingService {

    private final SeatingRepository seatingRepository;

    /**
     * Constructor for initializing the SeatingRepository.
     *
     * @param seatingRepository the SeatingRepository used for interacting with the database.
     */
    public SeatingService(SeatingRepository seatingRepository) {
        this.seatingRepository = seatingRepository;
    }

    /**
     * Saves a new seating record into the database.
     *
     * @param seating the Seating object containing the data to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int saveSeating(Seating seating) {
        return seatingRepository.save(seating);
    }

    /**
     * Retrieves all seating records for a specific room from the database.
     *
     * @param roomId the ID of the room for which seating is being fetched.
     * @return a list of Seating objects representing all seats in the specified room.
     */
    public List<Seating> getAllSeats(int roomId) {
        return seatingRepository.getAllSeatsByRoom(roomId);
    }

    /**
     * Retrieves a specific seating record by its ID.
     *
     * @param seatId the unique ID of the seat.
     * @return the Seating object corresponding to the given seat ID.
     */
    public Seating getSeatById(UUID seatId) {
        return seatingRepository.getSeatById(seatId);
    }

    /**
     * Updates an existing seating record in the database.
     *
     * @param seating the Seating object containing the updated data.
     * @return the number of rows affected by the update query.
     */
    public int updateSeating(Seating seating) {
        return seatingRepository.update(seating);
    }

    /**
     * Deletes a seating record from the database by its seat ID.
     *
     * @param seatId the ID of the seat to be deleted.
     * @return the number of rows affected by the delete query (usually 1 if successful).
     */
    public int deleteSeating(int seatId) {
        return seatingRepository.delete(seatId);
    }
}
