package io.github.binout.soccer.domain.player

import org.junit.jupiter.api.Test

import org.assertj.core.api.Assertions.assertThat

class PlayerTest {

    @Test
    fun email_with_blank_is_like_optional_empty() {
        assertThat(Player("sylvain", "").email()).isEmpty
    }
}
