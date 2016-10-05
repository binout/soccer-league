package io.github.binout.soccer.domain.season;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import io.github.binout.soccer.domain.season.match.LeagueMatch;
import io.github.binout.soccer.domain.season.match.Match;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Season {

    private final String id;
    private String name;
    private final Set<FriendlyMatch> friendlyMatches;
    private final Set<LeagueMatch> leagueMatches;

    Season() {
        this.id = UUID.randomUUID().toString();
        this.friendlyMatches = new HashSet<>();
        this.leagueMatches = new HashSet<>();
    }

    public Season(String name) {
        this();
        this.name = Objects.requireNonNull(name);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Stream<FriendlyMatch> friendlyMatches() {
        return friendlyMatches.stream().sorted(Comparator.comparing(FriendlyMatch::date));
    }

    public Stream<LeagueMatch> leagueMatches() {
        return leagueMatches.stream().sorted(Comparator.comparing(LeagueMatch::date));
    }

    public FriendlyMatch addFriendlyMatch(FriendlyMatchDate date, Set<Player> players) {
        FriendlyMatch match = Match.newFriendlyMatch(date, players);
        this.friendlyMatches.add(match);
        return match;
    }

    public LeagueMatch addLeagueMatch(LeagueMatchDate date, Set<Player> players) {
        LeagueMatch match = Match.newLeagueMatch(date, players);
        this.leagueMatches.add(match);
        return match;
    }

    public SeasonStatistics statistics() {
        return new SeasonStatistics(this);
    }

    public static String currentSeasonName() {
        LocalDate now = LocalDate.now();
        int month = now.get(ChronoField.MONTH_OF_YEAR);
        int year = now.get(ChronoField.YEAR);
        if (month > Month.AUGUST.getValue()) {
            return year + "-" + (year+1);
        } else {
            return (year - 1) + "-" + year;
        }
    }
}
