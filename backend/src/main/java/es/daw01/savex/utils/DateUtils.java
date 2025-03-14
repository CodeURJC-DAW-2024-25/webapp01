package es.daw01.savex.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm";
    public static final String DATE_FORMAT_DATE = "dd-MM-yyyy";

    private DateUtils() {
        /* Prevent instantiation */ }

    /**
     * Get the current date and time
     * @return LocalDateTime object with the current date and time
    */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * Format a LocalDateTime object to a string with the format "dd-MM-yyyy HH:mm"
     * @param date LocalDateTime object to format
     * @return String with the formatted date
    */
    public static String format(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return date.format(formatter);
    }

    public static String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DATE);
        return date.format(formatter);
    }
}
