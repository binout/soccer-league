package io.github.binout.soccer.domain.date;

import io.github.binout.soccer.domain.player.Player;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

public interface MatchDate {

    LocalDate date();

    Stream<String> presents();

    void present(Player player);

    void absent(Player player);

    boolean canBePlanned();

    default int nbPresents() {
        return (int) presents().count();
    }

    default boolean isAbsent(Player player) {
        return !isPresent(player);
    }

    default boolean isPresent(Player player) {
        return presents().filter(n -> n.equals(player.name())).findFirst().isPresent();
    }

    static LeagueMatchDate newDateForLeague(int year, Month month, int dayOfMonth) {
        return new LeagueMatchDate(LocalDate.of(year, month, dayOfMonth));
    }

    static FriendlyMatchDate newDateForFriendly(int year, Month month, int dayOfMonth) {
        return new FriendlyMatchDate(LocalDate.of(year, month, dayOfMonth));
    }

    default boolean isNowOrFuture() {
        LocalDate now = LocalDate.now();
        return date().isAfter(now) || date().isEqual(now);
    }
}