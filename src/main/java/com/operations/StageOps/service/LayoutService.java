package com.operations.StageOps.service;


import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.repository.LayoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LayoutService {

    private final LayoutRepository layoutRepository;

    public LayoutService(LayoutRepository layoutRepository) {
        this.layoutRepository = layoutRepository;
    }

    // Save a new layout
    public int saveLayout(LayoutConfiguration layout) {
        return layoutRepository.save(layout);
    }
    // Fetch all layouts associated with a room
    public List<LayoutConfiguration> getLayoutsForRoom(int roomId) {
        return layoutRepository.findLayoutsByRoomId(roomId);
    }

    // Get all layouts
    public List<LayoutConfiguration> getAllLayouts() {
        return layoutRepository.getAllLayouts();
    }

    // Get layout by ID
    public LayoutConfiguration getLayoutById(int layoutId) {
        return layoutRepository.getLayoutById(layoutId);
    }

    public List<Seating> getSectionSeats(String section) {
        return layoutRepository.findSeatsBySectionId(section);
    }
    // Update layout
    public LayoutConfiguration updateLayout(int layoutId, LayoutConfiguration layout) {
        layout.setLayoutId(layoutId);  // Ensure the layoutId is set correctly in the updated layout
        return layoutRepository.update(layout);  // Now the repository will return the updated LayoutConfiguration object
    }
    // Delete layout
    public int deleteLayout(int layoutId) {
        return layoutRepository.delete(layoutId);
    }
}

