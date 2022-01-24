package br.com.pa.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String format(LocalDate date) {
        return FORMATTER.format(date);
    }

    public static String format(LocalDateTime dateTime) {
        return FORMATTER.format(dateTime);
    }

}
