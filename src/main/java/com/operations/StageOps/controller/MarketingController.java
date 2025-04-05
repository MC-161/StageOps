package com.operations.StageOps.controller;

import com.operations.StageOps.model.Event;
import com.operations.StageOps.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketing")
public class MarketingController {
    private final EventService eventService;

    @Autowired
    public MarketingController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // New method to schedule events for marketing with 3-week validation
    @PostMapping("/scheduleForMarketing")
    public String scheduleForMarketing(@RequestBody Event event) {
        return eventService.scheduleForMarketing(event);
    }

    // Endpoint for marketing to hold seats for group booking
    @PostMapping("/{eventId}/holdSeats")
    public String holdSeatsForGroupBooking(@PathVariable int eventId, @RequestParam int groupSize, @RequestBody List<String> seatIds) {
        return eventService.holdSeatsForGroupBooking(eventId, groupSize, seatIds);
    }
}
