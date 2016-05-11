package io.github.binout.soccer.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class LeagueMatch {

    private final LeagueDate leagueDate;
    private final Set<Player> players;

    LeagueMatch(LeagueDate date, Set<Player> players) {
        this.leagueDate = Objects.requireNonNull(date);
        this.players = Objects.requireNonNull(players);
        if (this.players.size() < 5 || this.players.size() > 7) {
            throw new IllegalArgumentException("5, 6 or 7 players for a league match");
        }
    }

    public LocalDate date() {
        return leagueDate.date();
    }

    public Stream<Player> players() {
        return players.stream();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeagueMatch that = (LeagueMatch) o;

        return leagueDate.equals(that.leagueDate);

    }

    @Override
    public int hashCode() {
        return leagueDate.hashCode();
    }
}
