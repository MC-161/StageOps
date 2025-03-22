package com.operations.StageOps.service;


import com.operations.StageOps.model.Room;
import com.operations.StageOps.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
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

    // Check if a specific room is available for a time period
    public boolean isRoomAvailableForTimePeriod(int roomId, LocalDate startDate, LocalDate endDate, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime) {
        return roomRepository.isRoomAvailableForTimePeriod(roomId, startDate, endDate, eventStartTime, eventEndTime);
    }

    // Get a list of all available rooms for a specific time period
    public List<Integer> getAvailableRooms(LocalDate startDate, LocalDate endDate, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime) {
        return roomRepository.getAvailableRooms(startDate, endDate, eventStartTime, eventEndTime);
    }

}
