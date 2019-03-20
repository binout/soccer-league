package io.github.binout.soccer.interfaces.rest.model;

public class RestPlayer extends RestModel {

    private String name;
    private String email;
    private Boolean isPlayerLeague;
    private Boolean isGoalkeeper;

    RestPlayer(){}

    public RestPlayer(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isPlayerLeague() {
        return isPlayerLeague;
    }

    public void setPlayerLeague(Boolean playerLeague) {
        isPlayerLeague = playerLeague;
    }

    public Boolean isGoalkeeper() {
        return isGoalkeeper;
    }

    public void setGoalkeeper(Boolean goalkeeper) {
        isGoalkeeper = goalkeeper;
    }
}