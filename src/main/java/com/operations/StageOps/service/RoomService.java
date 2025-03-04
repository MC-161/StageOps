package com.operations.StageOps.service;

import com.operations.StageOps.model.Room;
import com.operations.StageOps.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Room entities.
 * Provides methods for CRUD operations (Create, Read, Update, Delete) on room records.
 * The class interacts with the `RoomRepository` to perform database operations.
 */
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    /**
     * Constructor for initializing the RoomRepository.
     *
     * @param roomRepository the RoomRepository used for interacting with the database.
     */
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Saves a new room record into the database.
     *
     * @param room the Room object containing the data to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int saveRoom(Room room) {
        return roomRepository.save(room);
    }

    /**
     * Retrieves all room records from the database.
     *
     * @return a list of Room objects representing all rooms.
     */
    public List<Room> getAllRooms() {
        return roomRepository.getAllRooms();
    }

    /**
     * Retrieves a specific room record by its ID.
     *
     * @param roomId the ID of the room.
     * @return the Room object corresponding to the given room ID.
     */
    public Room getRoomById(int roomId) {
        return roomRepository.getRoomById(roomId);
    }

    /**
     * Updates an existing room record in the database.
     *
     * @param roomId      the ID of the room to be updated.
     * @param updatedRoom the Room object containing the updated data.
     * @return the number of rows affected by the update query.
     */
    public int updateRoom(int roomId, Room updatedRoom) {
        // Perform any additional business logic before updating the room (if required)
        return roomRepository.updateRoom(updatedRoom);
    }

    /**
     * Deletes a room record from the database by its room ID.
     *
     * @param roomId the ID of the room to be deleted.
     */
    public void deleteRoom(int roomId) {
        roomRepository.deleteRoom(roomId);
    }
}
