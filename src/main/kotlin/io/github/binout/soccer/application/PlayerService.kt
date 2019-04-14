package io.github.binout.soccer.application

import io.github.binout.soccer.domain.player.*
import javax.inject.Inject


class GetAllLeaguePlayers {
    @Inject lateinit var playerRepository: PlayerRepository

    fun execute(): List<Player> = playerRepository.all().filter { it.isPlayerLeague }
}


class GetAllPlayers {
    @Inject lateinit var playerRepository: PlayerRepository

    fun execute(): List<Player> = playerRepository.all()
}


class GetAllPlayerStats(@Inject private val playerRepository: PlayerRepository, private val playerStatistics: PlayerStatistics) {

    fun execute(): List<PlayerStats> = playerRepository.all().map { playerStatistics.of(it) }
}


class GetPlayer(@Inject private val playerRepository: PlayerRepository) {

    fun execute(name: String): Player? = playerRepository.byName(PlayerName( name))
}



class ReplacePlayer(@Inject private val playerRepository: PlayerRepository) {

    fun execute(name: String, email: String?, playerLeague: Boolean?, goalkeeper: Boolean?) {
        val playerName = PlayerName(name)
        val player = playerRepository.byName(playerName) ?: Player(playerName)
        email?.let { player.email = it }
        playerLeague?.let { player.isPlayerLeague = it }
        goalkeeper?.let { player.isGoalkeeper = it }
        playerRepository.add(player)
    }
}