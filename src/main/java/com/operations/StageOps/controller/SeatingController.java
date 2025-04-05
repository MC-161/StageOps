package com.operations.StageOps.controller;

import com.operations.StageOps.model.Seating;
import com.operations.StageOps.service.SeatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing seating-related operations.
 */
@RestController
@RequestMapping("/api/seating")
public class SeatingController {

    private final SeatingService seatingService;

    /**
     * Constructor for SeatingController.
     *
     * @param seatingService The service handling seating operations.
     */
    public SeatingController(SeatingService seatingService) {
        this.seatingService = seatingService;
    }

    /**
     * Saves a new seating entry.
     *
     * @param seating The seating object to be saved.
     * @return A response entity indicating the success or failure of the operation.
     */
    @PostMapping
    public ResponseEntity<String> saveSeating(@RequestBody Seating seating) {
        int result = seatingService.saveSeating(seating);
        if (result > 0) {
            return new ResponseEntity<>("Seating saved successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Failed to save seating", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Generates a new seating entry.
     *
     * @param roomId The ID of the room for which the seating is to be generated.
     * @return A response entity indicating the success or failure of the operation.
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Seating>> getAllSeats(@PathVariable int roomId) {
        List<Seating> seats = seatingService.getAllSeats(roomId);
        return new ResponseEntity<>(seats, HttpStatus.OK);
    }

    /**
     * Retrieves a seat by its ID.
     *
     * @param seatId The ID of the seat.
     * @return The seating object if found, or a NOT_FOUND status if not found.
     */
    @GetMapping("/{seatId}")
    public ResponseEntity<Seating> getSeatById(@PathVariable String seatId) {
        Seating seat = seatingService.getSeatById(seatId);
        if (seat != null) {
            return new ResponseEntity<>(seat, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Updates a seating entry.
     *
     * @param seatId The ID of the seating entry to be updated.
     * @param seating The updated seating object.
     * @return A response entity indicating the success or failure of the operation.
     */
    @PutMapping("/{seatId}")
    public ResponseEntity<String> updateSeating(@PathVariable String seatId, @RequestBody Seating seating) {
        seating.setSeatId(seatId);  // Assuming that seatId is a field in the Seating object
        int result = seatingService.updateSeating(seating);
        if (result > 0) {
            return new ResponseEntity<>("Seating updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to update seating", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Deletes a seating entry.
     *
     * @param seatId The ID of the seating entry to be deleted.
     * @return A response entity indicating the success or failure of the operation.
     */
    @DeleteMapping("/{seatId}")
    public ResponseEntity<String> deleteSeating(@PathVariable int seatId) {
        int result = seatingService.deleteSeating(seatId);
        if (result > 0) {
            return new ResponseEntity<>("Seating deleted successfully", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Failed to delete seating", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
