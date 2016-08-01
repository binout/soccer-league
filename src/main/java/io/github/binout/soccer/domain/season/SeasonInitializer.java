package io.github.binout.soccer.domain.season;

import io.github.binout.soccer.infrastructure.persistence.TransactedScopeEnabled;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Optional;

/**
 * LECTRA
 *
 * @author b.prioux
 */
@TransactedScopeEnabled
public class SeasonInitializer {

    @Inject
    SeasonRepository seasonRepository;

    public void initCurrentSeason(@Observes @Initialized(ApplicationScoped.class) Object init) {
        String currentSeason = Season.currentSeasonName();
        Optional<Season> optSeason = seasonRepository.all().filter(s -> s.name().equals(currentSeason)).findFirst();
        if (!optSeason.isPresent()) {
            seasonRepository.add(new Season(currentSeason));
        }
    }
}
