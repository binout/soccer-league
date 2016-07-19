package io.github.binout.soccer.interfaces.rest.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RestMatchDate extends RestModel {

    private String date;
    private List<String> presents;
    private boolean canBePlanned;

    RestMatchDate() {
        presents = new ArrayList<>();
    }

    public RestMatchDate(LocalDate date) {
        this();
        this.date = DateTimeFormatter.ISO_DATE.format(date);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getPresents() {
        return presents;
    }

    public void addPresent(String player) {
        this.presents.add(player);
    }

    public boolean isCanBePlanned() {
        return canBePlanned;
    }

    public void setCanBePlanned(boolean canBePlanned) {
        this.canBePlanned = canBePlanned;
    }
}
