package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.player.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mongolink.test.MongolinkRule;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class MongoPlayerRepositoryTest {

    @Rule
    public MongolinkRule mongolinkRule =  MongolinkRule.withPackage(getClass().getPackage().getName());

    private MongoPlayerRepository repository;

    @Before
    public void initRepository() {
        repository = new MongoPlayerRepository();
        repository.mongoSession = mongolinkRule.getCurrentSession();
    }

    private void persistPlayer(Player leaguePlayer) {
        repository.add(leaguePlayer);
        repository.mongoSession.flush();
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

}