package com.operations.StageOps.controller;


import com.operations.StageOps.service.LayoutService;
import com.operations.StageOps.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;
    private final LayoutService layoutService;

    @Autowired
    public RoomController(RoomService roomService, LayoutService layoutService) {
        this.roomService = roomService;
        this.layoutService = layoutService;
    }
}
