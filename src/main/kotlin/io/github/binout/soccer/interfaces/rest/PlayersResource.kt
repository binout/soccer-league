package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.application.player.GetAllLeaguePlayers
import io.github.binout.soccer.application.player.GetAllPlayers
import io.github.binout.soccer.application.player.GetPlayer
import io.github.binout.soccer.application.player.ReplacePlayer
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
    fun all() : List<RestPlayer> = getAllPlayers.execute().map { it.toRestModel() }

    @GetMapping("league")
    fun allLeague() : List<RestPlayer> = getAllLeaguePlayers.execute().map { it.toRestModel() }

    @PutMapping("{name}")
    fun put(@PathVariable("name") name: String, @RequestBody restPlayer: RestPlayer): ResponseEntity<*> {
        replacePlayer.execute(name, restPlayer.email, restPlayer.isPlayerLeague, restPlayer.isGoalkeeper)
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("{name}")
    fun get(name: String): ResponseEntity<*> {
        return getPlayer.execute(name)
                .map { it.toRestModel() }
                .map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.notFound().build())
    }

}
