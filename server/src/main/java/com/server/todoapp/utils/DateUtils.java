package com.server.todoapp.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String getDateOnly(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public static String getTimeOnly(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static String getDateAndTime(LocalDateTime dateTime) {
        return getDateOnly(dateTime) + " " + getTimeOnly(dateTime);
    }

    /**
     * Parse date in format dd-MM-yyyy HH:mm:ss
     * @param dateTime string in the format above
     * @return {@link LocalDateTime} object
     */
    public static LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
}
