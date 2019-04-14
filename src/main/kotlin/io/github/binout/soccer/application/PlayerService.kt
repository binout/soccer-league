package io.github.binout.soccer.application

import io.github.binout.soccer.domain.player.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class GetAllLeaguePlayers {
    @Inject
    lateinit var playerRepository: PlayerRepository

    fun execute(): List<Player> = playerRepository.all().filter { it.isPlayerLeague }
}

@ApplicationScoped
class GetAllPlayers {
    @Inject
    lateinit var playerRepository: PlayerRepository

    fun execute(): List<Player> = playerRepository.all()
}

@ApplicationScoped
class GetAllPlayerStats {
    @Inject
    lateinit var playerRepository: PlayerRepository
    @Inject
    lateinit var playerStatistics: PlayerStatistics

    fun execute(): List<PlayerStats> = playerRepository.all().map { playerStatistics.of(it) }
}

@ApplicationScoped
class GetPlayer {

    @Inject
    lateinit var playerRepository: PlayerRepository

    fun execute(name: String): Player? = playerRepository.byName(PlayerName(name))
}

@ApplicationScoped
class ReplacePlayer {

    @Inject
    lateinit var playerRepository: PlayerRepository

    fun execute(name: String, email: String?, playerLeague: Boolean?, goalkeeper: Boolean?) {
        val playerName = PlayerName(name)
        val player = playerRepository.byName(playerName) ?: Player(playerName)
        email?.let { player.email = it }
        playerLeague?.let { player.isPlayerLeague = it }
        goalkeeper?.let { player.isGoalkeeper = it }
        playerRepository.add(player)
    }
}