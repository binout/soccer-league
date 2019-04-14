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

import io.github.binout.soccer.application.GetAllPlayers
import io.github.binout.soccer.application.AddSeason
import io.github.binout.soccer.application.GetAllSeasons
import io.github.binout.soccer.application.GetSeason
import io.github.binout.soccer.application.GetSeasonStats
import io.github.binout.soccer.domain.season.SeasonStatistics
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("rest/seasons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class SeasonsResource {

    @Inject lateinit var getAllSeasons: GetAllSeasons
    @Inject lateinit var addSeason: AddSeason
    @Inject lateinit var getSeason: GetSeason
    @Inject lateinit var getSeasonStats: GetSeasonStats
    @Inject lateinit var getAllPlayers: GetAllPlayers

    @GET
    fun all(): List<RestSeason> = getAllSeasons.execute().map { it.toRestModel() }

    @PUT
    @Path("{name}")
    fun put(@PathParam("name") name: String): Response {
        val seasonName = SeasonName(name).name
        addSeason.execute(seasonName)
        return Response.ok().build()
    }

    @GET
    @Path("{name}")
    fun get(@PathParam("name") name: String): Response {
        val seasonName = SeasonName(name).name
        return getSeason.execute(seasonName)
                ?.let { Response.ok(RestSeason(it.name)).build() }
                ?: Response.status(404).build()
    }

    @GET
    @Path("{name}/stats")
    fun stats(@PathParam("name") name: String): Response {
        val seasonName = SeasonName(name).name
        return getSeasonStats.execute(seasonName)
                ?.let { Response.ok(toRestStatList(it)).build() }
                ?: Response.status(404).build()
    }

    private fun toRestStatList(s: SeasonStatistics): List<RestStat> = getAllPlayers.execute()
            .map { s.toRestStat(it) }
            .sortedByDescending { it.nbMatches }

}
