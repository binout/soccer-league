package io.github.binout.soccer.infrastructure.persistence;

import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Vetoed;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@ApplicationScoped
@Vetoed
public class InMemorySeasonRepository implements SeasonRepository {

    private Map<String, Season> seasons;

    public InMemorySeasonRepository() {
        seasons = new ConcurrentHashMap<>();
    }

    @Override
    public void add(Season season) {
        seasons.put(season.name(), season);
    }

    @Override
    public Optional<Season> byName(String name) {
        return Optional.ofNullable(seasons.get(name));
    }

    @Override
    public Stream<Season> all() {
        return seasons.values().stream();
    }

}
