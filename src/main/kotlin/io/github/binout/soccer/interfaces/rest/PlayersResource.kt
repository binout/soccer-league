package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.application.player.GetAllLeaguePlayers
import io.github.binout.soccer.application.player.GetAllPlayers
import io.github.binout.soccer.application.player.GetPlayer
import io.github.binout.soccer.application.player.ReplacePlayer
import io.github.binout.soccer.domain.player.Player
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("rest/players")
class PlayersResource(
        val getAllPlayers: GetAllPlayers,
        val getAllLeaguePlayers: GetAllLeaguePlayers,
        val replacePlayer: ReplacePlayer,
        val getPlayer: GetPlayer) {

    @GetMapping
    fun all() : List<RestPlayer> = getAllPlayers.execute().map { p -> toRestModel(p) }

    @GetMapping("league")
    fun allLeague() : List<RestPlayer> = getAllLeaguePlayers.execute().map { p -> toRestModel(p) }

    @PutMapping("{name}")
    fun put(@PathVariable("name") name: String, @RequestBody restPlayer: RestPlayer): ResponseEntity<*> {
        replacePlayer.execute(name, restPlayer.email, restPlayer.isPlayerLeague, restPlayer.isGoalkeeper)
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("{name}")
    fun get(name: String): ResponseEntity<*> {
        return getPlayer.execute(name)
                .map { toRestModel(it) }
                .map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.notFound().build())
    }

    private fun toRestModel(p: Player): RestPlayer {
        val restPlayer = RestPlayer(p.name())
        restPlayer.isPlayerLeague = p.isPlayerLeague
        restPlayer.isGoalkeeper = p.isGoalkeeper
        p.email().ifPresent { restPlayer.email = it }
        return restPlayer
    }

}
