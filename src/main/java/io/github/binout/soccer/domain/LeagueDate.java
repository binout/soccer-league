package io.github.binout.soccer.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LeagueDate {

    private final LocalDate date;
    private final Set<Player> absents;

    public LeagueDate(LocalDate date) {
        this.date = Objects.requireNonNull(date);
        this.absents = new HashSet<>();
    }

    public LocalDate date() {
        return date;
    }

    public void present(Player player) {
        absents.remove(player);
    }

    public void absent(Player player) {
        absents.add(player);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeagueDate that = (LeagueDate) o;

        return date.equals(that.date);

    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}
