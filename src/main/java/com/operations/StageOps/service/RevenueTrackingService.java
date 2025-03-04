package com.operations.StageOps.service;


import com.operations.StageOps.model.RevenueTracking;
import com.operations.StageOps.repository.RevenueTrackingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RevenueTrackingService {

    private final RevenueTrackingRepository revenueTrackingRepository;

    public RevenueTrackingService(RevenueTrackingRepository revenueTrackingRepository) {
        this.revenueTrackingRepository = revenueTrackingRepository;
    }

    // Save revenue tracking record
    public int saveRevenueTracking(RevenueTracking revenueTracking) {
        return revenueTrackingRepository.save(revenueTracking);
    }

    // Get all revenue tracking records
    public List<RevenueTracking> getAllRevenueTracking() {
        return revenueTrackingRepository.getAllRevenueTracking();
    }

    // Get revenue tracking by ID
    public RevenueTracking getRevenueTrackingById(int revenueId) {
        return revenueTrackingRepository.getRevenueTrackingById(revenueId);
    }
}
