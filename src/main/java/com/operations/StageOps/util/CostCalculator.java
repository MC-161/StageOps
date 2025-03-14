package com.operations.StageOps.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CostCalculator {

    /**
     * Calculates the total cost of a booking based on the number of days and price per day.
     *
     * @param startDate  The start date of the booking.
     * @param endDate    The end date of the booking.
     * @param pricePerDay The price per day for the room.
     * @return The total cost of the booking.
     */
    public static double calculateTotalCost(LocalDate startDate, LocalDate endDate, double pricePerDay) {
        if (startDate == null || endDate == null || pricePerDay <= 0) {
            return 0;
        }

        // Calculate the number of days between the start and end date
        long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);

        // Ensure at least 1 day is charged
        numOfDays = Math.max(numOfDays, 1);

        return numOfDays * pricePerDay;
    }
}
