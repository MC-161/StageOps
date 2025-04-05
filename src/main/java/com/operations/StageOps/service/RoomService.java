package com.operations.StageOps.service;


import com.operations.StageOps.model.Room;
import com.operations.StageOps.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service class for managing room operations.
 * Provides methods to create, update, delete, and check availability of rooms.
 */
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    /**
     * Constructor for RoomService, injecting the RoomRepository dependency.
     *
     * @param roomRepository The repository responsible for room data operations.
     */
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Saves a new room.
     *
     * @param room The Room object to be saved.
     * @return The result of the save operation (e.g., affected rows or room ID).
     */
    public int saveRoom(Room room) {
        return roomRepository.save(room);
    }


    /**
     * Get all rooms.
     *
     * @return A list of all rooms.
     */
    public List<Room> getAllRooms() {
        return roomRepository.getAllRooms();
    }

    /**
     * Get a room by its ID.
     *
     * @param roomId The ID of the room to retrieve.
     * @return The room with the specified ID.
     */
    public Room getRoomById(int roomId) {
        return roomRepository.getRoomById(roomId);
    }

    /**
     * Update a room.
     *
     * @param roomId The ID of the room to update.
     * @param updatedRoom The updated Room object.
     * @return The result of the update operation (e.g., affected rows).
     */
    public int updateRoom(int roomId, Room updatedRoom) {
        // Perform any additional business logic before updating the room (if required)
        return roomRepository.updateRoom(updatedRoom);
    }

    /**
     * Delete a room by its ID.
     *
     * @param roomId The ID of the room to delete.
     */
    public void deleteRoom(int roomId) {
        roomRepository.deleteRoom(roomId);
    }

    /**
     * Check if a room is available for a specific time period.
     *
     * @param roomId The ID of the room to check.
     * @param startDate The start date of the time period.
     * @param endDate The end date of the time period.
     * @param eventStartTime The start time of the event.
     * @param eventEndTime The end time of the event.
     * @return True if the room is available, false otherwise.
     */
    public boolean isRoomAvailableForTimePeriod(int roomId, LocalDate startDate, LocalDate endDate, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime) {
        return roomRepository.isRoomAvailableForTimePeriod(roomId, startDate, endDate, eventStartTime, eventEndTime);
    }

    /**
     * Get a list of available rooms for a specific time period.
     *
     * @param startDate The start date of the time period.
     * @param endDate The end date of the time period.
     * @param eventStartTime The start time of the event.
     * @param eventEndTime The end time of the event.
     * @return A list of available room IDs.
     */
    public List<Integer> getAvailableRooms(LocalDate startDate, LocalDate endDate, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime) {
        return roomRepository.getAvailableRooms(startDate, endDate, eventStartTime, eventEndTime);
    }

}
