package com.operations.StageOps.controller;

import com.operations.StageOps.service.LayoutCreationService;
import com.operations.StageOps.service.RoomPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    private final LayoutCreationService layoutCreationService;
    private final RoomPopulator roomPopulator;

    @Autowired
    public MeetingController(LayoutCreationService layoutCreationService, RoomPopulator roomPopulator) {
        this.layoutCreationService = layoutCreationService;
        this.roomPopulator = roomPopulator;
    }

    // Endpoint to create a meeting room layout and seating arrangement
    @PostMapping("/{roomId}")
    public ResponseEntity<String> createMeetingRoom(@PathVariable int roomId) {
        try {
            layoutCreationService.createLayoutForRoom(roomId);
            return ResponseEntity.ok("room layout and seats sectioned successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Error creating meeting room: " + e.getMessage());
        }
    }
    @PostMapping("/{roomId}/seats")
    public String createSeatsForRoom(@PathVariable int roomId, @RequestParam int capacity) {
        try {
            roomPopulator.createSeatsForRoom(roomId, capacity);
            return "Seats successfully created for room with ID: " + roomId;
        } catch (Exception e) {
            return "Error creating seats: " + e.getMessage();
        }
    }
}
