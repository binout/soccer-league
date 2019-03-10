package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.application.season.*
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.season.match.Match
import io.vavr.Tuple2
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
                .map { it.date().toRestMatch() })
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
        return ResponseEntity.ok(getToPlanLeagueMatches.execute(seasonName).stream()
                .map { it.date().toRestMatch() })
    }

    @DeleteMapping("league/{dateParam}/players/{playerName}")
    fun susbstitutePlayerLeague(@PathVariable("name") name: String, @PathVariable("dateParam") dateParam: String, @PathVariable("playerName") playerName: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        val date = dateParam.toRestDate()
        substitutePlayerInLeagueMatches.execute(seasonName, date.asLocalDate(), playerName)
        return ResponseEntity.ok().build<Any>()
    }

    private fun toRestMatch(m: Tuple2<out Match, List<Player>>): RestMatch {
        val restMatch = m._1.date().toRestMatch()
        m._1.players().forEach { restMatch.players.add(it) }
        m._2.stream().map { it.name() }.forEach { restMatch.subs.add(it) }
        return restMatch
    }
}
