package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Restrictions;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

public class MongoSeasonRepository extends MongoRepository<Season> implements SeasonRepository {

    @Inject
    MongoSeasonRepository(MongoSession mongoSession) {
        super(mongoSession);
    }

    @Override
    protected Class<Season> clazz() {
        return Season.class;
    }

    @Override
    public void add(Season season) {
        super.add(season, season::id);
    }

    @Override
    public Optional<Season> byName(String name) {
        return findBy(Restrictions.equals("name", name)).findFirst();
    }

    @Override
    public Stream<Season> all() {
        return super.all();
    }
}
