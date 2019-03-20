package io.github.binout.soccer.interfaces.rest.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RestMatch extends RestModel {

    private String date;
    private List<String> players;
    private List<String> subs;

    RestMatch() {
        players = new ArrayList<>();
        subs = new ArrayList<>();
    }

    public RestMatch(LocalDate date) {
        this();
        this.date = DateTimeFormatter.ISO_DATE.format(date);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void addPlayer(String player) {
        this.players.add(player);
    }

    public List<String> getSubs() {
        return subs;
    }

    public void addSub(String player) {
        this.subs.add(player);
    }
}