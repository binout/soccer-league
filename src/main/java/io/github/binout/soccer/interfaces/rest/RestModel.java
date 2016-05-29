package io.github.binout.soccer.interfaces.rest;

import java.util.ArrayList;
import java.util.List;

public class RestModel {

    private List<RestLink> links;

    public RestModel() {
        links = new ArrayList<>();
    }

    public List<RestLink> getLinks() {
        return links;
    }

    public void addLinks(RestLink link) {
        this.links.add(link);
    }
}
