package io.github.binout.soccer.infrastructure.persistence;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class InMemoryFriendlyMatchDateRepository implements FriendlyMatchDateRepository {

    private Map<LocalDate, FriendlyMatchDate> dates;

    public InMemoryFriendlyMatchDateRepository() {
        dates = new ConcurrentHashMap<>();
    }

    @Override
    public void add(FriendlyMatchDate date) {
        dates.put(date.date(), date);
    }

    @Override
    public Optional<FriendlyMatchDate> byDate(int year, Month month, int dayOfMonth) {
        return Optional.ofNullable(dates.get(LocalDate.of(year, month, dayOfMonth)));
    }
}
