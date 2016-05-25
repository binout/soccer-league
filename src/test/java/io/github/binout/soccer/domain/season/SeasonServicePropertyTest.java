package io.github.binout.soccer.domain.season;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.domain.player.PlayersGenerators.LeaguePlayers;
import io.github.binout.soccer.domain.player.PlayersGenerators.Players;
import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import io.github.binout.soccer.domain.season.match.LeagueMatch;
import io.github.binout.soccer.infrastructure.persistence.InMemoryPlayerRepository;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

@RunWith(JUnitQuickcheck.class)
public class SeasonServicePropertyTest {

    static final LeagueMatchDate DATE_FOR_LEAGUE = MatchDate.newDateForLeague(2016, Month.APRIL, 15);
    static final FriendlyMatchDate DATE_FOR_FRIENDLY = MatchDate.newDateForFriendly(2016, Month.APRIL, 15);
    static final Season EMPTY_SEASON = new Season("2016");

    private SeasonService seasonService;
    private PlayerRepository playerRepository;

    @Before
    public void init() {
        seasonService = new SeasonService();
        playerRepository = new InMemoryPlayerRepository();
        seasonService.playerRepository = playerRepository;
    }

    private void addToRepository(List<Player>... nbPlayers) {
        Arrays.stream(nbPlayers).flatMap(Collection::stream).forEach(playerRepository::add);
    }

    @Property
    public void at_least_5_league_players_for_league_match(List<@From(Players.class) Player> nbPlayers, List<@From(LeaguePlayers.class) Player> nbLeaguePlayers) {
        assumeTrue(nbLeaguePlayers.size() < 5);
        addToRepository(nbPlayers, nbLeaguePlayers);

        try {
            seasonService.planLeagueMatch(EMPTY_SEASON, DATE_FOR_LEAGUE);
            throw new AssertionError();
        } catch (IllegalArgumentException e) {
            // Not enough players
        }
    }

    @Property
    public void min_5_max_7_players_for_league_match(List<@From(Players.class) Player> nbPlayers, List<@From(LeaguePlayers.class) Player> nbLeaguePlayers) {
        assumeTrue(nbLeaguePlayers.size() >= 5);
        addToRepository(nbPlayers, nbLeaguePlayers);

        LeagueMatch leagueMatch = seasonService.planLeagueMatch(EMPTY_SEASON, DATE_FOR_LEAGUE);

        assertThat(leagueMatch.players().count()).isBetween(5L, 7L);
    }

    @Property
    public void at_least_10_players_for_friendly_match(List<@From(Players.class) Player> nbPlayers) {
        assumeTrue(nbPlayers.size() < 10);
        addToRepository(nbPlayers);

        try {
            seasonService.planFriendlyMatch(EMPTY_SEASON, DATE_FOR_FRIENDLY);
            throw new AssertionError();
        } catch (IllegalArgumentException e) {
            // Not enough players
        }
    }

    @Property
    public void max_10_players_for_friendly_match(List<@From(Players.class) Player> nbPlayers) {
        assumeTrue(nbPlayers.size() >= 10);
        addToRepository(nbPlayers);

        FriendlyMatch friendlyMatch = seasonService.planFriendlyMatch(EMPTY_SEASON, DATE_FOR_FRIENDLY);

        assertThat(friendlyMatch.players().count()).isEqualTo(10);
    }

    @Property
    public void substitutes_if_more_than_10_players(List<@From(Players.class) Player> nbPlayers) {
        assumeTrue(nbPlayers.size() >= 10);
        addToRepository(nbPlayers);
        FriendlyMatch friendlyMatch = seasonService.planFriendlyMatch(EMPTY_SEASON, DATE_FOR_FRIENDLY);

        Set<Player> substitutes = seasonService.getSubstitutes(EMPTY_SEASON, friendlyMatch);

        assertThat(substitutes).hasSize(nbPlayers.size() - 10);
    }


}