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
import org.springframework.stereotype.Component
import java.util.*

@Component
class MatchPlanning(
        private val seasonRepository: SeasonRepository,
        private val playerRepository: PlayerRepository,
        private val friendlyMatchDateRepository: FriendlyMatchDateRepository,
        private val leagueMatchDateRepository: LeagueMatchDateRepository) {


    fun substitutePlayer(season: Season, match: Match<*>, player: Player) {
        val by = getSubstitute(season, match)
        val matchDate = getMatchDate(match) ?: throw IllegalArgumentException("Unknown match date")
        if (matchDate.isAbsent(by)) {
            throw IllegalArgumentException(by.name + " is not present for this date")
        }
        match.replacePlayer(player, by)
        seasonRepository.replace(season)
        matchDate.absent(player)
        replaceMatchDate(matchDate)
    }

    private fun getSubstitute(season: Season, match: Match<*>): Player {
        val substitutes = getSubstitutes(season, match)
        if (substitutes.isEmpty()) {
            throw IllegalArgumentException("No substitutes available")
        }
        return substitutes.iterator().next()
    }

    private fun replaceMatchDate(matchDate: MatchDate) {
        when (matchDate) {
            is LeagueMatchDate -> leagueMatchDateRepository.replace(matchDate)
            is FriendlyMatchDate -> friendlyMatchDateRepository.replace(matchDate)
        }
    }

    private fun getMatchDate(match: Match<*>): MatchDate? {
        val date = match.date
        return when (match) {
            is FriendlyMatch -> friendlyMatchDateRepository.byDate(date.year, date.month, date.dayOfMonth)
            else -> leagueMatchDateRepository.byDate(date.year, date.month, date.dayOfMonth)
        }
    }

    fun getSubstitutes(season: Season, match: Match<*>): List<Player> {
        val date = getMatchDate(match) ?: throw IllegalArgumentException("Unknown match date")
        val players = match.players()
        val gamesPlayedComparator = getPlayerComparator(season, match)
        return playerRepository.all()
                .filter { date.isPresent(it) }
                .filterNot { it.name in players }
                .sortedWith(gamesPlayedComparator)
    }

    private fun getPlayerComparator(season: Season, match: Match<*>): Comparator<Player> {
        val counter = if (match is LeagueMatch) leagueCounter(season) else globalCounter(season)
        return Comparator.comparingInt { counter(it) }
    }

    companion object {

        internal fun leagueCounter(season: Season): (Player) -> Int = { season.statistics().leagueMatchPlayed(it) }
        internal fun globalCounter(season: Season): (Player) -> Int = { season.statistics().matchPlayed(it) }
    }

}
