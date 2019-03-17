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
package io.github.binout.soccer.application.season

import io.github.binout.soccer.domain.player.PlayerRepository
import io.github.binout.soccer.domain.season.MatchPlanning
import io.github.binout.soccer.domain.season.SeasonRepository
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SubstitutePlayerInFriendlyMatches(
        private val playerRepository: PlayerRepository,
        private val seasonRepository: SeasonRepository,
        private val matchPlanning: MatchPlanning) {

    fun execute(seasonName: String, date: LocalDate, playerName: String) {
        val season = seasonRepository.byName(seasonName) ?: throw IllegalArgumentException("Invalid season")
        val player = playerRepository.byName(playerName) ?: throw  IllegalArgumentException("Invalid player")
        val match = season.friendlyMatches().first { m -> m.date == date }
        matchPlanning.substitutePlayer(season, match, player)
    }
}
