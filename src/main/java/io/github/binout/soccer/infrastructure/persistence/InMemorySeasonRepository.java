package io.github.binout.soccer.infrastructure.persistence;

import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
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

}
