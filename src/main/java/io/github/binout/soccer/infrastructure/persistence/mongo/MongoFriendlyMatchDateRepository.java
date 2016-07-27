package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository;
import org.mongolink.MongoSession;
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
public class MongoFriendlyMatchDateRepository extends MongoRepository<FriendlyMatchDate> implements FriendlyMatchDateRepository {

    @Inject
    MongoFriendlyMatchDateRepository(MongoSession mongoSession) {
        super(mongoSession);
    }

    @Override
    protected Class<FriendlyMatchDate> clazz() {
        return FriendlyMatchDate.class;
    }

    @Override
    public void add(FriendlyMatchDate date) {
        super.add(date, date::id);
    }

    @Override
    public Optional<FriendlyMatchDate> byDate(int year, Month month, int dayOfMonth) {
        return super.findBy(Restrictions.equals("date", LocalDate.of(year, month, dayOfMonth))).findFirst();
    }

    @Override
    public Stream<FriendlyMatchDate> all() {
        return super.all();
    }
}
