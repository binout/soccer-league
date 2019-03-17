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
import io.github.binout.soccer.domain.player.PlayerRepository
import org.springframework.stereotype.Component
import java.time.Month

@Component
class FriendlyMatchDateRegistration(private val repository: FriendlyMatchDateRepository,
                                    private val playerRepository: PlayerRepository) {

    fun addPlayer(playerName: String, year: Int, month: Month, day: Int) {
        managePlayers(playerName, year, month, day) { obj, player -> obj.present(player) }
    }

    fun removePlayer(playerName: String, year: Int, month: Month, day: Int) {
        managePlayers(playerName, year, month, day) { obj, player -> obj.absent(player) }
    }

    private fun managePlayers(playerName: String, year: Int, month: Month, day: Int, inscription: (FriendlyMatchDate, Player) -> Unit) {
        val friendlyMatchDate = repository.byDate(year, month, day) ?: throw IllegalArgumentException("Date is invalid")
        val player = playerRepository.byName(playerName) ?: throw IllegalArgumentException("Player is invalid")
        inscription(friendlyMatchDate, player)
        repository.replace(friendlyMatchDate)
    }
}

@Component
class LeagueMatchDateRegistration(private val repository: LeagueMatchDateRepository,
                                  private val playerRepository: PlayerRepository) {

    fun addPlayer(playerName: String, year: Int, month: Month, day: Int) {
        managePlayers(playerName, year, month, day) { obj, player -> obj.present(player) }
    }

    fun removePlayer(playerName: String, year: Int, month: Month, day: Int) {
        managePlayers(playerName, year, month, day) { obj, player -> obj.absent(player) }
    }

    private fun managePlayers(playerName: String, year: Int, month: Month, day: Int, inscription: (LeagueMatchDate, Player) -> Unit) {
        val leagueMatchDate = repository.byDate(year, month, day) ?: throw IllegalArgumentException("Date is invalid")
        val player = playerRepository.byName(playerName) ?: throw IllegalArgumentException("Player is invalid")
        inscription(leagueMatchDate, player)
        repository.replace(leagueMatchDate)
    }
}