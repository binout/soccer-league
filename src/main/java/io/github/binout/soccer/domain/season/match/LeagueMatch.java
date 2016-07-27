package io.github.binout.soccer.domain.season.match;

import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.domain.player.Player;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class LeagueMatch implements Match {

    public static final int MAX_PLAYERS = 7;
    public static final int MIN_PLAYERS = 5;

    private LeagueMatchDate leagueDate;
    private Set<Player> players;

    LeagueMatch(){
        this.players = new HashSet<>();
    }

    LeagueMatch(LeagueMatchDate date, Set<Player> players) {
        this();
        this.leagueDate = Objects.requireNonNull(date);
        this.players = checkPlayers(date, players);
    }

    public LocalDate date() {
        return leagueDate.date();
    }

    @Override
    public MatchDate matchDate() {
        return leagueDate;
    }

    @Override
    public void substitutePlayer(Player from, Player by) {
        if (leagueDate.isAbsent(by)) {
            throw new IllegalArgumentException(by.name() + " is not present for this date");
        }
        if (!players.contains(from)) {
            throw new IllegalArgumentException(from.name() + " is not a player of this match");
        }
        players.remove(from);
        players.add(by);
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
        return MIN_PLAYERS;
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
