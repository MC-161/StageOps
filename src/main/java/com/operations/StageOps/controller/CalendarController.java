package com.operations.StageOps.controller;

import com.operations.StageOps.model.Event;
import com.operations.StageOps.model.Booking;
import com.operations.StageOps.service.EventService;
import com.operations.StageOps.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")

public class CalendarController {

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingService bookingService;
    // Retrieve all events for CalendarFX
}
