package com.operations.StageOps.service;


import com.operations.StageOps.model.Seating;
import com.operations.StageOps.repository.SeatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatingService {

    private final SeatingRepository seatingRepository;

    public SeatingService(SeatingRepository seatingRepository) {
        this.seatingRepository = seatingRepository;
    }

    // Save a new seat
    public int saveSeating(Seating seating) {
        return seatingRepository.save(seating);
    }

    // Get all seats
    public List<Seating> getAllSeats(int roomId) {
        return seatingRepository.getAllSeatsByRoom(roomId);
    }

    // Get seat by ID
    public Seating getSeatById(String seatId) {
        return seatingRepository.getSeatById(seatId);
    }

    // Update seat
    public int updateSeating(Seating seating) {
        return seatingRepository.update(seating);
    }

    // Delete seat
    public int deleteSeating(int seatId) {
        return seatingRepository.delete(seatId);
    }
}

