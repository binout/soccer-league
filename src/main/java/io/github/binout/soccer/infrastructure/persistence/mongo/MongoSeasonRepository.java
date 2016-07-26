package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;
import io.github.binout.soccer.infrastructure.persistence.TransactedScope;
import io.github.binout.soccer.infrastructure.persistence.TransactedScopeEnabled;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Restrictions;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class MongoSeasonRepository implements SeasonRepository {

    @Inject
    MongoSession mongoSession;

    @Override
    public void add(Season season) {
        mongoSession.save(season);
    }

    @Override
    public Optional<Season> byName(String name) {
        Criteria criteria = mongoSession.createCriteria(Season.class);
        criteria.add(Restrictions.equals("name", name));
        return criteria.list().stream().findFirst();    }

    @Override
    public Stream<Season> all() {
        return mongoSession.createCriteria(Season.class).list().stream();
    }
}
