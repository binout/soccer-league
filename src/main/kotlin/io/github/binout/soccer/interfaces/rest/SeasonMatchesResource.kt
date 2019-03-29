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
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("rest/seasons/{name}/matches")
class SeasonMatchesResource(
        val getFriendlyMatches: GetFriendlyMatches,
        val getNextFriendlyMatches: GetNextFriendlyMatches,
        val addFriendlyMatch: AddFriendlyMatch,
        val getLeagueMatches: GetLeagueMatches,
        val getNextLeagueMatches: GetNextLeagueMatches,
        val addLeagueMatch: AddLeagueMatch,
        val getToPlanFriendlyMatches: GetToPlanFriendlyMatches,
        val getToPlanLeagueMatches: GetToPlanLeagueMatches,
        val substitutePlayerInFriendlyMatches: SubstitutePlayerInFriendlyMatches,
        val substitutePlayerInLeagueMatches: SubstitutePlayerInLeagueMatches) {

    @GetMapping("friendly")
    fun getFriendlyMatch(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        return ResponseEntity.ok(getFriendlyMatches.execute(seasonName).map { this.toRestMatch(it) })
    }

    @PutMapping("friendly/{dateParam}")
    fun putFriendlyMatch(@PathVariable("name") name: String, @PathVariable("dateParam") dateParam: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        val date = dateParam.toRestDate()
        addFriendlyMatch.execute(seasonName, date.year, date.month, date.day)
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("friendly/next")
    fun getNextFriendly(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        return ResponseEntity.ok(getNextFriendlyMatches.execute(seasonName).map{ this.toRestMatch(it) })
    }

    @GetMapping("friendly/to-plan")
    fun friendlyToPlan(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        return ResponseEntity.ok(getToPlanFriendlyMatches.execute(seasonName)
                .map { it.date.toRestMatch() })
    }

    @DeleteMapping("friendly/{dateParam}/players/{playerName}")
    fun susbstitutePlayerFriendly(@PathVariable("name") name: String, @PathVariable("dateParam") dateParam: String, @PathVariable("playerName") playerName: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        val date = dateParam.toRestDate()
        substitutePlayerInFriendlyMatches.execute(seasonName, date.asLocalDate(), playerName)
        return ResponseEntity.ok().build<Any>()
    }


    @GetMapping("league")
    fun getLeagueMatch(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        return ResponseEntity.ok(getLeagueMatches.execute(seasonName).map { this.toRestMatch(it) })
    }

    @PutMapping("league/{dateParam}")
    fun putLeagueMatch(@PathVariable("name") name: String, @PathVariable("dateParam") dateParam: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        val date = dateParam.toRestDate()
        addLeagueMatch.execute(seasonName, date.year, date.month, date.day)
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("league/next")
    fun getNextLeague(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        return ResponseEntity.ok(getNextLeagueMatches.execute(seasonName).map { this.toRestMatch(it) })
    }

    @GetMapping("league/to-plan")
    fun leagueToPlan(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        return ResponseEntity.ok(getToPlanLeagueMatches.execute(seasonName).map { it.date.toRestMatch() })
    }

    @DeleteMapping("league/{dateParam}/players/{playerName}")
    fun susbstitutePlayerLeague(@PathVariable("name") name: String, @PathVariable("dateParam") dateParam: String, @PathVariable("playerName") playerName: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        val date = dateParam.toRestDate()
        substitutePlayerInLeagueMatches.execute(seasonName, date.asLocalDate(), playerName)
        return ResponseEntity.ok().build<Any>()
    }

    private fun toRestMatch(m: Pair<Match<*>, List<Player>>): RestMatch = m.let { (match, players) ->
        val restMatch = match.date.toRestMatch()
        restMatch.hasMinimumPlayer = match.hasMinimumPlayer()
        match.players().values().forEach { restMatch.players.add(it) }
        players.map { it.name }.values().forEach { restMatch.subs.add(it) }
        return restMatch
    }
}
