package io.github.binout.soccer.infrastructure.persistence

import io.github.binout.soccer.domain.date.FriendlyMatchDate
import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository
import io.github.binout.soccer.domain.date.LeagueMatchDate
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.PlayerName
import io.github.binout.soccer.domain.player.PlayerRepository
import io.github.binout.soccer.domain.season.Season
import io.github.binout.soccer.domain.season.SeasonRepository
import java.time.LocalDate
import java.time.Month
import java.util.concurrent.ConcurrentHashMap
import javax.enterprise.inject.Vetoed

@Vetoed
class InMemoryFriendlyMatchDateRepository : FriendlyMatchDateRepository {

    private val dates: MutableMap<LocalDate, FriendlyMatchDate> = ConcurrentHashMap()

    override fun all(): List<FriendlyMatchDate> = dates.values.sortedBy{ it.date }

    override fun replace(date: FriendlyMatchDate) {
        dates[date.date] = date
    }

    override fun byDate(year: Int, month: Month, dayOfMonth: Int): FriendlyMatchDate? =
            dates[LocalDate.of(year, month, dayOfMonth)]
}

@Vetoed
class InMemoryLeagueMatchDateRepository : LeagueMatchDateRepository {

    private val dates: MutableMap<LocalDate, LeagueMatchDate> = ConcurrentHashMap()

    override fun all(): List<LeagueMatchDate> = dates.values.sortedBy{ it.date }

    override fun replace(date: LeagueMatchDate) {
        dates[date.date] = date
    }

    override fun byDate(year: Int, month: Month, dayOfMonth: Int): LeagueMatchDate? =
            dates[LocalDate.of(year, month, dayOfMonth)]
}

@Vetoed
class InMemoryPlayerRepository : PlayerRepository {

    private val players: MutableMap<PlayerName, Player> = ConcurrentHashMap()

    override fun add(player: Player) {
        players[player.name] = player
    }

    override fun all(): List<Player> = players.values.sortedBy { it.name }

    override fun byName(name: PlayerName): Player? = players[name]
}

@Vetoed
class InMemorySeasonRepository : SeasonRepository {

    private val seasons: MutableMap<String, Season> = ConcurrentHashMap()

    override fun replace(season: Season) {
        seasons[season.name] = season
    }

    override fun byName(name: String): Season? = seasons[name]

    override fun all(): List<Season> = seasons.values.toList()

}