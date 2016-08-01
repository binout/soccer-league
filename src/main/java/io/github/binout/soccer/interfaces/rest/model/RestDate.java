package io.github.binout.soccer.interfaces.rest.model;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

public class RestDate {

    private final int year;
    private final Month month;
    private final int day;

    public RestDate(String date) {
        TemporalAccessor accessor = DateTimeFormatter.ISO_LOCAL_DATE.parse(date);
        year = accessor.get(ChronoField.YEAR);
        month = Month.of(accessor.get(ChronoField.MONTH_OF_YEAR));
        day = accessor.get(ChronoField.DAY_OF_MONTH);
    }

    public int year() {
        return year;
    }

    public Month month() {
        return month;
    }

    public int day() {
        return day;
    }

    public LocalDate asLocalDate() {
        return LocalDate.of(year(), month(), day());
    }
}
