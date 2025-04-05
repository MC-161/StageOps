package com.operations.StageOps.controller;

import com.operations.StageOps.model.RevenueTracking;
import com.operations.StageOps.service.RevenueTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controller class for handling requests related to revenue tracking.
 */
@RestController
@RequestMapping("/api/revenue")
public class RevenueTrackingController {

    private final RevenueTrackingService revenueTrackingService;

    /**
     * Constructor for the RevenueTrackingController class.
     *
     * @param revenueTrackingService the service class for revenue tracking
     */
    @Autowired
    public RevenueTrackingController(RevenueTrackingService revenueTrackingService) {
        this.revenueTrackingService = revenueTrackingService;
    }


    /**
     * Retrieves total revenue for a specific day.
     *
     * @param date the date in YYYY-MM-DD format
     * @return the total revenue for the given date
     */
    @GetMapping("/total/daily/{date}")
    public ResponseEntity<Double> getTotalRevenueByDate(@PathVariable String date) {
        double totalRevenue = revenueTrackingService.getTotalRevenueByDate(date);
        return ResponseEntity.ok(totalRevenue);
    }

    /**
     * Retrieves total revenue for a specific month and year.
     *
     * @param year  the year
     * @param month the month (1-12)
     * @return the total revenue for the given month and year
     */
    @GetMapping("/total/monthly/{year}/{month}")
    public ResponseEntity<Double> getTotalRevenueByMonth(@PathVariable int year, @PathVariable int month) {
        double totalRevenue = revenueTrackingService.getTotalRevenueByMonth(month, year);
        return ResponseEntity.ok(totalRevenue);
    }

    /**
     * Retrieves total revenue for a specific year.
     *
     * @param year the year
     * @return the total revenue for the given year
     */
    @GetMapping("/total/yearly/{year}")
    public ResponseEntity<Double> getTotalRevenueByYear(@PathVariable int year) {
        double totalRevenue = revenueTrackingService.getTotalRevenueByYear(year);
        return ResponseEntity.ok(totalRevenue);
    }

    /**
     * Retrieves total revenue for all time.
     *
     * @return the total revenue for all time
     */
    @GetMapping("/entries/daily/{date}")
    public ResponseEntity<List<RevenueTracking>> getRevenueEntriesByDay(@PathVariable String date) {
        List<RevenueTracking> entries = revenueTrackingService.getRevenueTrackingEntriesByDay(date);
        return ResponseEntity.ok(entries);
    }

    /**
     * Retrieves total revenue for all time.
     *
     * @return the total revenue for all time
     */
    @GetMapping("/entries/monthly/{year}/{month}")
    public ResponseEntity<List<RevenueTracking>> getRevenueEntriesByMonth(@PathVariable int year, @PathVariable int month) {
        List<RevenueTracking> entries = revenueTrackingService.getRevenueTrackingEntriesByMonth(month, year);
        return ResponseEntity.ok(entries);
    }

    /**
     * Retrieves total revenue for all time.
     *
     * @return the total revenue for all time
     */
    @GetMapping("/entries/yearly/{year}")
    public ResponseEntity<List<RevenueTracking>> getRevenueEntriesByYear(@PathVariable int year) {
        List<RevenueTracking> entries = revenueTrackingService.getRevenueTrackingEntriesByYear(year);
        return ResponseEntity.ok(entries);
    }


    /**
     * Retrieves total revenue for all time.
     *
     * @return the total revenue for all time
     */
    @GetMapping("/entries/lifetime")
    public ResponseEntity<List<RevenueTracking>> getAllRevenueEntries() {
        List<RevenueTracking> entries = revenueTrackingService.getAllRevenueTrackingEntries();
        return ResponseEntity.ok(entries);
    }

    /**
     * Endpoint to get the monthly revenue for a given year.
     *
     * @param year the year for which monthly revenue is requested
     * @return List of maps containing the month and total revenue for each month in the year
     */
    @GetMapping("/monthly/{year}")
    public List<Map<String, Object>> getMonthlyRevenue(@PathVariable int year) {
        return revenueTrackingService.getMonthlyRevenue(year);
    }

    /**
     * Endpoint to get the daily revenue for a given month and year.
     *
     * @param month the month (1-12) for which daily revenue is requested
     * @param year  the year for which daily revenue is requested
     * @return List of maps containing the day and total revenue for each day in the month
     */
    @GetMapping("/daily/{month}/{year}")
    public List<Map<String, Object>> getDailyRevenue(@PathVariable int month, @PathVariable int year) {
        return revenueTrackingService.getDailyRevenue(month, year);
    }


    /**
     * Retrieves the total lifetime revenue grouped by year.
     *
     * @return a list of maps containing the year and total revenue for each year
     */
    @GetMapping("/lifetime")
    public List<Map<String, Object>> getLifetimeRevenueByYear() {
        return revenueTrackingService.getLifetimeRevenueByYear();
    }

    /**
     * Retrieves the total revenue for the current week.
     *
     * @return the total revenue for the current week
     */
    @GetMapping("/this-week/total")
    public ResponseEntity<Double> getTotalRevenueThisWeek() {
        LocalDate startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY); // Get the start of the current week (Monday)
        LocalDate endOfWeek = startOfWeek.plusDays(6); // End of the week (Sunday)

        double totalRevenue = revenueTrackingService.getTotalRevenueForWeek(startOfWeek, endOfWeek);
        return ResponseEntity.ok(totalRevenue);
    }
}
