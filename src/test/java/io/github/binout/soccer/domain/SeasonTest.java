package io.github.binout.soccer.domain;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SeasonTest {

    @Test
    public void computeParticipations_1_league_match() {
        Season season = new Season("2016");
        LeagueDate date = new LeagueDate(LocalDate.of(2016, Month.MAY, 17));
        season.addLeagueMatch(date, new HashSet<>(Arrays.asList(
                new Player("benoit"),
                new Player("nicolas"),
                new Player("julien"),
                new Player("pierre"),
                new Player("mat")
                )));
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
        season.addLeagueMatch(new LeagueDate(LocalDate.of(2016, Month.MAY, 17)), new HashSet<>(Arrays.asList(
                new Player("benoit"),
                new Player("nicolas"),
                new Player("julien"),
                new Player("pierre"),
                new Player("mat")
        )));
        season.addFriendlyMatch(new FriendlyDate(LocalDate.of(2016, Month.MAY, 24)), new HashSet<>(Arrays.asList(
                new Player("benoit"),
                new Player("nicolas"),
                new Player("julien"),
                new Player("pierre"),
                new Player("mat"),

                new Player("flo"),
                new Player("gauthier"),
                new Player("fabien"),
                new Player("guillaume"),
                new Player("sebastien")
        )));
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