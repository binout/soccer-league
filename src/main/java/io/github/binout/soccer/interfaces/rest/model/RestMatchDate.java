package io.github.binout.soccer.interfaces.rest.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RestMatchDate extends RestModel {

    private String date;

    RestMatchDate() {}

    public RestMatchDate(LocalDate date) {
        this.date = DateTimeFormatter.ISO_DATE.format(date);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
