package io.github.binout.soccer.domain.player

import io.github.binout.soccer.domain.date.FriendlyMatchDate
import io.github.binout.soccer.domain.date.LeagueMatchDate
import io.github.binout.soccer.domain.season.Season
import io.github.binout.soccer.domain.season.SeasonRepository
import io.github.binout.soccer.infrastructure.persistence.InMemorySeasonRepository
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

internal class PlayerStatisticsTest : WithAssertions {

    private lateinit var seasonRepository: SeasonRepository
    private lateinit var playerStatistics: PlayerStatistics

    @BeforeEach
    fun init() {
        seasonRepository = InMemorySeasonRepository()
        playerStatistics = PlayerStatistics(seasonRepository)
    }

    @Test
    fun `nb matches is zero if player plays in any match`() {
        val zlatan = newPlayer("Zlatan")
        assertThat(playerStatistics.of(zlatan).nbMatches).isZero()
        assertThat(playerStatistics.of(zlatan).nbSeasons).isZero()
    }

    @Test
    fun `nb matches take account of friendly match`() {
        val zlatan = newPlayer("Zlatan")
        val season = Season("2019")
        val players = `10 players with`(zlatan)
        val date = FriendlyMatchDate(LocalDate.now())
        players.forEach { date.present(it) }
        season.addFriendlyMatch(date, players)
        seasonRepository.replace(season)
        assertThat(playerStatistics.of(zlatan).nbMatches).isEqualTo(1)
        assertThat(playerStatistics.of(zlatan).nbSeasons).isEqualTo(1)
    }

    @Test
    fun `nb matches take account of league match`() {
        val zlatan = newPlayer("Zlatan")
        val season = Season("2019")
        val players = `10 players with`(zlatan)
        val date = LeagueMatchDate(LocalDate.now())
        players.forEach { date.present(it) }
        season.addLeagueMatch(date, players.reversed().take(7).toSet())
        seasonRepository.replace(season)
        assertThat(playerStatistics.of(zlatan).nbMatches).isEqualTo(1)
        assertThat(playerStatistics.of(zlatan).nbSeasons).isEqualTo(1)
    }

    private fun newPlayer(value: String) = Player(PlayerName(value))

    private fun `10 players with`(player: Player): Set<Player> =
        (1 until 10).map { UUID.randomUUID().toString() }
                .map { PlayerName(it) }
                .map { Player(it) }
                .plus(player)
                .toSet()
}