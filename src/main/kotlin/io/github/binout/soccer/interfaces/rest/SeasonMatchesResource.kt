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
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.values
import io.github.binout.soccer.domain.season.Match
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("rest/seasons/{name}/matches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class SeasonMatchesResource {

        @Inject lateinit var getFriendlyMatches: GetFriendlyMatches
        @Inject lateinit var getNextFriendlyMatches: GetNextFriendlyMatches
        @Inject lateinit var addFriendlyMatch: AddFriendlyMatch
        @Inject lateinit var getLeagueMatches: GetLeagueMatches
        @Inject lateinit var getNextLeagueMatches: GetNextLeagueMatches
        @Inject lateinit var addLeagueMatch: AddLeagueMatch
        @Inject lateinit var getToPlanFriendlyMatches: GetToPlanFriendlyMatches
        @Inject lateinit var getToPlanLeagueMatches: GetToPlanLeagueMatches
        @Inject lateinit var substitutePlayerInFriendlyMatches: SubstitutePlayerInFriendlyMatches
        @Inject lateinit var substitutePlayerInLeagueMatches: SubstitutePlayerInLeagueMatches

    @GET
    @Path("friendly")
    fun getFriendlyMatch(@PathParam("name") name: String): Response {
        val seasonName = SeasonName(name).name
        return Response.ok(getFriendlyMatches.execute(seasonName).map { this.toRestMatch(it) }).build()
    }

    @PUT
    @Path("friendly/{dateParam}")
    fun putFriendlyMatch(@PathParam("name") name: String, @PathParam("dateParam") dateParam: String): Response {
        val seasonName = SeasonName(name).name
        val date = dateParam.toRestDate()
        addFriendlyMatch.execute(seasonName, date.year, date.month, date.day)
        return Response.ok().build()
    }

    @GET
    @Path("friendly/next")
    fun getNextFriendly(@PathParam("name") name: String): Response {
        val seasonName = SeasonName(name).name
        return Response.ok(getNextFriendlyMatches.execute(seasonName).map{ this.toRestMatch(it) }).build()
    }

    @GET
    @Path("friendly/to-plan")
    fun friendlyToPlan(@PathParam("name") name: String): Response {
        val seasonName = SeasonName(name).name
        return Response.ok(getToPlanFriendlyMatches.execute(seasonName)
                .map { it.date.toRestMatch() })
                .build()
    }

    @DELETE
    @Path("friendly/{dateParam}/players/{playerName}")
    fun susbstitutePlayerFriendly(@PathParam("name") name: String, @PathParam("dateParam") dateParam: String, @PathParam("playerName") playerName: String): Response {
        val seasonName = SeasonName(name).name
        val date = dateParam.toRestDate()
        substitutePlayerInFriendlyMatches.execute(seasonName, date.asLocalDate(), playerName)
        return Response.ok().build()
    }


    @GET
    @Path("league")
    fun getLeagueMatch(@PathParam("name") name: String): Response {
        val seasonName = SeasonName(name).name
        return Response.ok(getLeagueMatches.execute(seasonName).map { this.toRestMatch(it) }).build()
    }

    @PUT
    @Path("league/{dateParam}")
    fun putLeagueMatch(@PathParam("name") name: String, @PathParam("dateParam") dateParam: String): Response {
        val seasonName = SeasonName(name).name
        val date = dateParam.toRestDate()
        addLeagueMatch.execute(seasonName, date.year, date.month, date.day)
        return Response.ok().build()
    }

    @GET
    @Path("league/next")
    fun getNextLeague(@PathParam("name") name: String): Response {
        val seasonName = SeasonName(name).name
        return Response.ok(getNextLeagueMatches.execute(seasonName).map { this.toRestMatch(it) }).build()
    }

    @GET
    @Path("league/to-plan")
    fun leagueToPlan(@PathParam("name") name: String): Response {
        val seasonName = SeasonName(name).name
        return Response.ok(getToPlanLeagueMatches.execute(seasonName).map { it.date.toRestMatch() }).build()
    }

    @DELETE
    @Path("league/{dateParam}/players/{playerName}")
    fun susbstitutePlayerLeague(@PathParam("name") name: String, @PathParam("dateParam") dateParam: String, @PathParam("playerName") playerName: String): Response {
        val seasonName = SeasonName(name).name
        val date = dateParam.toRestDate()
        substitutePlayerInLeagueMatches.execute(seasonName, date.asLocalDate(), playerName)
        return Response.ok().build()
    }

    private fun toRestMatch(m: Pair<Match<*>, List<Player>>): RestMatch = m.let { (match, players) ->
        val restMatch = match.date.toRestMatch()
        restMatch.hasMinimumPlayer = match.hasMinimumPlayer()
        match.players().values().forEach { restMatch.players.add(it) }
        players.map { it.name }.values().forEach { restMatch.subs.add(it) }
        return restMatch
    }
}
