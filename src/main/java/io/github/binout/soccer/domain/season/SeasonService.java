package io.github.binout.soccer.domain.season;

import io.github.binout.soccer.domain.date.*;
import io.github.binout.soccer.domain.event.FriendlyMatchPlanned;
import io.github.binout.soccer.domain.event.LeagueMatchPlanned;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import io.github.binout.soccer.domain.season.match.LeagueMatch;
import io.github.binout.soccer.domain.season.match.Match;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SeasonService {

    @Inject
    PlayerRepository playerRepository;

    @Inject
    SeasonRepository seasonRepository;

    @Inject
    FriendlyMatchDateRepository friendlyMatchDateRepository;

    @Inject
    LeagueMatchDateRepository leagueMatchDateRepository;

    @Inject
    Event<FriendlyMatchPlanned> friendlyMatchPlannedEvent;

    @Inject
    Event<LeagueMatchPlanned> leagueMatchPlannedEvent;

    public LeagueMatch planLeagueMatch(Season season, LeagueMatchDate date) {
        TreeMap<Integer, List<Player>> treeMap = computeGamesPlayed(date, playerRepository.all().filter(Player::isPlayerLeague), leagueCounter(season));
        LeagueMatch leagueMatch = season.addLeagueMatch(date, extractPlayers(treeMap, LeagueMatch.MAX_PLAYERS, true));
        leagueMatchPlannedEvent.fire(new LeagueMatchPlanned(leagueMatch, getSubstitutes(season, leagueMatch)));
        return leagueMatch;
    }

    public void substitutePlayer(Season season, Match match, Player player) {
        Player by = getSubstitute(season, match);
        MatchDate matchDate = getMatchDate(match).orElseThrow(() -> new IllegalArgumentException("Unknown match date"));
        if (matchDate.isAbsent(by)) {
            throw new IllegalArgumentException(by.name() + " is not present for this date");
        }
        match.replacePlayer(player, by);
        matchDate.absent(player);
    }

    private Player getSubstitute(Season season, Match match) {
        List<Player> substitutes = getSubstitutes(season, match);
        if (substitutes.isEmpty()) {
            throw new IllegalArgumentException("No substitutes available");
        }
        return substitutes.iterator().next();
    }

    private Optional<? extends MatchDate> getMatchDate(Match match) {
        LocalDate date = match.date();
        if (match instanceof FriendlyMatch) {
            return friendlyMatchDateRepository.byDate(date.getYear(), date.getMonth(), date.getDayOfMonth());
        } else {
            return leagueMatchDateRepository.byDate(date.getYear(), date.getMonth(), date.getDayOfMonth());
        }

    }

    public FriendlyMatch planFriendlyMatch(Season season, FriendlyMatchDate date) {
        TreeMap<Integer, List<Player>> treeMap = computeGamesPlayed(date, playerRepository.all(), globalCounter(season));
        FriendlyMatch friendlyMatch = season.addFriendlyMatch(date, extractPlayers(treeMap, FriendlyMatch.MAX_PLAYERS, false));
        friendlyMatchPlannedEvent.fire(new FriendlyMatchPlanned(friendlyMatch, getSubstitutes(season, friendlyMatch)));
        return friendlyMatch;
    }

    private TreeMap<Integer, List<Player>> computeGamesPlayed(MatchDate date, Stream<Player> players, Function<Player, Integer> counter) {
        Map<Integer, List<Player>> playersByGamesPlayed = players.filter(date::isPresent).collect(Collectors.groupingBy(counter));
        return new TreeMap<>(playersByGamesPlayed);
    }

    private Set<Player> extractPlayers(TreeMap<Integer, List<Player>> treeMap, int maxPlayers, boolean goalPriority) {
        Set<Player> players = new HashSet<>();
        if (goalPriority) {
            treeMap.values().stream().flatMap(Collection::stream).filter(Player::isGoalkeeper).forEach(players::add);
        }
        Iterator<Map.Entry<Integer, List<Player>>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext() && teamIsNotFull(players, maxPlayers)) {
            Map.Entry<Integer, List<Player>> entry = iterator.next();
            List<Player> playersForCurrentCount = entry.getValue();
            Collections.shuffle(playersForCurrentCount); // randomize list
            Iterator<Player> playerIterator = playersForCurrentCount.iterator();
            while (playerIterator.hasNext() && teamIsNotFull(players, maxPlayers)) {
                players.add(playerIterator.next());
            }
        }
        return players;
    }

    private boolean teamIsNotFull(Set<Player> players, int maxPlayers) {
        return players.size() < maxPlayers;
    }

    public List<Player> getSubstitutes(Season season, Match match) {
        SeasonStatistics statistics = season.statistics();
        MatchDate date = getMatchDate(match).orElseThrow(() -> new IllegalArgumentException("Unknown match date"));
        List<String> players = match.players().collect(Collectors.toList());
        Comparator<Player> gamesPlayedComparator = getPlayerComparator(season, match);
        return playerRepository.all()
                .filter(date::isPresent)
                .filter(p -> !players.contains(p.name()))
                .sorted(gamesPlayedComparator)
                .collect(Collectors.toList());
    }

    private Comparator<Player> getPlayerComparator(Season season, Match match) {
        Function<Player, Integer> counter = match instanceof LeagueMatch ? leagueCounter(season) : globalCounter(season);
        return (p1, p2) -> Integer.compare(counter.apply(p1), counter.apply(p2));
    }

    public List<FriendlyMatchDate> friendlyMatchDatesToPlan(Season season) {
        Set<LocalDate> dates = season.friendlyMatches().map(FriendlyMatch::date).collect(Collectors.toSet());
        return matchDatesToPlan(dates, friendlyMatchDateRepository.all());
    }

    public List<LeagueMatchDate> leagueMatchDatesToPlan(Season season) {
        Set<LocalDate> dates = season.leagueMatches().map(LeagueMatch::date).collect(Collectors.toSet());
        return matchDatesToPlan(dates, leagueMatchDateRepository.all());
    }

    private <T extends MatchDate> List<T> matchDatesToPlan(Set<LocalDate> dates, Stream<T> allDates) {
        LocalDate now = LocalDate.now();
        return allDates
                .filter(d -> d.date().isAfter(now) || d.date().isEqual(now))
                .filter(d -> !dates.contains(d.date()))
                .filter(MatchDate::canBePlanned)
                .collect(Collectors.toList());
    }

    private static Function<Player, Integer> leagueCounter(Season season) {
        return p -> season.statistics().leagueMatchPlayed(p);
    }

    private static Function<Player, Integer> globalCounter(Season season) {
        return p -> season.statistics().matchPlayed(p);
    }
}
