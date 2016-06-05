package io.github.binout.soccer.interfaces.rest.model;

public class RestSeason extends RestModel {

    private String name;

    RestSeason(){}

    public RestSeason(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
