/*
 * Copyright 2016 Benoît Prioux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.binout.soccer.domain.season

import io.github.binout.soccer.domain.date.*
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.PlayerRepository
import java.lang.IllegalStateException
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class MatchPlanning() {

    private var seasonRepository: SeasonRepository? = null
    private var playerRepository: PlayerRepository? = null
    private var friendlyMatchDateRepository: FriendlyMatchDateRepository? = null
    private var leagueMatchDateRepository: LeagueMatchDateRepository? = null

    @Inject
    constructor(seasonRepository: SeasonRepository, playerRepository: PlayerRepository,
                friendlyMatchDateRepository: FriendlyMatchDateRepository,
                leagueMatchDateRepository: LeagueMatchDateRepository): this() {
        this.seasonRepository = seasonRepository
        this.playerRepository = playerRepository
        this.friendlyMatchDateRepository = friendlyMatchDateRepository
        this.leagueMatchDateRepository = leagueMatchDateRepository
    }

    fun substitutePlayer(season: Season, match: Match<*>, player: Player) {
        val matchDate = getMatchDate(match) ?: throw IllegalArgumentException("Unknown match date")
        val by = getSubstitutes(season, match).firstOrNull()
        if (by == null) {
            if (!match.removePlayer(player)) {
                throw IllegalStateException("Cannot substitute ${player.name.value}, no player available !")
            }
        } else {
            if (matchDate.isAbsent(by)) {
                throw IllegalArgumentException("${by.name.value} is not present for this date")
            }
            match.replacePlayer(player, by)
        }
        seasonRepository!!.replace(season)
        matchDate.absent(player)
        replaceMatchDate(matchDate)
    }


    private fun replaceMatchDate(matchDate: MatchDate) = when (matchDate) {
        is LeagueMatchDate -> leagueMatchDateRepository!!.replace(matchDate)
        is FriendlyMatchDate -> friendlyMatchDateRepository!!.replace(matchDate)
    }

    private fun getMatchDate(match: Match<*>): MatchDate? {
        val date = match.date
        return when (match) {
            is FriendlyMatch -> friendlyMatchDateRepository!!.byDate(date.year, date.month, date.dayOfMonth)
            is LeagueMatch -> leagueMatchDateRepository!!.byDate(date.year, date.month, date.dayOfMonth)
        }
    }

    fun getSubstitutes(season: Season, match: Match<*>): List<Player> {
        val date = getMatchDate(match) ?: throw IllegalArgumentException("Unknown match date")
        val players = match.players()
        val gamesPlayedComparator = getPlayerComparator(season, match)
        return playerRepository!!.all()
                .filter { date.isPresent(it) }
                .filterNot { it.name in players }
                .sortedWith(gamesPlayedComparator)
    }

    private fun getPlayerComparator(season: Season, match: Match<*>): Comparator<Player> {
        val counter = when (match) {
            is LeagueMatch -> leagueCounter(season)
            is FriendlyMatch -> globalCounter(season)
        }
        return Comparator.comparingInt { counter(it) }
    }

    companion object {

        internal fun leagueCounter(season: Season): (Player) -> Int = { season.statistics().leagueMatchPlayed(it) }
        internal fun globalCounter(season: Season): (Player) -> Int = { season.statistics().matchPlayed(it) }
    }

}
