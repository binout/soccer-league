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
import io.github.binout.soccer.domain.season.match.FriendlyMatch
import java.time.LocalDate
import java.time.Month
import java.util.*

open class FriendlyMatchDate(override val date: LocalDate) : MatchDate {

    val id: String = UUID.randomUUID().toString()
    private val presents: MutableSet<String> = mutableSetOf()

    override fun presents(): List<String> = presents.toList()

    override fun present(player: Player) {
        presents.add(player.name)
    }

    override fun absent(player: Player) {
        presents.remove(player.name)
    }

    override fun canBePlanned(): Boolean = nbPresents() >= FriendlyMatch.MIN_PLAYERS

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FriendlyMatchDate

        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }

}

interface FriendlyMatchDateRepository {

    fun all(): List<FriendlyMatchDate>

    fun add(date: FriendlyMatchDate)

    fun byDate(year: Int, month: Month, dayOfMonth: Int): FriendlyMatchDate?

}
