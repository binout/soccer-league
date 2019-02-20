package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.player.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MongoPlayerRepositoryTest {

    @Rule
    public MongolinkRule mongolinkRule =  MongolinkRule.withPackage(getClass().getPackage().getName());

    private MongoPlayerRepository repository;

    @Before
    public void initRepository() {
        repository = new MongoPlayerRepository(() -> mongolinkRule.getCurrentSession());
    }

    private void persistPlayer(Player leaguePlayer) {
        repository.add(leaguePlayer);
        repository.session().flush();
    }

    @Test
    public void should_persist_player() {
        persistPlayer(new Player("benoit"));

        Optional<Player> benoit = repository.byName("benoit");
        assertThat(benoit).isPresent();
        assertThat(benoit.get().id()).isNotNull();
        assertThat(benoit.get().email()).isEmpty();
        assertThat(benoit.get().isPlayerLeague()).isFalse();
    }

    @Test
    public void should_persist_league_player() {
        Player leaguePlayer = new Player("benoit", "mail@google.com");
        leaguePlayer.playsInLeague(true);
        persistPlayer(leaguePlayer);

        Optional<Player> benoit = repository.byName("benoit");
        assertThat(benoit).isPresent();
        assertThat(benoit.get().id()).isNotNull();
        assertThat(benoit.get().email()).contains("mail@google.com");
        assertThat(benoit.get().isPlayerLeague()).isTrue();
    }

    @Test
    public void should_persist_goalkeeper() {
        Player leaguePlayer = new Player("thomas", "mail@google.com");
        leaguePlayer.playsInLeague(true);
        leaguePlayer.playsAsGoalkeeper(true);
        persistPlayer(leaguePlayer);

        Optional<Player> thomas = repository.byName("thomas");
        assertThat(thomas).isPresent();
        assertThat(thomas.get().id()).isNotNull();
        assertThat(thomas.get().email()).contains("mail@google.com");
        assertThat(thomas.get().isPlayerLeague()).isTrue();
        assertThat(thomas.get().isGoalkeeper()).isTrue();
    }

}