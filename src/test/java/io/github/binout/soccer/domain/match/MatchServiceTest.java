package io.github.binout.soccer.domain.match;

import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.infrastructure.persistence.InMemoryPlayerRepository;
import org.junit.Before;
import org.junit.Test;

import java.time.Month;

public class MatchServiceTest {

    private MatchService matchService;
    private PlayerRepository playerRepository;

    @Before
    public void init() {
        matchService = new MatchService();
        playerRepository = new InMemoryPlayerRepository();
        matchService.playerRepository = playerRepository;
    }

    @Test(expected = IllegalArgumentException.class)
    public void empty_repository_no_friendly_match() {
        Season season = new Season("2016");
        matchService.planFriendlyMatch(MatchDate.newDateForFriendly(2016, Month.APRIL, 15), season.statistics());
    }

    @Test(expected = IllegalArgumentException.class)
    public void empty_repository_no_league_match() {
        Season season = new Season("2016");
        matchService.planLeagueMatch(MatchDate.newDateForLeague(2016, Month.APRIL, 15), season.statistics());
    }

}