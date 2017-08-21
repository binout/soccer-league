package io.github.binout.soccer.domain.season;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.domain.player.Player;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SeasonTest {

    private Set<Player> players(String... names) {
        return Arrays.stream(names).map(Player::new).collect(Collectors.toSet());
    }

    private void addLeagueMatch(Season season, LeagueMatchDate date, Set<Player> players) {
        players.forEach(date::present);
        season.addLeagueMatch(date, players);
    }

    private void addFriendlyMatch(Season season, FriendlyMatchDate date, Set<Player> players) {
        players.forEach(date::present);
        season.addFriendlyMatch(date, players);
    }

    @Test
    public void compute_games_played_for_1_league_match() {
        Season season = new Season("2016");
        LeagueMatchDate date = MatchDate.newDateForLeague(2016, Month.MAY, 17);
        addLeagueMatch(season, date, players("benoit", "nicolas", "julien", "pierre", "mat"));

        SeasonStatistics stats = season.statistics();

        assertThat(stats.nbPlayers()).isEqualTo(5);
        assertThat(stats.matchPlayed(new Player("benoit"))).isEqualTo(1);
        assertThat(stats.matchPlayed(new Player("nicolas"))).isEqualTo(1);
        assertThat(stats.matchPlayed(new Player("julien"))).isEqualTo(1);
        assertThat(stats.matchPlayed(new Player("pierre"))).isEqualTo(1);
        assertThat(stats.matchPlayed(new Player("mat"))).isEqualTo(1);
    }

    @Test
    public void compute_games_played_for_1_league_match_and_1_friendly_match() {
        Season season = new Season("2016");

        LeagueMatchDate leagueMatchDate = MatchDate.newDateForLeague(2016, Month.MAY, 17);
        addLeagueMatch(season, leagueMatchDate, players("benoit","nicolas","julien","pierre","mat"));

        FriendlyMatchDate friendlyMatchDate = MatchDate.newDateForFriendly(2016, Month.MAY, 24);
        Set<Player> players = players("benoit", "nicolas", "julien", "pierre", "mat");
        players.addAll(players("flo","gauthier","fabien","guillaume","sebastien"));
        addFriendlyMatch(season, friendlyMatchDate, players);

        SeasonStatistics stats = season.statistics();

        assertThat(stats.nbPlayers()).isEqualTo(10);
        assertThat(stats.matchPlayed(new Player("benoit"))).isEqualTo(2);
        assertThat(stats.matchPlayed(new Player("nicolas"))).isEqualTo(2);
        assertThat(stats.matchPlayed(new Player("julien"))).isEqualTo(2);
        assertThat(stats.matchPlayed(new Player("pierre"))).isEqualTo(2);
        assertThat(stats.matchPlayed(new Player("mat"))).isEqualTo(2);

        assertThat(stats.matchPlayed(new Player("flo"))).isEqualTo(1);
        assertThat(stats.matchPlayed(new Player("gauthier"))).isEqualTo(1);
        assertThat(stats.matchPlayed(new Player("fabien"))).isEqualTo(1);
        assertThat(stats.matchPlayed(new Player("guillaume"))).isEqualTo(1);
        assertThat(stats.matchPlayed(new Player("sebastien"))).isEqualTo(1);
    }

    @Test
    public void compute_season_before_30_september() {
        assertThat(Season.computeSeason(LocalDate.of(2017, 9, 20))).isEqualTo("2016-2017");
    }

    @Test
    public void compute_season_after_30_september() {
        assertThat(Season.computeSeason(LocalDate.of(2017, 10, 1))).isEqualTo("2017-2018");
    }
}