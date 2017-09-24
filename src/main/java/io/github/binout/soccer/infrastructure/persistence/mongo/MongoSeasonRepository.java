package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Component
public class MongoSeasonRepository extends MongoRepository<Season> implements SeasonRepository {

    @Autowired
    MongoSeasonRepository(MongoSessionTransactionManager transactionManager) {
        super(() -> transactionManager.doGetTransaction());
    }

    MongoSeasonRepository(Supplier<MongoSession> mongoSessionSupplier) {
        super(mongoSessionSupplier);
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
