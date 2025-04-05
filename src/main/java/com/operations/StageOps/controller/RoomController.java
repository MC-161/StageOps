package com.operations.StageOps.controller;

import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Room;
import com.operations.StageOps.service.LayoutService;
import com.operations.StageOps.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Controller class for managing room-related operations.
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final LayoutService layoutService;

    /**
     * Constructor for RoomController.
     *
     * @param roomService   The service handling room operations.
     * @param layoutService The service handling layout operations.
     */
    @Autowired
    public RoomController(RoomService roomService, LayoutService layoutService) {
        this.roomService = roomService;
        this.layoutService = layoutService;
    }

    /**
     * Creates a new room.
     *
     * @param room The room object to be created.
     * @return A success or error message.
     */
    @PostMapping
    public String createRoom(@RequestBody Room room) {
        int result = roomService.saveRoom(room);
        return result > 0 ? "Room created successfully!" : "Error creating Room!";
    }

    /**
     * Retrieves all layouts associated with a specific room.
     *
     * @param roomId The ID of the room.
     * @return A list of layout configurations for the room.
     */
    @GetMapping("/{roomId}/layouts")
    public List<LayoutConfiguration> getLayoutsForRoom(@PathVariable int roomId) {
        return layoutService.getLayoutsForRoom(roomId);
    }

    /**
     * Retrieves a specific room by ID.
     *
     * @param roomId The ID of the room.
     * @return The room object.
     */
    @GetMapping("/{roomId}")
    public Room getRoom(@PathVariable int roomId) {
        return roomService.getRoomById(roomId);
    }

    /**
     * Retrieves all rooms.
     *
     * @return A list of all rooms.
     */
    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    /**
     * Updates a specific room.
     *
     * @param roomId The ID of the room to be updated.
     * @param room   The updated room object.
     * @return A success or error message.
     */
    @PutMapping("/{roomId}")
    public String updateRoom(@PathVariable int roomId, @RequestBody Room room) {
        int result = roomService.updateRoom(roomId, room);
        return result > 0 ? "Room updated successfully!" : "Error Updating Room!";
    }

    /**
     * Deletes a specific room.
     *
     * @param roomId The ID of the room to be deleted.
     */
    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable int roomId) {
        roomService.deleteRoom(roomId);
    }

    /**
     * Checks if a room is available for a given time period.
     *
     * @param roomId         The ID of the room.
     * @param startDate      The start date of the time period.
     * @param endDate        The end date of the time period.
     * @param eventStartTime The start time of the event.
     * @param eventEndTime   The end time of the event.
     * @return A boolean indicating whether the room is available.
     */
    @GetMapping("/{roomId}/availability")
    public ResponseEntity<Boolean> checkRoomAvailability(
            @PathVariable int roomId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam ZonedDateTime eventStartTime,
            @RequestParam ZonedDateTime eventEndTime
    ) {
        boolean isAvailable = roomService.isRoomAvailableForTimePeriod(roomId, startDate, endDate, eventStartTime, eventEndTime);
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * Retrieves a list of available rooms for a given time period.
     *
     * @param startDate      The start date of the time period.
     * @param endDate        The end date of the time period.
     * @param eventStartTime The start time of the event.
     * @param eventEndTime   The end time of the event.
     * @return A list of available room IDs.
     */
    @GetMapping("/available")
    public ResponseEntity<List<Integer>> getAvailableRooms(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam ZonedDateTime eventStartTime,
            @RequestParam ZonedDateTime eventEndTime
    ) {
        List<Integer> availableRooms = roomService.getAvailableRooms(startDate, endDate, eventStartTime, eventEndTime);
        return ResponseEntity.ok(availableRooms);
    }
}

