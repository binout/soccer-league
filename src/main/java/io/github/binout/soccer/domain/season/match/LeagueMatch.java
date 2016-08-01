package io.github.binout.soccer.domain.season.match;

import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.player.Player;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeagueMatch implements Match {

    public static final int MAX_PLAYERS = 7;
    public static final int MIN_PLAYERS = 5;

    private LocalDate leagueDate;
    private Set<String> players;

    LeagueMatch(){
        this.players = new HashSet<>();
    }

    LeagueMatch(LeagueMatchDate matchDate, Set<Player> players) {
        this();
        this.leagueDate = Objects.requireNonNull(matchDate.date());
        this.players = checkPlayers(matchDate, players).stream().map(Player::name).collect(Collectors.toSet());
    }

    public LocalDate date() {
        return leagueDate;
    }

    @Override
    public void replacePlayer(Player from, Player by) {
        if (!players.contains(from.name())) {
            throw new IllegalArgumentException(from.name() + " is not a player of this match");
        }
        players.remove(from.name());
        players.add(by.name());
    }

    @Override
    public Stream<String> players() {
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
