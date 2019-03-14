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

import java.time.LocalDate
import java.time.Month

interface MatchDate {

    fun isNowOrFuture(): Boolean {
        val now = LocalDate.now()
        return date.isAfter(now) || date.isEqual(now)
    }

    val date: LocalDate

    fun presents(): List<String>

    fun present(player: Player)

    fun absent(player: Player)

    fun canBePlanned(): Boolean

    fun nbPresents(): Int = presents().count()

    fun isAbsent(player: Player): Boolean = !isPresent(player)

    fun isPresent(player: Player): Boolean = presents().any { it == player.name }

    companion object {

        fun newDateForLeague(year: Int, month: Month, dayOfMonth: Int): LeagueMatchDate {
            return LeagueMatchDate(LocalDate.of(year, month, dayOfMonth))
        }

        fun newDateForFriendly(year: Int, month: Month, dayOfMonth: Int): FriendlyMatchDate {
            return FriendlyMatchDate(LocalDate.of(year, month, dayOfMonth))
        }
    }
}


