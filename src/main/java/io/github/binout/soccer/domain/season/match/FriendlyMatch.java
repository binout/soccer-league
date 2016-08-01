package io.github.binout.soccer.domain.season.match;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.player.Player;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FriendlyMatch implements Match {

    public static final int MAX_PLAYERS = 10;
    public static final int MIN_PLAYERS = 10;

    private LocalDate friendlyDate;
    private Set<String> players;

    FriendlyMatch(){
        this.players = new HashSet<>();
    }

    FriendlyMatch(FriendlyMatchDate matchDate, Set<Player> players) {
        this();
        this.friendlyDate = Objects.requireNonNull(matchDate.date());
        this.players = checkPlayers(matchDate, players).stream().map(Player::name).collect(Collectors.toSet());
    }

    @Override
    public LocalDate date() {
        return friendlyDate;
    }

    @Override
    public Stream<String> players() {
        return players.stream();
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

        FriendlyMatch that = (FriendlyMatch) o;

        return friendlyDate.equals(that.friendlyDate);

    }

    @Override
    public int hashCode() {
        return friendlyDate.hashCode();
    }
}
