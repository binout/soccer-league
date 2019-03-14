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

import io.github.binout.soccer.domain.date.LeagueMatchDate
import io.github.binout.soccer.domain.player.Player

import java.time.LocalDate
import java.util.HashSet
import java.util.Objects
import java.util.stream.Collectors
import java.util.stream.Stream

class LeagueMatch(matchDate: LeagueMatchDate, players: Set<Player>) : Match {

    companion object {
        const val MAX_PLAYERS = 7
        const val MIN_PLAYERS = 5
    }

    override val date: LocalDate = matchDate.date
    private val players: MutableSet<String> = checkPlayers(matchDate, players).map { it.name }.toMutableSet()

    override fun players(): List<String> = players.toList()

    override fun replacePlayer(from: Player, by: Player) {
        if (!players.contains(from.name)) {
            throw IllegalArgumentException(from.name + " is not a player of this match")
        }
        players.remove(from.name)
        players.add(by.name)
    }

    override fun maxPlayers(): Int = MAX_PLAYERS

    override fun minPlayers(): Int = MIN_PLAYERS
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LeagueMatch

        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }
}
