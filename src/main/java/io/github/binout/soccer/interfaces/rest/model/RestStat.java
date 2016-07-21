package io.github.binout.soccer.interfaces.rest.model;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class RestStat {

    private String player;
    private int nbMatches;

    RestStat(){}

    public RestStat(String player, int nbMatches) {
        this();
        this.player = player;
        this.nbMatches = nbMatches;
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
}
