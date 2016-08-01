package io.github.binout.soccer.infrastructure.persistence.mongo;

import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Restriction;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class MongoRepository<T> {

    private final MongoSession mongoSession;

    protected MongoRepository(MongoSession mongoSession) {
        this.mongoSession = mongoSession;
    }

    protected abstract Class<T> clazz();

    public MongoSession session() {
        return mongoSession;
    }

    protected void add(T object, Supplier<Object> idSupplier) {
        if (mongoSession.get(idSupplier.get(), clazz()) == null) {
            mongoSession.save(object);
        }
    }

    protected Stream<T> findBy(Restriction... restrictions) {
        Criteria criteria = mongoSession.createCriteria(clazz());
        Arrays.stream(restrictions).forEach(criteria::add);
        return criteria.list().stream();
    }

    protected Stream<T> all() {
        return mongoSession.createCriteria(clazz()).list().stream();
    }
}
