package io.github.binout.soccer.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class FriendlyMatch {

    private final FriendlyDate friendlyDate;
    private final Set<Player> players;

    FriendlyMatch(FriendlyDate date, Set<Player> players) {
        this.friendlyDate = Objects.requireNonNull(date);
        this.players = Objects.requireNonNull(players);
        if (this.players.size()!= 10) {
            throw new IllegalArgumentException("10 players for a friendly match");
        }
    }

    public LocalDate date() {
        return friendlyDate.date();
    }

    public Stream<Player> players() {
        return players.stream();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendlyMatch that = (FriendlyMatch) o;

        return friendlyDate.equals(that.friendlyDate);

    }

    @Override
    public int hashCode() {
        return friendlyDate.hashCode();
    }
}
