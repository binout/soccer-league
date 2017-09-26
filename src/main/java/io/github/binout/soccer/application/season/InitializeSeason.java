package io.github.binout.soccer.application.season;

import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

import java.util.Optional;

@Component
public class InitializeSeason {

    private final SeasonRepository seasonRepository;

    @Autowired
    public InitializeSeason(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void initCurrentSeason() {
        String currentSeason = Season.currentSeasonName();
        Optional<Season> optSeason = seasonRepository.all().filter(s -> s.name().equals(currentSeason)).findFirst();
        if (!optSeason.isPresent()) {
            seasonRepository.add(new Season(currentSeason));
        }
    }
}
