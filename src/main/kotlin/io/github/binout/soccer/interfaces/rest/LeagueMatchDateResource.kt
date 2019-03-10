package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.application.date.*
import io.github.binout.soccer.domain.date.MatchDate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("rest/match-dates/league")
class LeagueMatchDateResource(
        val allLeagueMatchDates: GetAllLeagueMatchDates, 
        val nextLeagueMatchDates: GetNextLeagueMatchDates,
        val addLeagueMatchDate: AddLeagueMatchDate,
        val getLeagueMatchDate: GetLeagueMatchDate,
        val addPlayerToLeagueMatchDate: AddPlayerToLeagueMatchDate,
        val removePlayerToLeagueMatchDate: RemovePlayerToLeagueMatchDate) {

    @GetMapping
    fun all(): List<RestMatchDate> = allLeagueMatchDates.execute().map { this.toRestModel(it) }

    @GetMapping("next")
    operator fun next(): List<RestMatchDate> = nextLeagueMatchDates.execute().map { this.toRestModel(it) }

    @PutMapping("{dateParam}")
    fun put(@PathVariable("dateParam") dateParam: String): ResponseEntity<*> {
        val date = RestDate(dateParam)
        addLeagueMatchDate.execute(date.year(), date.month(), date.day())
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("{dateParam}")
    operator fun get(@PathVariable("dateParam") dateParam: String): ResponseEntity<*> {
        val date = RestDate(dateParam)
        return getLeagueMatchDate.execute(date.year(), date.month(), date.day())
                .map { this.toRestModel(it) }
                .map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.notFound().build())
    }

    private fun toRestModel(m: MatchDate): RestMatchDate {
        val restMatchDate = RestMatchDate(m.date())
        m.presents().forEach { restMatchDate.addPresent(it) }
        restMatchDate.isCanBePlanned = m.canBePlanned()
        return restMatchDate
    }

    @PutMapping("{dateParam}/players/{name}")
    fun putPlayers(@PathVariable("dateParam") dateParam: String, @PathVariable("name") name: String): ResponseEntity<*> {
        val date = RestDate(dateParam)
        addPlayerToLeagueMatchDate.execute(name, date.year(), date.month(), date.day())
        return ResponseEntity.ok().build<Any>()
    }

    @DeleteMapping("{dateParam}/players/{name}")
    fun deletePlayers(@PathVariable("dateParam") dateParam: String, @PathVariable("name") name: String): ResponseEntity<*> {
        val date = RestDate(dateParam)
        removePlayerToLeagueMatchDate.execute(name, date.year(), date.month(), date.day())
        return ResponseEntity.ok().build<Any>()
    }
}
