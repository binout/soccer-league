package io.github.binout.soccer.domain.season;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SeasonInitializer {

    private final SeasonRepository seasonRepository;

    @Autowired
    public SeasonInitializer(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void initCurrentSeason() {
        String currentSeason = Season.currentSeasonName();
        Optional<Season> optSeason = seasonRepository.all().filter(s -> s.name().equals(currentSeason)).findFirst();
        if (!optSeason.isPresent()) {
            seasonRepository.add(new Season(currentSeason));
        }
    }
}
