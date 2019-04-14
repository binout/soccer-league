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

import io.github.binout.soccer.application.*
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("rest/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PlayersResource(
        @Inject private val getAllPlayers: GetAllPlayers,
        @Inject private val getAllLeaguePlayers: GetAllLeaguePlayers,
        @Inject private val replacePlayer: ReplacePlayer,
        @Inject private val getPlayer: GetPlayer) {

    @GET
    fun all() : List<RestPlayer> = getAllPlayers.execute().map { it.toRestModel() }

    @GET
    @Path("league")
    fun allLeague() : List<RestPlayer> = getAllLeaguePlayers.execute().map { it.toRestModel() }

    @PUT
    @Path("{name}")
    fun put(@PathParam("name") name: String, restPlayer: RestPlayer): Response {
        replacePlayer.execute(name, restPlayer.email, restPlayer.isPlayerLeague, restPlayer.isGoalkeeper)
        return Response.ok().build()
    }

    @GET
    @Path("{name}")
    fun get(name: String): Response = getPlayer.execute(name)
            ?.let { Response.ok(it.toRestModel()).build() }
            ?: Response.status(404).build()

}

@Path("rest/players-stats")
class PlayerStatsResource(val getAllPlayerStats: GetAllPlayerStats) {

    @GET
    fun all() : List<RestPlayerStat> = getAllPlayerStats.execute().map { it.toRestModel() }

}

