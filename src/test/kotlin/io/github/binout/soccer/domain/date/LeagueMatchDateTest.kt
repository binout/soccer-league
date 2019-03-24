package io.github.binout.soccer.domain.date

import org.junit.jupiter.api.Test

import java.time.LocalDate

import org.assertj.core.api.Assertions.assertThat

class LeagueMatchDateTest {

    @Test
    fun date_has_no_present_by_default() {
        assertThat(LeagueMatchDate(LocalDate.now()).nbPresents()).isZero()
    }

}