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

@Path("rest/match-dates/league")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class LeagueMatchDateResource {

    @Inject lateinit var allLeagueMatchDates: GetAllLeagueMatchDates
    @Inject lateinit var addLeagueMatchDate: AddLeagueMatchDate
    @Inject lateinit var getLeagueMatchDate: GetLeagueMatchDate
    @Inject lateinit var addPlayerToLeagueMatchDate: AddPlayerToLeagueMatchDate
    @Inject lateinit var nextLeagueMatchDates: GetNextLeagueMatchDates
    @Inject lateinit var removePlayerToLeagueMatchDate: RemovePlayerToLeagueMatchDate

    @GET
    fun all(): List<RestMatchDate> = allLeagueMatchDates.execute().map { it.toRestModel() }

    @GET
    @Path("next")
    fun next(): List<RestMatchDate> = nextLeagueMatchDates.execute().map { it.toRestModel() }

    @PUT
    @Path("{dateParam}")
    fun put(@PathParam("dateParam") dateParam: String): Response {
        val date = dateParam.toRestDate()
        addLeagueMatchDate.execute(date.year, date.month, date.day)
        return Response.ok().build()
    }

    @GET
    @Path("{dateParam}")
    fun get(@PathParam("dateParam") dateParam: String): Response {
        val date = dateParam.toRestDate()
        return getLeagueMatchDate.execute(date.year, date.month, date.day)
                ?.let { Response.ok(it.toRestModel()).build() }
                ?: Response.status(404).build()
    }

    @PUT
    @Path("{dateParam}/players/{name}")
    fun putPlayers(@PathParam("dateParam") dateParam: String, @PathParam("name") name: String): Response {
        val date = dateParam.toRestDate()
        addPlayerToLeagueMatchDate.execute(name, date.year, date.month, date.day)
        return Response.ok().build()
    }

    @DELETE
    @Path("{dateParam}/players/{name}")
    fun deletePlayers(@PathParam("dateParam") dateParam: String, @PathParam("name") name: String): Response {
        val date = dateParam.toRestDate()
        removePlayerToLeagueMatchDate.execute(name, date.year, date.month, date.day)
        return Response.ok().build()
    }
}
