package com.operations.StageOps.service;


import com.operations.StageOps.model.Room;
import com.operations.StageOps.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Save a new room
    public int saveRoom(Room room) {
        return roomRepository.save(room);
    }


    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.getAllRooms();
    }

    // Get room by ID
    public Room getRoomById(int roomId) {
        return roomRepository.getRoomById(roomId);
    }
    public int updateRoom(int roomId, Room updatedRoom) {
        // Perform any additional business logic before updating the room (if required)
        return roomRepository.updateRoom(updatedRoom);
    }
    // Delete room by roomId
    public void deleteRoom(int roomId) {
        roomRepository.deleteRoom(roomId);
    }
}
