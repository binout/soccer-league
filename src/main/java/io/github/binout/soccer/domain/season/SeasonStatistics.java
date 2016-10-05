package io.github.binout.soccer.domain.season;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import io.github.binout.soccer.domain.season.match.LeagueMatch;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SeasonStatistics {

    private final Map<String, Long> friendlyMatchesPlayed;
    private final Map<String, Long> leagueMatchesPlayed;
    private final long nbPlayers;

    SeasonStatistics(Season season) {
        List<String> friendlyPlayerGames = season.friendlyMatches().flatMap(FriendlyMatch::players).collect(Collectors.toList());
        this.friendlyMatchesPlayed = playersByGamePlayed(friendlyPlayerGames);
        List<String> leaguePlayerGames = season.leagueMatches().flatMap(LeagueMatch::players).collect(Collectors.toList());
        this.leagueMatchesPlayed = playersByGamePlayed(leaguePlayerGames);
        this.nbPlayers = Stream.concat(friendlyPlayerGames.stream(), leaguePlayerGames.stream()).distinct().count();

    }

    private Map<String, Long> playersByGamePlayed(List<String> allPlayerGames) {
        return allPlayerGames.stream().distinct().collect(Collectors.toMap(
                Function.identity(),
                p -> allPlayerGames.stream().filter(p::equals).count()));
    }

    public long nbPlayers() {
        return this.nbPlayers;
    }

    public int matchPlayed(Player player) {
        return leagueMatchPlayed(player) + friendlyMatchPlayed(player);
    }

    public int leagueMatchPlayed(Player player) {
        return this.leagueMatchesPlayed.getOrDefault(player.name(), 0L).intValue();
    }

    public int friendlyMatchPlayed(Player player) {
        return this.friendlyMatchesPlayed.getOrDefault(player.name(), 0L).intValue();
    }
}
