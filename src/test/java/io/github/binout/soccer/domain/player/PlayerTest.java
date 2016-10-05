package io.github.binout.soccer.domain.player;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {

    @Test
    public void email_with_blank_is_like_optional_empty() {
        assertThat(new Player("sylvain", "").email()).isEmpty();
    }
}
