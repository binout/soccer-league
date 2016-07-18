package io.github.binout.soccer.domain.season;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import io.github.binout.soccer.domain.season.match.LeagueMatch;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class SeasonService {

    @Inject
    PlayerRepository playerRepository;

    @Inject
    SeasonRepository seasonRepository;

    public void initCurrentSeason(@Observes @Initialized(ApplicationScoped.class) Object init) {
        String currentSeason = Season.currentSeasonName();
        Optional<Season> optSeason = seasonRepository.all().filter(s -> s.name().equals(currentSeason)).findFirst();
        if (!optSeason.isPresent()) {
            seasonRepository.add(new Season(currentSeason));
        }
    }

    public LeagueMatch planLeagueMatch(Season season, LeagueMatchDate date) {
        SeasonStatistics statistics = season.statistics();
        Map<Integer, List<Player>> playersByGamesPlayed = playerRepository.all()
                .filter(date::isPresent)
                .filter(Player::isPlayerLeague)
                .collect(Collectors.groupingBy(statistics::gamesPlayed));
        TreeMap<Integer, List<Player>> treeMap = new TreeMap<>(playersByGamesPlayed);
        return season.addLeagueMatch(date, extractPlayers(treeMap, LeagueMatch.MAX_PLAYERS));
    }

    public FriendlyMatch planFriendlyMatch(Season season, FriendlyMatchDate date) {
        SeasonStatistics statistics = season.statistics();
        Map<Integer, List<Player>> playersByGamesPlayed = playerRepository.all()
                .filter(date::isPresent)
                .collect(Collectors.groupingBy(statistics::gamesPlayed));
        TreeMap<Integer, List<Player>> treeMap = new TreeMap<>(playersByGamesPlayed);
        return season.addFriendlyMatch(date, extractPlayers(treeMap, FriendlyMatch.MAX_PLAYERS));
    }

    public Set<Player> getSubstitutes(Season season, FriendlyMatch match) {
        SeasonStatistics statistics = season.statistics();
        FriendlyMatchDate date = match.friendlyDate();
        List<Player> players = match.players().collect(Collectors.toList());
        return playerRepository.all()
                .filter(date::isPresent)
                .filter(p -> !players.contains(p))
                .sorted((p1, p2) -> Integer.compare(statistics.gamesPlayed(p1), statistics.gamesPlayed(p2)))
                .collect(Collectors.toSet());
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
}
