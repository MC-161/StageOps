package com.operations.StageOps.controller;

import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.service.LayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/layouts")
public class LayoutController {

    private final LayoutService layoutService;

    /**
     * Constructor for LayoutController
     * @param layoutService LayoutService object
     */
    @Autowired
    public LayoutController(LayoutService layoutService) {
        this.layoutService = layoutService;
    }

    /**
     * Create a new layout
     * @param layout LayoutConfiguration object
     * @return String message
     */
    @PostMapping
    public String createLayout(@RequestBody LayoutConfiguration layout) {
        int result = layoutService.saveLayout(layout);
        return result > 0 ? "Layout created successfully!" : "Error creating Layout!";
    }

    /**
     * Get all layouts for a room
     * @param roomId int
     * @return List of LayoutConfiguration objects
     */
    @GetMapping("rooms/{roomId}")
    public List<LayoutConfiguration> getLayoutsForRoom(@PathVariable int roomId) {
        return layoutService.getLayoutsForRoom(roomId);
    }

    /**
     * Get a layout by id
     * @param layoutId int
     * @return LayoutConfiguration object
     */
    @GetMapping("/{layoutId}")
    public LayoutConfiguration getLayout(@PathVariable int layoutId) {
        return layoutService.getLayoutById(layoutId);
    }

    /**
     * Get all layouts
     * @return List of LayoutConfiguration objects
     */
    @GetMapping
    public List<LayoutConfiguration> getAllLayouts() {
        return layoutService.getAllLayouts();
    }

    /**
     * Update a layout
     * @param layoutId int
     * @param layout LayoutConfiguration object
     * @return ResponseEntity object
     */
    @PutMapping("/{layoutId}")
    public ResponseEntity<LayoutConfiguration> updateLayout(@PathVariable int layoutId, @RequestBody LayoutConfiguration layout) {
        LayoutConfiguration updatedLayout = layoutService.updateLayout(layoutId, layout);
        if (updatedLayout != null) {
            return ResponseEntity.ok(updatedLayout);  // Return the updated layout object with a 200 OK status
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Handle case where the layout is not found
        }
    }

    /**
     * Get all seats for a section
     * @param layoutId String
     * @return List of Seating objects
     */
    @GetMapping("/seats")
    public List<Seating> getSectionSeats(@RequestParam String layoutId) {
        return layoutService.getSectionSeats(layoutId);
    }

    /**
     * Delete a layout
     * @param layoutId int
     */
    @DeleteMapping("/{layoutId}")
    public void deleteLayout(@PathVariable int layoutId) {
        layoutService.deleteLayout(layoutId);
    }
}
