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
package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.domain.date.MatchDate
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.PlayerStats
import io.github.binout.soccer.domain.player.values
import io.github.binout.soccer.domain.season.Season
import io.github.binout.soccer.domain.season.SeasonStatistics
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

data class RestDate(val year: Int, val month: Month, val day: Int) {
    fun asLocalDate(): LocalDate = LocalDate.of(year, month, day)
}

fun String.toRestDate(): RestDate  {
    val accessor = DateTimeFormatter.ISO_LOCAL_DATE.parse(this)
    return RestDate(
            year = accessor.get(ChronoField.YEAR),
            month = Month.of(accessor.get(ChronoField.MONTH_OF_YEAR)),
            day = accessor.get(ChronoField.DAY_OF_MONTH))
}

data class RestMatch(
        var date: String,
        var players: MutableList<String> = mutableListOf(),
        var subs: MutableList<String> = mutableListOf(),
        var hasMinimumPlayer : Boolean = false)

fun LocalDate.toRestMatch() = RestMatch(DateTimeFormatter.ISO_DATE.format(this))


data class RestMatchDate(
        var date: String,
        var presents: MutableList<String> = mutableListOf(),
        var isCanBePlanned: Boolean = false)

fun LocalDate.toRestMatchDate() = RestMatchDate(DateTimeFormatter.ISO_DATE.format(this))

fun MatchDate.toRestModel(): RestMatchDate {
    val restMatchDate = date.toRestMatchDate()
    presents().values().forEach { restMatchDate.presents.add(it) }
    restMatchDate.isCanBePlanned = canBePlanned()
    return restMatchDate
}

data class RestPlayer(
        var name: String,
        var email: String? = null,
        var isPlayerLeague: Boolean = false,
        var isGoalkeeper: Boolean = false)

fun Player.toRestModel() = RestPlayer(name.value, email, isPlayerLeague, isGoalkeeper)

data class RestPlayerStat(
        var name: String,
        var email: String? = null,
        var isPlayerLeague: Boolean = false,
        var isGoalkeeper: Boolean = false,
        var nbSeasons: Int,
        var nbMatches: Int)

fun PlayerStats.toRestModel() = RestPlayerStat(player.name.value, player.email, player.isPlayerLeague, player.isGoalkeeper, nbSeasons, nbMatches)


data class RestSeason(var name: String)

fun Season.toRestModel() = RestSeason(name)

data class RestStat(
        var player: String,
        var nbMatches: Int = 0,
        var nbFriendlyMatches: Int = 0,
        var nbLeagueMatches: Int = 0)

fun SeasonStatistics.toRestStat(p: Player): RestStat = RestStat(
        player = p.name.value,
        nbFriendlyMatches = friendlyMatchPlayed(p),
        nbLeagueMatches = leagueMatchPlayed(p),
        nbMatches = matchPlayed(p))