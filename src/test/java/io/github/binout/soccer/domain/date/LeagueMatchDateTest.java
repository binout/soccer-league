package io.github.binout.soccer.domain.date;

import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class LeagueMatchDateTest {

    @Test
    public void date_has_no_present_by_default() {
        assertThat(new LeagueMatchDate(LocalDate.now()).nbPresents()).isZero();
    }

}