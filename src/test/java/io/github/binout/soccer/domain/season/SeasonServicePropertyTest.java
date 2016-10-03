package io.github.binout.soccer.domain.season;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import io.github.binout.soccer.domain.date.*;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.domain.player.PlayersGenerators.LeaguePlayers;
import io.github.binout.soccer.domain.player.PlayersGenerators.Players;
import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import io.github.binout.soccer.domain.season.match.LeagueMatch;
import io.github.binout.soccer.infrastructure.persistence.InMemoryFriendlyMatchDateRepository;
import io.github.binout.soccer.infrastructure.persistence.InMemoryLeagueMatchDateRepository;
import io.github.binout.soccer.infrastructure.persistence.InMemoryPlayerRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.enterprise.event.Event;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

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

        LeagueMatchDateRepository leagueMatchDateRepository = new InMemoryLeagueMatchDateRepository();
        leagueMatchDateRepository.add(DATE_FOR_LEAGUE);
        seasonService.leagueMatchDateRepository = leagueMatchDateRepository;

        FriendlyMatchDateRepository friendlyMatchDateRepository = new InMemoryFriendlyMatchDateRepository();
        friendlyMatchDateRepository.add(DATE_FOR_FRIENDLY);
        seasonService.friendlyMatchDateRepository = friendlyMatchDateRepository;

        seasonService.friendlyMatchPlannedEvent = Mockito.mock(Event.class);
        seasonService.leagueMatchPlannedEvent = Mockito.mock(Event.class);
    }

    private void addToRepository(List<Player>... nbPlayers) {
        Arrays.stream(nbPlayers).flatMap(Collection::stream).forEach(playerRepository::add);
        Arrays.stream(nbPlayers).flatMap(Collection::stream).forEach(DATE_FOR_LEAGUE::present);
        Arrays.stream(nbPlayers).flatMap(Collection::stream).forEach(DATE_FOR_FRIENDLY::present);
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
    public void goalkeeper_priority_for_league_match(List<@From(Players.class) Player> nbPlayers, List<@From(LeaguePlayers.class) Player> nbLeaguePlayers) {
        assumeTrue(nbLeaguePlayers.size() >= 4);
        String goalName = UUID.randomUUID().toString();
        Player goalKeeper = new Player(goalName);
        goalKeeper.playsInLeague(true);
        goalKeeper.playsAsGoalkeeper(true);
        addToRepository(nbPlayers, nbLeaguePlayers, Collections.singletonList(goalKeeper));

        LeagueMatch leagueMatch = seasonService.planLeagueMatch(EMPTY_SEASON, DATE_FOR_LEAGUE);

        assertThat(leagueMatch.players().collect(Collectors.toList())).contains(goalName);
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

        List<Player> substitutes = seasonService.getSubstitutes(EMPTY_SEASON, friendlyMatch);

        assertThat(substitutes).hasSize(nbPlayers.size() - 10);
    }


}