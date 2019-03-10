package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.application.season.*
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.season.match.Match
import io.vavr.Tuple2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("rest/seasons/{name}/matches")
class SeasonMatchesResource {

    @Autowired
    internal var getFriendlyMatches: GetFriendlyMatches? = null

    @Autowired
    internal var getNextFriendlyMatches: GetNextFriendlyMatches? = null

    @Autowired
    internal var addFriendlyMatch: AddFriendlyMatch? = null

    @Autowired
    internal var getLeagueMatches: GetLeagueMatches? = null

    @Autowired
    internal var getNextLeagueMatches: GetNextLeagueMatches? = null

    @Autowired
    internal var addLeagueMatch: AddLeagueMatch? = null

    @Autowired
    internal var getToPlanFriendlyMatches: GetToPlanFriendlyMatches? = null

    @Autowired
    internal var getToPlanLeagueMatches: GetToPlanLeagueMatches? = null

    @Autowired
    internal var substitutePlayerInFriendlyMatches: SubstitutePlayerInFriendlyMatches? = null

    @Autowired
    internal var substitutePlayerInLeagueMatches: SubstitutePlayerInLeagueMatches? = null

    @GetMapping("friendly")
    fun getFriendlyMatch(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        return ResponseEntity.ok(getFriendlyMatches!!.execute(seasonName).map { this.toRestMatch(it) })
    }

    @PutMapping("friendly/{dateParam}")
    fun putFriendlyMatch(@PathVariable("name") name: String, @PathVariable("dateParam") dateParam: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        val date = RestDate(dateParam)
        addFriendlyMatch!!.execute(seasonName, date.year(), date.month(), date.day())
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("friendly/next")
    fun getNextFriendly(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        return ResponseEntity.ok(getNextFriendlyMatches!!.execute(seasonName).map{ this.toRestMatch(it) })
    }

    @GetMapping("friendly/to-plan")
    fun friendlyToPlan(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        return ResponseEntity.ok(getToPlanFriendlyMatches!!.execute(seasonName)
                .map { it.date() }
                .map { RestMatch(it) })
    }

    @DeleteMapping("friendly/{dateParam}/players/{playerName}")
    fun susbstitutePlayerFriendly(@PathVariable("name") name: String, @PathVariable("dateParam") dateParam: String, @PathVariable("playerName") playerName: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        val date = RestDate(dateParam)
        substitutePlayerInFriendlyMatches!!.execute(seasonName, date.asLocalDate(), playerName)
        return ResponseEntity.ok().build<Any>()
    }


    @GetMapping("league")
    fun getLeagueMatch(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        return ResponseEntity.ok(getLeagueMatches!!.execute(seasonName).map { this.toRestMatch(it) })
    }

    @PutMapping("league/{dateParam}")
    fun putLeagueMatch(@PathVariable("name") name: String, @PathVariable("dateParam") dateParam: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        val date = RestDate(dateParam)
        addLeagueMatch!!.execute(seasonName, date.year(), date.month(), date.day())
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("league/next")
    fun getNextLeague(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        return ResponseEntity.ok(getNextLeagueMatches!!.execute(seasonName).map { this.toRestMatch(it) })
    }

    @GetMapping("league/to-plan")
    fun leagueToPlan(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        return ResponseEntity.ok(getToPlanLeagueMatches!!.execute(seasonName).stream()
                .map { it.date() }
                .map { RestMatch(it) })
    }

    @DeleteMapping("league/{dateParam}/players/{playerName}")
    fun susbstitutePlayerLeague(@PathVariable("name") name: String, @PathVariable("dateParam") dateParam: String, @PathVariable("playerName") playerName: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        val date = RestDate(dateParam)
        substitutePlayerInLeagueMatches!!.execute(seasonName, date.asLocalDate(), playerName)
        return ResponseEntity.ok().build<Any>()
    }


    private fun toRestMatch(m: Tuple2<out Match, List<Player>>): RestMatch {
        val restMatch = RestMatch(m._1.date())
        m._1.players().forEach { restMatch.addPlayer(it) }
        m._2.stream().map { it.name() }.forEach { restMatch.addSub(it) }
        return restMatch
    }
}
