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

import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.date.FriendlyMatchDate
import io.github.binout.soccer.domain.date.LeagueMatchDate
import io.github.binout.soccer.domain.date.MatchDate
import io.github.binout.soccer.domain.player.PlayerName

import java.time.LocalDate

sealed class Match<D : MatchDate> (matchDate: D, players: Set<Player>, val minPlayers: Int, val maxPlayers: Int) {

    val date: LocalDate = matchDate.date
    private val players: MutableSet<PlayerName> = checkPlayers(matchDate, players).map { it.name }.toMutableSet()

    fun isNowOrFuture(): Boolean {
        val now = LocalDate.now()
        return date.isAfter(now) || date.isEqual(now)
    }

    fun players(): List<PlayerName> = players.toList()

    fun hasMinimumPlayer() = players.size == minPlayers

    fun replacePlayer(from: Player, by: Player) {
        if (from.name !in players) {
            throw IllegalArgumentException("${from.name.value} is not a player of this match")
        }
        players.remove(from.name)
        players.add(by.name)
    }

    fun removePlayer(player: Player): Boolean {
        if (player.name !in players) {
            throw IllegalArgumentException("${player.name.value} is not a player of this match")
        }
        return if (players.size > minPlayers) {
            players.remove(player.name)
            true
        } else {
            false
        }
    }

    private fun checkPlayers(date: MatchDate, players: Set<Player>): Set<Player> {
        if (players.size < minPlayers || players.size > maxPlayers) {
            throw IllegalArgumentException("Number of players is incorrect for the match")
        }
        if (players.any(date::isAbsent)) {
            throw IllegalArgumentException("Some players are absent for this date")
        }
        return players
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Match<*>

        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }


    companion object {

        fun newLeagueMatch(date: LeagueMatchDate, players: Set<Player>): LeagueMatch {
            return LeagueMatch(date, players)
        }

        fun newFriendlyMatch(date: FriendlyMatchDate, players: Set<Player>): FriendlyMatch {
            return FriendlyMatch(date, players.toMutableSet())
        }
    }
}

class FriendlyMatch(matchDate: FriendlyMatchDate, players: Set<Player>) : Match<FriendlyMatchDate>(matchDate, players, MIN_PLAYERS, MIN_PLAYERS) {
    companion object {
        const val MIN_PLAYERS = 10
        const val MAX_PLAYERS = 10
    }
}

open class LeagueMatch(matchDate: LeagueMatchDate, players: Set<Player>) : Match<LeagueMatchDate>(matchDate, players, MIN_PLAYERS, MAX_PLAYERS) {
    companion object {
        const val MIN_PLAYERS = 5
        const val MAX_PLAYERS = 7
    }
}
