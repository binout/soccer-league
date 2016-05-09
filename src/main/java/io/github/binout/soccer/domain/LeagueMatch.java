package io.github.binout.soccer.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class LeagueMatch {

    private Instant date;
    private List<Player> players;

    LeagueMatch(Instant date, List<Player> players) {
        this.date = Objects.requireNonNull(date);
        this.players = Objects.requireNonNull(players);
        if (this.players.size() < 5 || this.players.size() > 7) {
            throw new IllegalArgumentException();
        }
    }

    public Instant date() {
        return date;
    }

    public Stream<Player> players() {
        return players.stream();
    }
}
