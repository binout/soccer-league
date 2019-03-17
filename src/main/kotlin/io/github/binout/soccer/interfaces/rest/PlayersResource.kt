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
package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.application.GetAllLeaguePlayers
import io.github.binout.soccer.application.GetAllPlayers
import io.github.binout.soccer.application.GetPlayer
import io.github.binout.soccer.application.ReplacePlayer
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
                ?.let { ResponseEntity.ok(it.toRestModel()) }
                ?: ResponseEntity.notFound().build<Any>()
    }

}
