package com.operations.StageOps.service;

import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.repository.LayoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing LayoutConfiguration entities.
 * It provides methods for CRUD operations (Create, Read, Update, Delete) and other business logic related to layouts.
 * The class interacts with the `LayoutRepository` to perform database operations.
 */
@Service
public class LayoutService {

    private final LayoutRepository layoutRepository;

    /**
     * Constructor for initializing the LayoutRepository.
     *
     * @param layoutRepository the LayoutRepository used for interacting with the layout database.
     */
    public LayoutService(LayoutRepository layoutRepository) {
        this.layoutRepository = layoutRepository;
    }

    /**
     * Saves a new layout configuration record into the database.
     *
     * @param layout the LayoutConfiguration object containing the data to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int saveLayout(LayoutConfiguration layout) {
        return layoutRepository.save(layout);
    }

    /**
     * Retrieves all layout configurations associated with a specific room.
     *
     * @param roomId the ID of the room for which layouts are to be fetched.
     * @return a list of LayoutConfiguration objects representing the layouts for the given room.
     */
    public List<LayoutConfiguration> getLayoutsForRoom(int roomId) {
        return layoutRepository.findLayoutsByRoomId(roomId);
    }

    /**
     * Retrieves all layout configurations from the database.
     *
     * @return a list of all LayoutConfiguration objects in the database.
     */
    public List<LayoutConfiguration> getAllLayouts() {
        return layoutRepository.getAllLayouts();
    }

    /**
     * Retrieves a specific layout configuration by its ID.
     *
     * @param layoutId the ID of the layout configuration to retrieve.
     * @return the LayoutConfiguration object corresponding to the given layout ID.
     */
    public LayoutConfiguration getLayoutById(int layoutId) {
        return layoutRepository.getLayoutById(layoutId);
    }

    /**
     * Retrieves all seats associated with a specific section.
     *
     * @param section the ID or name of the section for which seats are to be fetched.
     * @return a list of Seating objects representing the seats in the given section.
     */
    public List<Seating> getSectionSeats(String section) {
        return layoutRepository.findSeatsBySectionId(section);
    }

    /**
     * Updates an existing layout configuration record in the database.
     *
     * @param layoutId the ID of the layout configuration to be updated.
     * @param layout the LayoutConfiguration object containing the updated data.
     * @return the updated LayoutConfiguration object.
     */
    public LayoutConfiguration updateLayout(int layoutId, LayoutConfiguration layout) {
        layout.setLayoutId(layoutId);  // Ensure the layoutId is set correctly in the updated layout
        return layoutRepository.update(layout);  // Now the repository will return the updated LayoutConfiguration object
    }

    /**
     * Deletes a layout configuration record from the database based on the layout ID.
     *
     * @param layoutId the ID of the layout configuration to be deleted.
     * @return the number of rows affected by the delete query (usually 1 if successful).
     */
    public int deleteLayout(int layoutId) {
        return layoutRepository.delete(layoutId);
    }
}
