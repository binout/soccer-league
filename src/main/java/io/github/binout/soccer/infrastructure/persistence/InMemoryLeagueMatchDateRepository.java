package io.github.binout.soccer.infrastructure.persistence;

import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@ApplicationScoped
public class InMemoryLeagueMatchDateRepository implements LeagueMatchDateRepository {

    private Map<LocalDate, LeagueMatchDate> dates;

    public InMemoryLeagueMatchDateRepository() {
        dates = new ConcurrentHashMap<>();
    }

    @Override
    public Stream<LeagueMatchDate> all() {
        return dates.values().stream();
    }

    @Override
    public void add(LeagueMatchDate date) {
        dates.put(date.date(), date);
    }

    @Override
    public Optional<LeagueMatchDate> byDate(int year, Month month, int dayOfMonth) {
        return Optional.ofNullable(dates.get(LocalDate.of(year, month, dayOfMonth)));
    }
}
