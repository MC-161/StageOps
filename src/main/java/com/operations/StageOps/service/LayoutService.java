package com.operations.StageOps.service;


import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.repository.LayoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing layout configurations associated with rooms.
 * Provides CRUD operations for layout configurations.
 */
@Service
public class LayoutService {

    private final LayoutRepository layoutRepository;

    /**
     * Constructor to inject the LayoutRepository dependency.
     *
     * @param layoutRepository The repository that handles the data operations for Layout configurations.
     */
    public LayoutService(LayoutRepository layoutRepository) {
        this.layoutRepository = layoutRepository;
    }

    /**
     * Save a new layout configuration.
     *
     * @param layout The layout configuration to be saved.
     * @return The result of the save operation (e.g., affected rows or layout ID).
     */
    public int saveLayout(LayoutConfiguration layout) {
        return layoutRepository.save(layout);
    }

    /**
     * Fetch all layouts associated with a specific room.
     *
     * @param roomId The ID of the room for which layouts are to be fetched.
     * @return A list of layout configurations for the specified room.
     */
    public List<LayoutConfiguration> getLayoutsForRoom(int roomId) {
        return layoutRepository.findLayoutsByRoomId(roomId);
    }

    /**
     * Fetch all layout configurations.
     *
     * @return A list of all layout configurations.
     */
    public List<LayoutConfiguration> getAllLayouts() {
        return layoutRepository.getAllLayouts();
    }

    /**
     * Fetch a layout configuration by its ID.
     *
     * @param layoutId The ID of the layout configuration to fetch.
     * @return The layout configuration associated with the specified ID.
     */
    public LayoutConfiguration getLayoutById(int layoutId) {
        return layoutRepository.getLayoutById(layoutId);
    }

    /**
     * Fetch all seats in a specific section of a layout.
     *
     * @param section The ID of the section for which seats are to be fetched.
     * @return A list of seating arrangements in the specified section.
     */
    public List<Seating> getSectionSeats(String section) {
        return layoutRepository.findSeatsBySectionId(section);
    }

    /**
     * Update an existing layout configuration.
     *
     * @param layoutId The ID of the layout configuration to be updated.
     * @param layout   The updated layout configuration.
     * @return The updated layout configuration.
     */
    public LayoutConfiguration updateLayout(int layoutId, LayoutConfiguration layout) {
        layout.setLayoutId(layoutId);  // Ensure the layoutId is set correctly in the updated layout
        return layoutRepository.update(layout);  // Now the repository will return the updated LayoutConfiguration object
    }

    /**
     * Delete a layout configuration by its ID.
     *
     * @param layoutId The ID of the layout configuration to be deleted.
     * @return The result of the delete operation (e.g., affected rows).
     */
    public int deleteLayout(int layoutId) {
        return layoutRepository.delete(layoutId);
    }
}

