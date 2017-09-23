package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class MongoLeagueMatchDateRepository extends MongoRepository<LeagueMatchDate> implements LeagueMatchDateRepository {

    @Autowired
    MongoLeagueMatchDateRepository(MongoSession mongoSession) {
        super(mongoSession);
    }

    @Override
    protected Class<LeagueMatchDate> clazz() {
        return LeagueMatchDate.class;
    }

    @Override
    public void add(LeagueMatchDate date) {
        super.add(date, date::id);
    }

    @Override
    public Optional<LeagueMatchDate> byDate(int year, Month month, int dayOfMonth) {
        return super.findBy(Restrictions.equals("date", LocalDate.of(year, month, dayOfMonth))).findFirst();
    }

    @Override
    public Stream<LeagueMatchDate> all() {
        return super.all().sorted(Comparator.comparing(LeagueMatchDate::date));
    }
}
