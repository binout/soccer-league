package io.github.binout.soccer.domain.season.match;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.MatchDate;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public interface Match {

    LocalDate date();

    Stream<String> players();

    int maxPlayers();

    int minPlayers();

    void replacePlayer(Player from, Player by);

    default Set<Player> checkPlayers(MatchDate date, Set<Player> players) {
        if (players.size() < minPlayers() || players.size() > maxPlayers()) {
            throw new IllegalArgumentException("Number of players is incorrect for the match");
        }
        if (players.stream().anyMatch(date::isAbsent)) {
            throw new IllegalArgumentException("Some players are absent for this date");
        }
        return Objects.requireNonNull(players);
    }

    static LeagueMatch newLeagueMatch(LeagueMatchDate date, Set<Player> players) {
        return new LeagueMatch(date, players);
    }

    static FriendlyMatch newFriendlyMatch(FriendlyMatchDate date, Set<Player> players) {
        return new FriendlyMatch(date, players);
    }
}
