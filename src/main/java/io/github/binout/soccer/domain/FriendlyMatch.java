package io.github.binout.soccer.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class FriendlyMatch {

    private Instant date;
    private List<Player> players;

    FriendlyMatch(Instant date, List<Player> players) {
        this.date = Objects.requireNonNull(date);
        this.players = Objects.requireNonNull(players);
        if (this.players.size()!= 10) {
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
