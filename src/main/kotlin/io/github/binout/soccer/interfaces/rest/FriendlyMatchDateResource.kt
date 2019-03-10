package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.application.date.*
import io.github.binout.soccer.domain.date.MatchDate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("rest/match-dates/friendly")
class FriendlyMatchDateResource {

    @Autowired
    internal var allFriendlyMatchDates: GetAllFriendlyMatchDates? = null

    @Autowired
    internal var nextFriendlyMatchDates: GetNextFriendlyMatchDates? = null

    @Autowired
    internal var addFriendlyMatchDate: AddFriendlyMatchDate? = null

    @Autowired
    internal var getFriendlyMatchDate: GetFriendlyMatchDate? = null

    @Autowired
    internal var addPlayerToFriendlyMatchDate: AddPlayerToFriendlyMatchDate? = null

    @Autowired
    internal var removePlayerToFriendlyMatchDate: RemovePlayerToFriendlyMatchDate? = null

    @GetMapping
    fun all(): List<RestMatchDate> = allFriendlyMatchDates!!.execute().map { this.toRestModel(it) }

    @GetMapping("next")
    fun next(): List<RestMatchDate> = nextFriendlyMatchDates!!.execute().map{ this.toRestModel(it) }

    @PutMapping("{dateParam}")
    fun put(@PathVariable("dateParam") dateParam: String): ResponseEntity<*> {
        val date = RestDate(dateParam)
        addFriendlyMatchDate!!.execute(date.year(), date.month(), date.day())
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("{dateParam}")
    operator fun get(@PathVariable("dateParam") dateParam: String): ResponseEntity<*> {
        val date = RestDate(dateParam)
        return getFriendlyMatchDate!!.execute(date.year(), date.month(), date.day())
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
        addPlayerToFriendlyMatchDate!!.execute(name, date.year(), date.month(), date.day())
        return ResponseEntity.ok().build<Any>()
    }

    @DeleteMapping("{dateParam}/players/{name}")
    fun deletePlayers(@PathVariable("dateParam") dateParam: String, @PathVariable("name") name: String): ResponseEntity<*> {
        val date = RestDate(dateParam)
        removePlayerToFriendlyMatchDate!!.execute(name, date.year(), date.month(), date.day())
        return ResponseEntity.ok().build<Any>()
    }

}
