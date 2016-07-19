package io.github.binout.soccer.domain.date;

import io.github.binout.soccer.domain.player.Player;

import java.time.LocalDate;
import java.time.Month;

public interface MatchDate {

    LocalDate date();

    void present(Player player);

    int nbPresents();

    void absent(Player player);

    boolean isAbsent(Player player);

    default boolean isPresent(Player player) {
        return !isAbsent(player);
    }

    static LeagueMatchDate newDateForLeague(int year, Month month, int dayOfMonth) {
        return new LeagueMatchDate(LocalDate.of(year, month, dayOfMonth));
    }

    static FriendlyMatchDate newDateForFriendly(int year, Month month, int dayOfMonth) {
        return new FriendlyMatchDate(LocalDate.of(year, month, dayOfMonth));
    }
}
