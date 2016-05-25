package io.github.binout.soccer.domain.season;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import io.github.binout.soccer.domain.season.match.LeagueMatch;
import io.github.binout.soccer.domain.season.match.Match;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Season {

    private final String name;
    private final Set<FriendlyMatch> friendlyMatches;
    private final Set<LeagueMatch> leagueMatches;

    public Season(String name) {
        this.name = Objects.requireNonNull(name);
        this.friendlyMatches = new HashSet<>();
        this.leagueMatches = new HashSet<>();
    }

    public String name() {
        return name;
    }

    public Stream<FriendlyMatch> friendlyMatches() {
        return friendlyMatches.stream();
    }

    public Stream<LeagueMatch> leagueMatches() {
        return leagueMatches.stream();
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
        List<Player> allPlayerGames = this.friendlyMatches.stream().flatMap(FriendlyMatch::players).collect(Collectors.toList());
        allPlayerGames.addAll(this.leagueMatches.stream().flatMap(LeagueMatch::players).collect(Collectors.toList()));

        return new SeasonStatistics(allPlayerGames.stream().distinct().collect(Collectors.toMap(
                Function.identity(),
                p -> allPlayerGames.stream().map(Player::name).filter(name -> name.equals(p.name())).count()
        )));
    }

}
