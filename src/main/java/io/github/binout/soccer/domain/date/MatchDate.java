package io.github.binout.soccer.domain.date;

import io.github.binout.soccer.domain.Player;

import java.time.LocalDate;
import java.time.Month;

public interface MatchDate {

    LocalDate date();

    void present(Player player);

    void absent(Player player);

    boolean isAbsent(Player player);

    static LeagueMatchDate newDateForLeague(int year, Month month, int dayOfMonth) {
        return new LeagueMatchDate(LocalDate.of(year, month, dayOfMonth));
    }

    static FriendlyMatchDate newDateForFriendly(int year, Month month, int dayOfMonth) {
        return new FriendlyMatchDate(LocalDate.of(year, month, dayOfMonth));
    }
}
