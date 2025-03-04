package com.operations.StageOps.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CostCalculator {

    /**
     * Calculates the total cost of a booking based on the number of days and price per day.
     *
     * @param startDate  The start date of the booking.
     * @param endDate    The end date of the booking.
     * @param pricePerDay The price per day for the room.
     * @return The total cost of the booking.
     */
    public static double calculateTotalCost(Date startDate, Date endDate, double pricePerDay) {
        if (startDate == null || endDate == null || pricePerDay <= 0) {
            return 0;
        }

        long diffInMillis = endDate.getTime() - startDate.getTime();
        long numOfDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        // Ensure at least 1 day is charged
        numOfDays = Math.max(numOfDays, 1);

        return numOfDays * pricePerDay;
    }
}
