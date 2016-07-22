package io.github.binout.soccer.domain.season;

import io.github.binout.soccer.domain.date.*;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import io.github.binout.soccer.domain.season.match.LeagueMatch;
import io.github.binout.soccer.domain.season.match.Match;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;
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

    public void initCurrentSeason(@Observes @Initialized(ApplicationScoped.class) Object init) {
        String currentSeason = Season.currentSeasonName();
        Optional<Season> optSeason = seasonRepository.all().filter(s -> s.name().equals(currentSeason)).findFirst();
        if (!optSeason.isPresent()) {
            seasonRepository.add(new Season(currentSeason));
        }
    }

    public LeagueMatch planLeagueMatch(Season season, LeagueMatchDate date) {
        TreeMap<Integer, List<Player>> treeMap = computeGamesPlayed(season, date, playerRepository.all().filter(Player::isPlayerLeague));
        return season.addLeagueMatch(date, extractPlayers(treeMap, LeagueMatch.MAX_PLAYERS));
    }

    public void substitutePlayer(Season season, Match match, Player player) {
        List<Player> substitutes = getSubstitutes(season, match);
        if (substitutes.isEmpty()) {
            throw new IllegalArgumentException("No substitutes available");
        }
        match.substitutePlayer(player, substitutes.iterator().next());
        match.matchDate().absent(player);
    }

    public FriendlyMatch planFriendlyMatch(Season season, FriendlyMatchDate date) {
        TreeMap<Integer, List<Player>> treeMap = computeGamesPlayed(season, date, playerRepository.all());
        return season.addFriendlyMatch(date, extractPlayers(treeMap, FriendlyMatch.MAX_PLAYERS));
    }

    private TreeMap<Integer, List<Player>> computeGamesPlayed(Season season, MatchDate date, Stream<Player> players) {
        SeasonStatistics statistics = season.statistics();
        Map<Integer, List<Player>> playersByGamesPlayed = players
                .filter(date::isPresent)
                .collect(Collectors.groupingBy(statistics::gamesPlayed));
        return new TreeMap<>(playersByGamesPlayed);
    }

    private Set<Player> extractPlayers(TreeMap<Integer, List<Player>> treeMap, int maxPlayers) {
        Set<Player> players = new HashSet<>();
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
        MatchDate date = match.matchDate();
        List<Player> players = match.players().collect(Collectors.toList());
        Comparator<Player> gamesPlayedComparator = (p1, p2) -> Integer.compare(statistics.gamesPlayed(p1), statistics.gamesPlayed(p2));
        return playerRepository.all()
                .filter(date::isPresent)
                .filter(p -> !players.contains(p))
                .sorted(gamesPlayedComparator)
                .collect(Collectors.toList());
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
}
