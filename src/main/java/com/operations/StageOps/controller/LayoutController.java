package com.operations.StageOps.controller;

import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.service.LayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/layouts")
public class LayoutController {

    private final LayoutService layoutService;

    @Autowired
    public LayoutController(LayoutService layoutService) {
        this.layoutService = layoutService;
    }

    @PostMapping
    public String createLayout(@RequestBody LayoutConfiguration layout) {
        int result = layoutService.saveLayout(layout);
        return result > 0 ? "Layout created successfully!" : "Error creating Layout!";
    }

    @GetMapping("/{layoutId}")
    public LayoutConfiguration getLayout(@PathVariable int layoutId) {
        return layoutService.getLayoutById(layoutId);
    }

    @GetMapping
    public List<LayoutConfiguration> getAllLayouts() {
        return layoutService.getAllLayouts();
    }

    @PutMapping("/{layoutId}")
    public ResponseEntity<LayoutConfiguration> updateLayout(@PathVariable int layoutId, @RequestBody LayoutConfiguration layout) {
        LayoutConfiguration updatedLayout = layoutService.updateLayout(layoutId, layout);
        if (updatedLayout != null) {
            return ResponseEntity.ok(updatedLayout);  // Return the updated layout object with a 200 OK status
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Handle case where the layout is not found
        }
    }

    @GetMapping("/seats")
    public List<Seating> getSectionSeats(@RequestParam String layoutId) {
        return layoutService.getSectionSeats(layoutId);
    }

    @DeleteMapping("/{layoutId}")
    public void deleteLayout(@PathVariable int layoutId) {
        layoutService.deleteLayout(layoutId);
    }
}
