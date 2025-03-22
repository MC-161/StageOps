package com.operations.StageOps.service;

import com.operations.StageOps.model.RevenueTracking;
import com.operations.StageOps.repository.RevenueTrackingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    // Get all revenue tracking records (entries)
    public List<RevenueTracking> getAllRevenueTrackingEntries() {
        return revenueTrackingRepository.getAllRevenueTrackingEntries();
    }

    // Get revenue tracking by ID
    public RevenueTracking getRevenueTrackingById(int revenueId) {
        return revenueTrackingRepository.getRevenueTrackingById(revenueId);
    }

    // Get total revenue for a specific day (singular revenue)
    public double getTotalRevenueByDate(String date) {
        return revenueTrackingRepository.getTotalRevenueByDate(date);
    }

    // Get total revenue for a specific month and year (singular revenue)
    public double getTotalRevenueByMonth(int month, int year) {
        return revenueTrackingRepository.getTotalRevenueByMonth(month, year);
    }

    // Get total revenue for a specific year (singular revenue)
    public double getTotalRevenueByYear(int year) {
        return revenueTrackingRepository.getTotalRevenueByYear(year);
    }

    // Get revenue tracking entries by event ID
    public List<RevenueTracking> getRevenueTrackingEntriesByEventId(int eventId) {
        return revenueTrackingRepository.getRevenueTrackingEntriesByEventId(eventId);
    }

    // Get revenue tracking entries by booking ID
    public List<RevenueTracking> getRevenueTrackingEntriesByBookingId(int bookingId) {
        return revenueTrackingRepository.getRevenueTrackingEntriesByBookingId(bookingId);
    }

    // Get revenue tracking entries by day
    public List<RevenueTracking> getRevenueTrackingEntriesByDay(String date) {
        return revenueTrackingRepository.getRevenueTrackingEntriesByDay(date);
    }

    // Get revenue tracking entries by month and year
    public List<RevenueTracking> getRevenueTrackingEntriesByMonth(int month, int year) {
        return revenueTrackingRepository.getRevenueTrackingEntriesByMonth(month, year);
    }

    // Get revenue tracking entries by year
    public List<RevenueTracking> getRevenueTrackingEntriesByYear(int year) {
        return revenueTrackingRepository.getRevenueTrackingEntriesByYear(year);
    }

    //Get revenue tracking entries lifetime


    // Get revenue tracking entries by date range
    public List<RevenueTracking> getRevenueTrackingEntriesByDateRange(String startDate, String endDate) {
        return revenueTrackingRepository.getRevenueTrackingEntriesByDateRange(startDate, endDate);
    }

    /**
     * Get total revenue for each month of the given year.
     *
     * @param year the year for which monthly revenue is requested
     * @return List of maps containing month and total revenue for each month of the year
     */
    public List<Map<String, Object>> getMonthlyRevenue(int year) {
        return revenueTrackingRepository.getTotalRevenueYear(year);
    }

    /**
     * Get total revenue for each day in the given month and year.
     *
     * @param month the month (1-12) for which daily revenue is requested
     * @param year  the year for which daily revenue is requested
     * @return List of maps containing day and total revenue for each day in the month
     */
    public List<Map<String, Object>> getDailyRevenue(int month, int year) {
        return revenueTrackingRepository.getTotalRevenueByMonthAndYear(month, year);
    }

    // Get total revenue for each year lifetime
    public List<Map<String, Object>> getLifetimeRevenueByYear() {
        return revenueTrackingRepository.getLifetimeRevenueByYear();
    }

    public double getTotalRevenueForWeek(LocalDate startOfWeek, LocalDate endOfWeek) {
        return revenueTrackingRepository.sumRevenueByDateBetween(startOfWeek, endOfWeek);
    }

}
