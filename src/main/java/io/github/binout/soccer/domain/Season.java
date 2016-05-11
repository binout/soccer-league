package io.github.binout.soccer.domain;

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

    public FriendlyMatch addFriendlyMatch(FriendlyDate date, Set<Player> players) {
        FriendlyMatch match = new FriendlyMatch(date, players);
        this.friendlyMatches.add(match);
        return match;
    }

    public LeagueMatch addLeagueMatch(LeagueDate date, Set<Player> players) {
        LeagueMatch match = new LeagueMatch(date, players);
        this.leagueMatches.add(match);
        return match;
    }

    public Map<Player, Long> computeParticipations() {
        List<Player> participations = this.friendlyMatches.stream().flatMap(FriendlyMatch::players).collect(Collectors.toList());
        participations.addAll(this.leagueMatches.stream().flatMap(LeagueMatch::players).collect(Collectors.toList()));

        return participations.stream().distinct().collect(Collectors.toMap(
                Function.identity(),
                p -> participations.stream().map(Player::name).filter(name -> name.equals(p.name())).count()
        ));
    }

}
