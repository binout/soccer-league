package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository;
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
public class MongoLeagueMatchDateRepository implements LeagueMatchDateRepository {

    @Inject
    MongoSession mongoSession;

    @Override
    public Stream<LeagueMatchDate> all() {
        return mongoSession.createCriteria(LeagueMatchDate.class).list().stream();
    }

    @Override
    public void add(LeagueMatchDate date) {
        mongoSession.save(date);
    }

    @Override
    public Optional<LeagueMatchDate> byDate(int year, Month month, int dayOfMonth) {
        Criteria criteria = mongoSession.createCriteria(LeagueMatchDate.class);
        criteria.add(Restrictions.equals("date", LocalDate.of(year, month, dayOfMonth)));
        return criteria.list().stream().findFirst();
    }
}
