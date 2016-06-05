package io.github.binout.soccer.interfaces.rest.model;

import java.net.URI;

public class RestLink {

    private String rel;
    private String href;

    RestLink() {
    }

    public RestLink(String rel, String href) {
        this();
        this.rel = rel;
        this.href = href;
    }

    public RestLink(String href) {
        this("self", href);
    }

    public RestLink(URI uri) {
        this("self", uri.toString());
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
