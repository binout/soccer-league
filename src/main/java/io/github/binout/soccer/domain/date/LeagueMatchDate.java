package io.github.binout.soccer.domain.date;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.season.match.LeagueMatch;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class LeagueMatchDate implements MatchDate {

    private final String id;
    private LocalDate date;
    private Set<String> presents;

    LeagueMatchDate() {
        this.id = UUID.randomUUID().toString();
        this.presents = new HashSet<>();
    }

    LeagueMatchDate(LocalDate date) {
        this();
        this.date = Objects.requireNonNull(date);
    }

    public String id() {
        return id;
    }

    @Override
    public LocalDate date() {
        return date;
    }

    @Override
    public Stream<String> presents() {
        return presents.stream();
    }

    @Override
    public void present(Player player) {
        presents.add(player.name());
    }

    @Override
    public void absent(Player player) {
        presents.remove(player.name());
    }

    @Override
    public boolean canBePlanned() {
        return nbPresents() >= LeagueMatch.MIN_PLAYERS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeagueMatchDate that = (LeagueMatchDate) o;

        return date.equals(that.date);

    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}