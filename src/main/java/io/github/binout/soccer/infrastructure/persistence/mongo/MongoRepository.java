package io.github.binout.soccer.infrastructure.persistence.mongo;

import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Restriction;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class MongoRepository<T> {

    private final Supplier<MongoSession> mongoSessionSupplier;

    protected MongoRepository(Supplier<MongoSession> mongoSessionSupplier) {
        this.mongoSessionSupplier = mongoSessionSupplier;
    }

    protected abstract Class<T> clazz();

    public MongoSession session() {
        return mongoSessionSupplier.get();
    }

    protected void add(T object, Supplier<Object> idSupplier) {
        MongoSession mongoSession = session();
        if (mongoSessionSupplier.get().get(idSupplier.get(), clazz()) == null) {
            mongoSession.save(object);
        }
    }

    protected Stream<T> findBy(Restriction... restrictions) {
        MongoSession mongoSession = session();
        Criteria criteria = mongoSession.createCriteria(clazz());
        Arrays.stream(restrictions).forEach(criteria::add);
        return criteria.list().stream();
    }

    protected Stream<T> all() {
        MongoSession mongoSession = session();
        return mongoSession.createCriteria(clazz()).list().stream();
    }
}
