package io.github.binout.soccer.domain;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.MatchDate;
import org.junit.Test;

import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SeasonTest {

    private Set<Player> players(String... names) {
        return Arrays.stream(names).map(Player::new).collect(Collectors.toSet());
    }

    @Test
    public void computeParticipations_1_league_match() {
        Season season = new Season("2016");
        LeagueMatchDate date = MatchDate.newDateForLeague(2016, Month.MAY, 17);
        season.addLeagueMatch(date, players("benoit","nicolas","julien","pierre","mat"));

        Map<Player, Long> participations = season.computeParticipations();

        assertThat(participations).hasSize(5);
        assertThat(participations.get(new Player("benoit"))).isEqualTo(1);
        assertThat(participations.get(new Player("nicolas"))).isEqualTo(1);
        assertThat(participations.get(new Player("julien"))).isEqualTo(1);
        assertThat(participations.get(new Player("pierre"))).isEqualTo(1);
        assertThat(participations.get(new Player("mat"))).isEqualTo(1);
    }

    @Test
    public void computeParticipations_1_league_match_and_1_friendly_match() {
        Season season = new Season("2016");

        LeagueMatchDate leagueMatchDate = MatchDate.newDateForLeague(2016, Month.MAY, 17);
        season.addLeagueMatch(leagueMatchDate, players("benoit","nicolas","julien","pierre","mat"));

        FriendlyMatchDate friendlyMatchDate = MatchDate.newDateForFriendly(2016, Month.MAY, 24);
        Set<Player> players = players("benoit", "nicolas", "julien", "pierre", "mat");
        players.addAll(players("flo","gauthier","fabien","guillaume","sebastien"));
        season.addFriendlyMatch(friendlyMatchDate, players);

        Map<Player, Long> participations = season.computeParticipations();

        assertThat(participations).hasSize(10);
        assertThat(participations.get(new Player("benoit"))).isEqualTo(2);
        assertThat(participations.get(new Player("nicolas"))).isEqualTo(2);
        assertThat(participations.get(new Player("julien"))).isEqualTo(2);
        assertThat(participations.get(new Player("pierre"))).isEqualTo(2);
        assertThat(participations.get(new Player("mat"))).isEqualTo(2);

        assertThat(participations.get(new Player("flo"))).isEqualTo(1);
        assertThat(participations.get(new Player("gauthier"))).isEqualTo(1);
        assertThat(participations.get(new Player("fabien"))).isEqualTo(1);
        assertThat(participations.get(new Player("guillaume"))).isEqualTo(1);
        assertThat(participations.get(new Player("sebastien"))).isEqualTo(1);
    }
}