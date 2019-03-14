package io.github.binout.soccer.infrastructure.persistence

import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.PlayerRepository

import java.util.Comparator
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream

class InMemoryPlayerRepository : PlayerRepository {

    private val players: MutableMap<String, Player> = ConcurrentHashMap()

    override fun add(player: Player) {
        players[player.name] = player
    }

    override fun all(): List<Player> = players.values.sortedBy { it.name }

    override fun byName(name: String): Player? = players[name]
}
