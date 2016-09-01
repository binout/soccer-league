package io.github.binout.soccer.domain.event;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.season.match.LeagueMatch;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class LeagueMatchPlanned {

    private final LocalDate date;
    private final Stream<String> players;
    private final List<Player> substitutes;

    public LeagueMatchPlanned(LeagueMatch leagueMatch, List<Player> substitutes) {
        this.date = leagueMatch.date();
        this.players = leagueMatch.players();
        this.substitutes = substitutes;
    }

    public LocalDate date() {
        return date;
    }

    public Stream<String> players() {
        return players;
    }

    public Stream<String> substitutes() {
        return substitutes.stream().map(Player::name);
    }
}
