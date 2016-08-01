package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.domain.player.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mongolink.test.MongolinkRule;

import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class MongoLeagueMatchDateRepositoryTest {

    @Rule
    public MongolinkRule mongolinkRule =  MongolinkRule.withPackage(getClass().getPackage().getName());

    private MongoLeagueMatchDateRepository repository;
    private MongoPlayerRepository playerRepository;

    @Before
    public void initRepository() {
        playerRepository = new MongoPlayerRepository(mongolinkRule.getCurrentSession());
        repository = new MongoLeagueMatchDateRepository(mongolinkRule.getCurrentSession());
    }

    @Test
    public void should_persist_date_without_player() {
        repository.add(MatchDate.newDateForLeague(2016, Month.APRIL, 1));
        repository.session().flush();

        Optional<LeagueMatchDate> matchDate = repository.byDate(2016, Month.APRIL, 1);
        assertThat(matchDate).isPresent();
        assertThat(matchDate.get().id()).isNotNull();
        assertThat(matchDate.get().presents().count()).isZero();
    }

    @Test
    public void should_persist_date_with_player() {
        Player benoit = new Player("benoit");
        playerRepository.add(benoit);
        repository.session().flush();

        LeagueMatchDate date = MatchDate.newDateForLeague(2016, Month.APRIL, 1);
        date.present(benoit);
        repository.add(date);
        repository.session().flush();

        Optional<LeagueMatchDate> matchDate = repository.byDate(2016, Month.APRIL, 1);
        assertThat(matchDate).isPresent();
        assertThat(matchDate.get().id()).isNotNull();
        assertThat(matchDate.get().presents().count()).isEqualTo(1);
        assertThat(matchDate.get().presents().findFirst().get()).isEqualTo("benoit");
    }
}