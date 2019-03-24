package io.github.binout.soccer.domain.date

import java.time.LocalDate

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FriendlyMatchDateTest {

    @Test
    fun date_has_no_present_by_default() {
        assertThat(FriendlyMatchDate(LocalDate.now()).nbPresents()).isZero()
    }

}