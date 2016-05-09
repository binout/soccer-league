package io.github.binout.soccer.domain;

import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SeasonTest {

    @Test
    public void computeParticipations() {
        Season season = new Season("2016");
        season.addLeagueMatch(Instant.now(), Arrays.asList(
                new Player("benoit", "b.prioux@lectra.com"),
                new Player("nicolas", "n.bacot@lectra.com"),
                new Player("julien", "j.fleury@lectra.com"),
                new Player("pierre", "p.julin@lectra.com"),
                new Player("mat", "m.merzereau@lectra.com")
                ));
        Map<Player, Long> participations = season.computeParticipations();

        assertThat(participations).hasSize(5);
        assertThat(participations.get(new Player("benoit", "b.prioux@lectra.com"))).isEqualTo(1);
    }
}