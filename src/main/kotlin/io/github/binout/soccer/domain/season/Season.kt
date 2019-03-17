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

    fun addLeagueMatch(date: LeagueMatchDate, players: Set<Player>): LeagueMatch {
        val match = Match.newLeagueMatch(date, players)
        this.leagueMatches.add(match)
        return match
    }

    fun statistics(): SeasonStatistics = SeasonStatistics(this)

    companion object {

        fun currentSeasonName(): String {
            return computeSeason(LocalDate.now())
        }

        internal fun computeSeason(now: LocalDate): String {
            val month = now.get(ChronoField.MONTH_OF_YEAR)
            val year = now.get(ChronoField.YEAR)
            val day = now.dayOfMonth
            return if (month > Month.SEPTEMBER.value || month == Month.SEPTEMBER.value && day > 3) {
                year.toString() + "-" + (year + 1)
            } else {
                (year - 1).toString() + "-" + year
            }
        }
    }
}

interface SeasonRepository {

    fun replace(season: Season)

    fun byName(name: String): Season?

    fun all(): List<Season>
}
