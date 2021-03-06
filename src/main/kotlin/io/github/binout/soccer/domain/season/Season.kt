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

import io.github.binout.soccer.domain.date.FriendlyMatchDate
import io.github.binout.soccer.domain.date.LeagueMatchDate
import io.github.binout.soccer.domain.player.Player
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoField

class Season(val name: String) {

    private val friendlyMatches: MutableSet<FriendlyMatch> = mutableSetOf()
    private val leagueMatches: MutableSet<LeagueMatch> = mutableSetOf()

    fun friendlyMatches(): List<FriendlyMatch> = friendlyMatches.sortedBy { it.date }

    fun leagueMatches(): List<LeagueMatch> = leagueMatches.sortedBy { it.date }

    fun addFriendlyMatch(date: FriendlyMatchDate, players: Set<Player>): FriendlyMatch {
        val match = Match.newFriendlyMatch(date, players)
        this.friendlyMatches.add(match)
        return match
    }

    fun cancelFriendlyMatch(friendlyMatchDate: FriendlyMatchDate) {
        this.friendlyMatches.removeIf { it.date == friendlyMatchDate.date }
    }

    fun addLeagueMatch(date: LeagueMatchDate, players: Set<Player>): LeagueMatch {
        val match = Match.newLeagueMatch(date, players)
        this.leagueMatches.add(match)
        return match
    }

    fun cancelLeagueMatch(leagueMatchDate: LeagueMatchDate) {
        this.leagueMatches.removeIf { it.date == leagueMatchDate.date }
    }

    fun matches() = friendlyMatches + leagueMatches

    fun statistics(): SeasonStatistics = SeasonStatistics(this)

    companion object {
        private val START_MONTH = System.getenv("START_MONTH")
                ?.let { Month.of(it.toInt()) }
                ?: Month.OCTOBER
        private val START_DAY = System.getenv("START_DAY")?.toInt() ?: 1

        fun currentSeasonName() = computeSeason(LocalDate.now())

        internal fun computeSeason(now: LocalDate): String {
            val month = now.get(ChronoField.MONTH_OF_YEAR)
            val year = now.get(ChronoField.YEAR)
            val day = now.dayOfMonth
            return if (month > START_MONTH.value || month == START_MONTH.value && day >= START_DAY) {
                "$year-${year + 1}"
            } else {
                "${(year - 1)}-$year"
            }
        }
    }
}

interface SeasonRepository {

    fun replace(season: Season)

    fun byName(name: String): Season?

    fun all(): List<Season>
}
