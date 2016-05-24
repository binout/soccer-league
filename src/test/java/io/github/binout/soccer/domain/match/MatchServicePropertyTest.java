package io.github.binout.soccer.domain.match;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.infrastructure.persistence.InMemoryPlayerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Month;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitQuickcheck.class)
public class MatchServicePropertyTest {

    static final LeagueMatchDate DATE_FOR_LEAGUE = MatchDate.newDateForLeague(2016, Month.APRIL, 15);
    static final FriendlyMatchDate DATE_FOR_FRIENDLY = MatchDate.newDateForFriendly(2016, Month.APRIL, 15);
    static final Season EMPTY_SEASON = new Season("2016");

    private MatchService matchService;
    private PlayerRepository playerRepository;

    @Before
    public void init() {
        matchService = new MatchService();
        playerRepository = new InMemoryPlayerRepository();
        matchService.playerRepository = playerRepository;
    }

    private void registerPlayers(int nbPlayers, int nbLeaguePlayers) {
        for (int i = 0; i <nbPlayers; i++) {
            playerRepository.add(new Player(UUID.randomUUID().toString()));
        }
        for (int i = 0; i <nbLeaguePlayers; i++) {
            Player player = new Player(UUID.randomUUID().toString());
            player.playsInLeague(true);
            playerRepository.add(player);
        }
    }

    @Property
    public void at_least_5_league_players_for_league_match(
            @InRange(min = "0", max = "100") int nbPlayers,
            @InRange(min = "0", max = "4") int nbLeaguePlayers) {
        registerPlayers(nbPlayers, nbLeaguePlayers);
        try {
            matchService.planLeagueMatch(EMPTY_SEASON, DATE_FOR_LEAGUE);
            throw new AssertionError();
        } catch (IllegalArgumentException e) {
            // Not enough players
        }
    }

    @Property
    public void min_5_players_for_league_match(@InRange(min = "0", max = "100") int nbPlayers) {
        registerPlayers(nbPlayers, 5);
        LeagueMatch leagueMatch = matchService.planLeagueMatch(EMPTY_SEASON, DATE_FOR_LEAGUE);
        assertThat(leagueMatch.players().count()).isEqualTo(5);
    }

    @Property
    public void max_7_players_for_league_match(
            @InRange(min = "0", max = "100") int nbPlayers,
            @InRange(min = "7", max = "100") int nbLeaguePlayers) {
        registerPlayers(nbPlayers, nbLeaguePlayers);
        LeagueMatch leagueMatch = matchService.planLeagueMatch(EMPTY_SEASON, DATE_FOR_LEAGUE);
        assertThat(leagueMatch.players().count()).isEqualTo(7);
    }

    @Property
    public void at_least_10_players_for_friendly_match(@InRange(min = "0", max = "9") int nbPlayers) {
        registerPlayers(nbPlayers, 0);
        try {
            matchService.planFriendlyMatch(EMPTY_SEASON, DATE_FOR_FRIENDLY);
            throw new AssertionError();
        } catch (IllegalArgumentException e) {
            // Not enough players
        }
    }

    @Property
    public void max_10_players_for_friendly_match(@InRange(min = "10", max = "100") int nbPlayers) {
        registerPlayers(nbPlayers, 0);
        FriendlyMatch friendlyMatch = matchService.planFriendlyMatch(EMPTY_SEASON, DATE_FOR_FRIENDLY);
        assertThat(friendlyMatch.players().count()).isEqualTo(10);
    }

    @Test
    public void no_substitutes_if_only_10_players() {
        registerPlayers(10, 0);
        FriendlyMatch friendlyMatch = matchService.planFriendlyMatch(EMPTY_SEASON, DATE_FOR_FRIENDLY);

        Set<Player> substitutes = matchService.getSubstitutes(EMPTY_SEASON, friendlyMatch);

        assertThat(substitutes).isEmpty();
    }

    @Property
    public void substitutes_if_more_than_10_players(@InRange(min = "10", max = "100") int nbPlayers) {
        registerPlayers(nbPlayers, 0);
        FriendlyMatch friendlyMatch = matchService.planFriendlyMatch(EMPTY_SEASON, DATE_FOR_FRIENDLY);

        Set<Player> substitutes = matchService.getSubstitutes(EMPTY_SEASON, friendlyMatch);

        assertThat(substitutes).hasSize(nbPlayers - 10);
    }


}