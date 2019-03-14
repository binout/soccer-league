package io.github.binout.soccer.application.season

import io.github.binout.soccer.domain.season.Season
import io.github.binout.soccer.domain.season.SeasonRepository
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class InitializeSeason(private val seasonRepository: SeasonRepository) {

    @EventListener(ContextRefreshedEvent::class)
    @Transactional
    fun initCurrentSeason() {
        val currentSeason = Season.currentSeasonName()
        val optSeason = seasonRepository.all().firstOrNull { s -> s.name == currentSeason }
        if (optSeason == null) {
            seasonRepository.add(Season(currentSeason))
        }
    }
}
