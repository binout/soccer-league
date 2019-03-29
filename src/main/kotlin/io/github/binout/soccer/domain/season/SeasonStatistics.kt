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
import io.github.binout.soccer.domain.player.PlayerName

class SeasonStatistics(season: Season) {

    private val friendlyMatchesPlayed: Map<PlayerName, Int>
    private val leagueMatchesPlayed: Map<PlayerName, Int>
    val nbPlayers: Int

    init {
        val friendlyPlayerGames = season.friendlyMatches().flatMap { it.players() }
        this.friendlyMatchesPlayed = playersByGamePlayed(friendlyPlayerGames)
        val leaguePlayerGames = season.leagueMatches().flatMap { it.players() }
        this.leagueMatchesPlayed = playersByGamePlayed(leaguePlayerGames)
        this.nbPlayers = (friendlyPlayerGames + leaguePlayerGames).distinct().count()
    }

    private fun playersByGamePlayed(allPlayerGames: List<PlayerName>): Map<PlayerName, Int> =
            allPlayerGames.distinct().associate { player -> player to allPlayerGames.count { player == it  } }

    fun matchPlayed(player: Player): Int = leagueMatchPlayed(player) + friendlyMatchPlayed(player)

    fun leagueMatchPlayed(player: Player): Int =
            this.leagueMatchesPlayed.getOrDefault(player.name, 0)

    fun friendlyMatchPlayed(player: Player): Int =
            this.friendlyMatchesPlayed.getOrDefault(player.name, 0)
}
