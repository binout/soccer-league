package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.season.Season;
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
public class MongoSeasonRepositoryTest {

    @Rule
    public MongolinkRule mongolinkRule =  MongolinkRule.withPackage(getClass().getPackage().getName());

    private MongoSeasonRepository repository;

    @Before
    public void initRepository() {
        repository = new MongoSeasonRepository();
        repository.mongoSession = mongolinkRule.getCurrentSession();
    }

    @Test
    public void should_persist_season() {
        repository.add(new Season("2016"));
        repository.mongoSession.flush();

        Optional<Season> season = repository.byName("2016");
        assertThat(season).isPresent();
        assertThat(season.get().id()).isNotNull();
        assertThat(season.get().friendlyMatches().count()).isZero();
        assertThat(season.get().leagueMatches().count()).isZero();
    }

}