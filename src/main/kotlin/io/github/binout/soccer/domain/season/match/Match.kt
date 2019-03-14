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
package io.github.binout.soccer.domain.season.match

import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.date.FriendlyMatchDate
import io.github.binout.soccer.domain.date.LeagueMatchDate
import io.github.binout.soccer.domain.date.MatchDate

import java.time.LocalDate
import java.util.Objects
import java.util.stream.Stream

interface Match {

    fun isNowOrFuture(): Boolean {
        val now = LocalDate.now()
        return date.isAfter(now) || date.isEqual(now)
    }

    val date: LocalDate

    fun players(): List<String>

    fun maxPlayers(): Int

    fun minPlayers(): Int

    fun replacePlayer(from: Player, by: Player)

    fun checkPlayers(date: MatchDate, players: Set<Player>): Set<Player> {
        if (players.size < minPlayers() || players.size > maxPlayers()) {
            throw IllegalArgumentException("Number of players is incorrect for the match")
        }
        if (players.any(date::isAbsent)) {
            throw IllegalArgumentException("Some players are absent for this date")
        }
        return players
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
