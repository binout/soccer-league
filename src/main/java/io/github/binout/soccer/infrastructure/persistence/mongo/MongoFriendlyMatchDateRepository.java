package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Restrictions;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class MongoFriendlyMatchDateRepository implements FriendlyMatchDateRepository {

    @Inject
    MongoSession mongoSession;

    @Override
    public Stream<FriendlyMatchDate> all() {
        return mongoSession.createCriteria(FriendlyMatchDate.class).list().stream();
    }

    @Override
    public void add(FriendlyMatchDate date) {
        mongoSession.save(date);
    }

    @Override
    public Optional<FriendlyMatchDate> byDate(int year, Month month, int dayOfMonth) {
        Criteria criteria = mongoSession.createCriteria(FriendlyMatchDate.class);
        criteria.add(Restrictions.equals("date", LocalDate.of(year, month, dayOfMonth)));
        return criteria.list().stream().findFirst();
    }
}
