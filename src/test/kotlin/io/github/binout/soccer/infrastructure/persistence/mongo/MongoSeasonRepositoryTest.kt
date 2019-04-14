package io.github.binout.soccer.infrastructure.persistence.mongo

import io.github.binout.soccer.domain.date.FriendlyMatchDate
import io.github.binout.soccer.domain.date.LeagueMatchDate
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.PlayerName
import io.github.binout.soccer.domain.season.Season
import io.github.binout.soccer.infrastructure.persistence.MongoConfiguration
import io.github.binout.soccer.infrastructure.persistence.MongoSeasonRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month
import java.util.*

class MongoSeasonRepositoryTest {

    private lateinit var repository: MongoSeasonRepository

    @BeforeEach
    fun initRepository() {
        repository = MongoSeasonRepository(MongoConfiguration().database())
    }

    @Test
    fun should_persist_season() {
        repository.replace(Season("2016"))

        val season = repository.byName("2016")
        assertThat(season).isNotNull
        assertThat(season!!.friendlyMatches()).isEmpty()
        assertThat(season.leagueMatches()).isEmpty()
    }

    @Test
    fun should_persist_season_with_matches() {
        val season2017 = Season("2017")

        val friendlyPlayers = players(10)
        val friendlyMatchDate = FriendlyMatchDate(LocalDate.of(2017, Month.APRIL, 12))
        friendlyPlayers.forEach { friendlyMatchDate.present(it)}
        season2017.addFriendlyMatch(friendlyMatchDate, friendlyPlayers)

        val leaguePlayers = players(7)
        val leagueMatchDate = LeagueMatchDate(LocalDate.of(2017, Month.APRIL, 12))
        leaguePlayers.forEach { leagueMatchDate.present(it) }
        season2017.addLeagueMatch(leagueMatchDate, leaguePlayers)

        repository.replace(season2017)

        val season = repository.byName("2017")
        assertThat(season).isNotNull
        assertThat(season!!.friendlyMatches()).hasSize(1)
        assertThat(season.leagueMatches()).hasSize(1)
    }

    private fun players(nb: Int) : Set<Player> = (0 until nb)
            .map { UUID.randomUUID().toString() }
            .map { PlayerName(it) }
            .map { Player(it) }.toSet()
}