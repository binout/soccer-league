package io.github.binout.soccer.domain.player

import io.github.binout.soccer.domain.season.SeasonRepository
import org.springframework.stereotype.Component

class PlayerStats(val player: Player, val nbSeasons : Int, val nbMatches: Int)

@Component
class PlayerStatistics(private val seasonRepository: SeasonRepository) {

    fun of(player: Player): PlayerStats {
        val seasons = seasonRepository.all()
        val nbSeasons = seasons.count { season ->
            season.matches().any { it.players().contains(player.name) }
        }
        val nbMatches = seasons.flatMap { it.matches() }.count { it.players().contains(player.name) }
        return PlayerStats(player, nbSeasons, nbMatches)
    }

}