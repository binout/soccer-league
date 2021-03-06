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
package io.github.binout.soccer.domain

import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.season.FriendlyMatch
import io.github.binout.soccer.domain.season.LeagueMatch
import java.time.LocalDate

class FriendlyMatchPlanned(friendlyMatch: FriendlyMatch, substitutes: List<Player>) {

    val date = friendlyMatch.date
    val players = friendlyMatch.players()
    val substitutes = substitutes.map { it.name }
}

class LeagueMatchPlanned(leagueMatch: LeagueMatch, substitutes: List<Player>) {

    val date: LocalDate = leagueMatch.date
    val players = leagueMatch.players()
    val substitutes =  substitutes.map { it.name }

}