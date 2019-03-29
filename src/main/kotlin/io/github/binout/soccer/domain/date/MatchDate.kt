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
package io.github.binout.soccer.domain.date

import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.PlayerName
import io.github.binout.soccer.domain.season.FriendlyMatch
import io.github.binout.soccer.domain.season.LeagueMatch

import java.time.LocalDate
import java.time.Month

sealed class MatchDate(val date: LocalDate,
                       private val presents: MutableSet<PlayerName> = mutableSetOf(),
                       private val minPlayer: Int) {

    fun isNowOrFuture(): Boolean = LocalDate.now().let { date.isAfter(it) || date.isEqual(it) }

    fun presents(): List<PlayerName>  = presents.toList()

    fun present(player: Player) { presents.add(player.name) }

    fun absent(player: Player) { presents.remove(player.name) }

    fun nbPresents(): Int = presents.count()

    fun canBePlanned() = nbPresents() >= minPlayer

    fun isAbsent(player: Player): Boolean = !isPresent(player)

    fun isPresent(player: Player): Boolean = presents().any { it == player.name }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MatchDate

        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }

    companion object {

        fun newDateForLeague(year: Int, month: Month, dayOfMonth: Int): LeagueMatchDate {
            return LeagueMatchDate(LocalDate.of(year, month, dayOfMonth))
        }

        fun newDateForFriendly(year: Int, month: Month, dayOfMonth: Int): FriendlyMatchDate {
            return FriendlyMatchDate(LocalDate.of(year, month, dayOfMonth))
        }
    }

}


class LeagueMatchDate(localDate: LocalDate) : MatchDate(localDate, minPlayer =  LeagueMatch.MIN_PLAYERS)

interface LeagueMatchDateRepository {
    fun all(): List<LeagueMatchDate>
    fun replace(date: LeagueMatchDate)
    fun byDate(year: Int, month: Month, dayOfMonth: Int): LeagueMatchDate?
}


class FriendlyMatchDate(localDate: LocalDate) : MatchDate(localDate, minPlayer =  FriendlyMatch.MIN_PLAYERS)

interface FriendlyMatchDateRepository {
    fun all(): List<FriendlyMatchDate>
    fun replace(date: FriendlyMatchDate)
    fun byDate(year: Int, month: Month, dayOfMonth: Int): FriendlyMatchDate?
}



