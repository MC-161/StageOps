package com.operations.StageOps.service;

import com.operations.StageOps.model.RevenueTracking;
import com.operations.StageOps.repository.RevenueTrackingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service class responsible for managing revenue tracking records.
 * Provides various methods to fetch, save, and calculate revenue data based on different time frames (daily, monthly, yearly).
 */
@Service
public class RevenueTrackingService {

    private final RevenueTrackingRepository revenueTrackingRepository;

    /**
     * Constructor to inject the RevenueTrackingRepository dependency.
     *
     * @param revenueTrackingRepository The repository that handles data operations for revenue tracking.
     */
    public RevenueTrackingService(RevenueTrackingRepository revenueTrackingRepository) {
        this.revenueTrackingRepository = revenueTrackingRepository;
    }

    /**
     * Save a new revenue tracking record.
     *
     * @param revenueTracking The revenue tracking record to be saved.
     * @return The result of the save operation (e.g., affected rows or revenue ID).
     */
    public int saveRevenueTracking(RevenueTracking revenueTracking) {
        return revenueTrackingRepository.save(revenueTracking);
    }


    /**
     * Get all revenue tracking records.
     *
     * @return A list of all revenue tracking entries in the system.
     */
    public List<RevenueTracking> getAllRevenueTrackingEntries() {
        return revenueTrackingRepository.getAllRevenueTrackingEntries();
    }

    /**
     * Get revenue tracking record by its ID.
     *
     * @param revenueId The ID of the revenue tracking record.
     * @return The revenue tracking record with the specified ID.
     */
    public RevenueTracking getRevenueTrackingById(int revenueId) {
        return revenueTrackingRepository.getRevenueTrackingById(revenueId);
    }

    /**
     * Get total revenue for a specific date.
     *
     * @param date The date for which total revenue is requested.
     * @return The total revenue for the specified date.
     */
    public double getTotalRevenueByDate(String date) {
        return revenueTrackingRepository.getTotalRevenueByDate(date);
    }

    /**
     * Get the total revenue for a specific month and year.
     *
     * @param month The month (1-12) for which the total revenue is requested.
     * @param year  The year for which the total revenue is requested.
     * @return The total revenue for the given month and year.
     */
    public double getTotalRevenueByMonth(int month, int year) {
        return revenueTrackingRepository.getTotalRevenueByMonth(month, year);
    }

    /**
     * Get the total revenue for a specific year.
     *
     * @param year The year for which the total revenue is requested.
     * @return The total revenue for the given year.
     */
    public double getTotalRevenueByYear(int year) {
        return revenueTrackingRepository.getTotalRevenueByYear(year);
    }

    /**
     * Get revenue tracking records by event ID.
     *
     * @param eventId The event ID for which the revenue records are requested.
     * @return A list of revenue tracking records associated with the specified event.
     */
    public List<RevenueTracking> getRevenueTrackingEntriesByEventId(int eventId) {
        return revenueTrackingRepository.getRevenueTrackingEntriesByEventId(eventId);
    }

    /**
     * Get revenue tracking records by booking ID.
     *
     * @param bookingId The booking ID for which the revenue records are requested.
     * @return A list of revenue tracking records associated with the specified booking.
     */
    public List<RevenueTracking> getRevenueTrackingEntriesByBookingId(int bookingId) {
        return revenueTrackingRepository.getRevenueTrackingEntriesByBookingId(bookingId);
    }

    /**
     * Get revenue tracking records for a specific date.
     *
     * @param date The date for which the revenue records are requested.
     * @return A list of revenue tracking records associated with the specified date.
     */
    public List<RevenueTracking> getRevenueTrackingEntriesByDay(String date) {
        return revenueTrackingRepository.getRevenueTrackingEntriesByDay(date);
    }

    /**
     * Get revenue tracking records for a specific month and year.
     *
     * @param month The month (1-12) for which the revenue records are requested.
     * @param year  The year for which the revenue records are requested.
     * @return A list of revenue tracking records for the given month and year.
     */
    public List<RevenueTracking> getRevenueTrackingEntriesByMonth(int month, int year) {
        return revenueTrackingRepository.getRevenueTrackingEntriesByMonth(month, year);
    }

    /**
     * Get revenue tracking records for a specific year.
     *
     * @param year The year for which the revenue records are requested.
     * @return A list of revenue tracking records for the given year.
     */
    public List<RevenueTracking> getRevenueTrackingEntriesByYear(int year) {
        return revenueTrackingRepository.getRevenueTrackingEntriesByYear(year);
    }

    //Get revenue tracking entries lifetime


    /**
     * Get revenue tracking records within a specific date range.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return A list of revenue tracking records within the specified date range.
     */
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

    /**
     * Get total revenue for each year.
     *
     * @return List of maps containing year and total revenue for each year
     */
    public List<Map<String, Object>> getLifetimeRevenueByYear() {
        return revenueTrackingRepository.getLifetimeRevenueByYear();
    }

    /**
     * Get total revenue for the week.
     *
     * @param startOfWeek The start date of the week.
     * @param endOfWeek   The end date of the week.
     * @return The total revenue for the week.
     */
    public double getTotalRevenueForWeek(LocalDate startOfWeek, LocalDate endOfWeek) {
        return revenueTrackingRepository.sumRevenueByDateBetween(startOfWeek, endOfWeek);
    }

}
