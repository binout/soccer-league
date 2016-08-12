package io.github.binout.soccer.domain.event;

import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import io.github.binout.soccer.domain.season.match.LeagueMatch;

import java.time.LocalDate;
import java.util.stream.Stream;

public class FriendlyMatchPlanned {

    private final LocalDate date;
    private final Stream<String> players;

    public FriendlyMatchPlanned(FriendlyMatch friendlyMatch) {
        date = friendlyMatch.date();
        players = friendlyMatch.players();
    }

    public LocalDate date() {
        return date;
    }

    public Stream<String> players() {
        return players;
    }
}
