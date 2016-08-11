package io.github.binout.soccer.domain.event;

import io.github.binout.soccer.domain.season.match.LeagueMatch;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class LeagueMatchPlanned {

    private final LocalDate date;
    private final Stream<String> players;

    public LeagueMatchPlanned(LeagueMatch leagueMatch) {
        date = leagueMatch.date();
        players = leagueMatch.players();
    }

    public LocalDate date() {
        return date;
    }

    public Stream<String> players() {
        return players;
    }
}
