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
package io.github.binout.soccer.domain.player

data class PlayerName(val value: String): Comparable<PlayerName> {
    override fun compareTo(other: PlayerName): Int = value.compareTo(other.value)
}

fun List<PlayerName>.values() = this.map { it.value }

class Player(
        val name: PlayerName,
        var email: String? = null,
        var isPlayerLeague: Boolean = false,
        var isGoalkeeper: Boolean = false) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Player

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

interface PlayerRepository {

    fun add(player: Player)

    fun all(): List<Player>

    fun byName(name: PlayerName): Player?
}
