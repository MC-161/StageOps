package com.operations.StageOps.service;


import com.operations.StageOps.model.Seating;
import com.operations.StageOps.repository.SeatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing seating operations.
 * Provides methods to create, update, delete, and retrieve seating details.
 */
@Service
public class SeatingService {

    private final SeatingRepository seatingRepository;

    /**
     * Constructor for SeatingService, injecting the SeatingRepository dependency.
     *
     * @param seatingRepository The repository responsible for seating data operations.
     */
    public SeatingService(SeatingRepository seatingRepository) {
        this.seatingRepository = seatingRepository;
    }

    /**
     * Saves a new seat.
     *
     * @param seating The Seating object to be saved.
     * @return The result of the save operation (e.g., affected rows or seat ID).
     */
    public int saveSeating(Seating seating) {
        return seatingRepository.save(seating);
    }

    /**
     * Retrieves all seats for a specific room.
     *
     * @param roomId The ID of the room whose seats are to be retrieved.
     * @return A list of Seating objects for the given room.
     */
    public List<Seating> getAllSeats(int roomId) {
        return seatingRepository.getAllSeatsByRoom(roomId);
    }

    /**
     * Retrieves a seat by its ID.
     *
     * @param seatId The ID of the seat to retrieve.
     * @return The Seating object corresponding to the provided seat ID.
     */
    public Seating getSeatById(String seatId) {
        return seatingRepository.getSeatById(seatId);
    }

    /**
     * Updates an existing seat.
     *
     * @param seating The updated Seating object containing new details.
     * @return The result of the update operation (e.g., affected rows).
     */
    public int updateSeating(Seating seating) {
        return seatingRepository.update(seating);
    }

    /**
     * Deletes a seat based on its ID.
     *
     * @param seatId The ID of the seat to be deleted.
     * @return The result of the delete operation (e.g., affected rows).
     */
    public int deleteSeating(int seatId) {
        return seatingRepository.delete(seatId);
    }
}

