package io.github.binout.soccer.domain.season

import io.github.binout.soccer.domain.date.FriendlyMatchDate
import io.github.binout.soccer.domain.date.LeagueMatchDate
import io.github.binout.soccer.domain.date.MatchDate
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.PlayerName
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

class SeasonTest {

    private fun players(vararg names: String): MutableSet<Player> = names
            .map { PlayerName(it) }
            .map { Player(name = it) }.toMutableSet()

    private fun addLeagueMatch(season: Season, date: LeagueMatchDate, players: Set<Player>) {
        players.forEach { date.present(it) }
        season.addLeagueMatch(date, players)
    }

    private fun addFriendlyMatch(season: Season, date: FriendlyMatchDate, players: Set<Player>) {
        players.forEach(date::present)
        season.addFriendlyMatch(date, players)
    }

    @Test
    fun compute_games_played_for_1_league_match() {
        val season = Season("2016")
        val date = MatchDate.newDateForLeague(2016, Month.MAY, 17)
        addLeagueMatch(season, date, players("benoit", "nicolas", "julien", "pierre", "mat"))

        val stats = season.statistics()

        assertThat(stats.nbPlayers).isEqualTo(5)
        assertThat(stats.matchPlayed(Player(PlayerName("benoit")))).isEqualTo(1)
        assertThat(stats.matchPlayed(Player(PlayerName("nicolas")))).isEqualTo(1)
        assertThat(stats.matchPlayed(Player(PlayerName("julien")))).isEqualTo(1)
        assertThat(stats.matchPlayed(Player(PlayerName("pierre")))).isEqualTo(1)
        assertThat(stats.matchPlayed(Player(PlayerName("mat")))).isEqualTo(1)
    }

    @Test
    fun compute_games_played_for_1_league_match_and_1_friendly_match() {
        val season = Season("2016")

        val leagueMatchDate = MatchDate.newDateForLeague(2016, Month.MAY, 17)
        addLeagueMatch(season, leagueMatchDate, players("benoit", "nicolas", "julien", "pierre", "mat"))

        val friendlyMatchDate = MatchDate.newDateForFriendly(2016, Month.MAY, 24)
        val players = players("benoit", "nicolas", "julien", "pierre", "mat")
        players.addAll(players("flo", "gauthier", "fabien", "guillaume", "sebastien"))
        addFriendlyMatch(season, friendlyMatchDate, players)

        val stats = season.statistics()

        assertThat(stats.nbPlayers).isEqualTo(10)
        assertThat(stats.matchPlayed(Player(PlayerName("benoit")))).isEqualTo(2)
        assertThat(stats.matchPlayed(Player(PlayerName( "nicolas")))).isEqualTo(2)
        assertThat(stats.matchPlayed(Player(PlayerName("julien")))).isEqualTo(2)
        assertThat(stats.matchPlayed(Player(PlayerName("pierre")))).isEqualTo(2)
        assertThat(stats.matchPlayed(Player(PlayerName("mat")))).isEqualTo(2)

        assertThat(stats.matchPlayed(Player(PlayerName("flo")))).isEqualTo(1)
        assertThat(stats.matchPlayed(Player(PlayerName("gauthier")))).isEqualTo(1)
        assertThat(stats.matchPlayed(Player(PlayerName("fabien")))).isEqualTo(1)
        assertThat(stats.matchPlayed(Player(PlayerName("guillaume")))).isEqualTo(1)
        assertThat(stats.matchPlayed(Player(PlayerName("sebastien")))).isEqualTo(1)
    }

    @Test
    fun compute_season_before_30_september() {
        assertThat(Season.computeSeason(LocalDate.of(2017, 8, 20))).isEqualTo("2016-2017")
    }

    @Test
    fun compute_season_after_30_september() {
        assertThat(Season.computeSeason(LocalDate.of(2017, 10, 1))).isEqualTo("2017-2018")
    }
}