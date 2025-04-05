package com.operations.StageOps.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parseDate(String dateStr) {
        try {
            return sdf.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format. Expected YYYY-MM-DD.");
        }
    }

    public static String formatDate(Date date) {
        return sdf.format(date);
    }
}
