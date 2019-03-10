package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.application.date.*
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
    fun all(): List<RestMatchDate> = allLeagueMatchDates.execute().map { it.toRestModel() }

    @GetMapping("next")
    fun next(): List<RestMatchDate> = nextLeagueMatchDates.execute().map { it.toRestModel() }

    @PutMapping("{dateParam}")
    fun put(@PathVariable("dateParam") dateParam: String): ResponseEntity<*> {
        val date = dateParam.toRestDate()
        addLeagueMatchDate.execute(date.year, date.month, date.day)
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("{dateParam}")
    fun get(@PathVariable("dateParam") dateParam: String): ResponseEntity<*> {
        val date = dateParam.toRestDate()
        return getLeagueMatchDate.execute(date.year, date.month, date.day)
                .map { it.toRestModel() }
                .map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("{dateParam}/players/{name}")
    fun putPlayers(@PathVariable("dateParam") dateParam: String, @PathVariable("name") name: String): ResponseEntity<*> {
        val date = dateParam.toRestDate()
        addPlayerToLeagueMatchDate.execute(name, date.year, date.month, date.day)
        return ResponseEntity.ok().build<Any>()
    }

    @DeleteMapping("{dateParam}/players/{name}")
    fun deletePlayers(@PathVariable("dateParam") dateParam: String, @PathVariable("name") name: String): ResponseEntity<*> {
        val date = dateParam.toRestDate()
        removePlayerToLeagueMatchDate.execute(name, date.year, date.month, date.day)
        return ResponseEntity.ok().build<Any>()
    }
}
