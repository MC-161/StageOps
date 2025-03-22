package com.operations.StageOps.service;

import com.operations.StageOps.model.Seating;
import com.operations.StageOps.repository.SeatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomPopulator {

    private final SeatingRepository seatingRepository;

    @Autowired
    public RoomPopulator(SeatingRepository seatingRepository) {
        this.seatingRepository = seatingRepository;
    }

    /**
     * Create seats for a given room based on room ID and capacity.
     * Checks if the seat already exists before inserting.
     * @param roomId The room ID for which to create the seats
     * @param capacity The total capacity of the room (number of seats)
     */
    public void createSeatsForRoom(int roomId, int capacity) {
        for (int i = 1; i <= capacity; i++) {
            String seatId = generateUniqueSeatId();


            // If not found, save the new seat
            seatingRepository.save(new Seating(seatId, roomId, i, false, false, ""));
        }
    }

    /**
     * Generate a unique seat ID using UUID.
     * @return A unique seat ID.
     */
    private String generateUniqueSeatId() {
        return UUID.randomUUID().toString();
    }
}

