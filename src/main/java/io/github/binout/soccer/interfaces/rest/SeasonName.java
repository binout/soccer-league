package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.domain.season.Season;

/**
 * LECTRA
 *
 * @author b.prioux
 */
class SeasonName {

    private final String name;

    SeasonName(String param) {
        if ("current".equals(param)) {
            this.name = Season.currentSeasonName();
        } else {
            this.name = param;
        }
    }

    String name() {
        return name;
    }
}
