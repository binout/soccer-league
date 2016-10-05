package io.github.binout.soccer.interfaces.rest.model;

public class RestStat {

    private String player;
    private int nbMatches;
    private int nbFriendlyMatches;
    private int nbLeagueMatches;

    RestStat(){}

    public RestStat(String player) {
        this();
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getNbMatches() {
        return nbMatches;
    }

    public void setNbMatches(int nbMatches) {
        this.nbMatches = nbMatches;
    }

    public int getNbLeagueMatches() {
        return nbLeagueMatches;
    }

    public void setNbLeagueMatches(int nbLeagueMatches) {
        this.nbLeagueMatches = nbLeagueMatches;
    }

    public int getNbFriendlyMatches() {
        return nbFriendlyMatches;
    }

    public void setNbFriendlyMatches(int nbFriendlyMatches) {
        this.nbFriendlyMatches = nbFriendlyMatches;
    }
}
