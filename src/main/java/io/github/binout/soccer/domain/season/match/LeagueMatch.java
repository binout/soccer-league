package io.github.binout.soccer.domain.season.match;

import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.player.Player;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class LeagueMatch implements Match {

    public static final int MAX_PLAYERS = 7;

    private final LeagueMatchDate leagueDate;
    private final Set<Player> players;

    LeagueMatch(LeagueMatchDate date, Set<Player> players) {
        this.leagueDate = Objects.requireNonNull(date);
        this.players = checkPlayers(date, players);
    }

    public LocalDate date() {
        return leagueDate.date();
    }

    public Stream<Player> players() {
        return players.stream();
    }

    @Override
    public int maxPlayers() {
        return MAX_PLAYERS;
    }

    @Override
    public int minPlayers() {
        return 5;
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
