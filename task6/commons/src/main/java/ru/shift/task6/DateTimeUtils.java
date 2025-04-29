package ru.shift.task6;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static ZonedDateTime toLocalTime(Instant instant, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(instant, zoneId);
    }

    public static ZonedDateTime toLocalTime(Instant instant) {
        return toLocalTime(instant, ZoneId.systemDefault());
    }

    public static String format(ZonedDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }
}
