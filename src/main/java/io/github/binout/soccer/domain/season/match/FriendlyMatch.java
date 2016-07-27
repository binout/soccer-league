package io.github.binout.soccer.domain.season.match;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.domain.player.Player;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class FriendlyMatch implements Match {

    public static final int MAX_PLAYERS = 10;
    public static final int MIN_PLAYERS = 10;

    private FriendlyMatchDate friendlyDate;
    private Set<Player> players;

    FriendlyMatch(){
        this.players = new HashSet<>();
    }

    FriendlyMatch(FriendlyMatchDate date, Set<Player> players) {
        this();
        this.friendlyDate = Objects.requireNonNull(date);
        this.players = checkPlayers(date, players);
    }

    @Override
    public LocalDate date() {
        return friendlyDate.date();
    }

    @Override
    public MatchDate matchDate() {
        return friendlyDate;
    }

    @Override
    public Stream<Player> players() {
        return players.stream();
    }

    @Override
    public void substitutePlayer(Player from, Player by) {
        if (friendlyDate.isAbsent(by)) {
            throw new IllegalArgumentException(by.name() + " is not present for this date");
        }
        if (!players.contains(from)) {
            throw new IllegalArgumentException(from.name() + " is not a player of this match");
        }
        players.remove(from);
        players.add(by);
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
