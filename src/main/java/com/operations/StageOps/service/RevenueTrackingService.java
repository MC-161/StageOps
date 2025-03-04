package com.operations.StageOps.service;

import com.operations.StageOps.model.RevenueTracking;
import com.operations.StageOps.repository.RevenueTrackingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing RevenueTracking entities.
 * Provides methods for CRUD operations (Create, Read, and Read) on revenue tracking records.
 * The class interacts with the `RevenueTrackingRepository` to perform database operations.
 */
@Service
public class RevenueTrackingService {

    private final RevenueTrackingRepository revenueTrackingRepository;

    /**
     * Constructor for initializing the RevenueTrackingRepository.
     *
     * @param revenueTrackingRepository the RevenueTrackingRepository used for interacting with the database.
     */
    public RevenueTrackingService(RevenueTrackingRepository revenueTrackingRepository) {
        this.revenueTrackingRepository = revenueTrackingRepository;
    }

    /**
     * Saves a new revenue tracking record into the database.
     *
     * @param revenueTracking the RevenueTracking object containing the data to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int saveRevenueTracking(RevenueTracking revenueTracking) {
        return revenueTrackingRepository.save(revenueTracking);
    }

    /**
     * Retrieves all revenue tracking records from the database.
     *
     * @return a list of RevenueTracking objects representing all revenue tracking records.
     */
    public List<RevenueTracking> getAllRevenueTracking() {
        return revenueTrackingRepository.getAllRevenueTracking();
    }

    /**
     * Retrieves a specific revenue tracking record by its ID.
     *
     * @param revenueId the ID of the revenue tracking record.
     * @return the RevenueTracking object corresponding to the given revenue ID.
     */
    public RevenueTracking getRevenueTrackingById(int revenueId) {
        return revenueTrackingRepository.getRevenueTrackingById(revenueId);
    }
}
