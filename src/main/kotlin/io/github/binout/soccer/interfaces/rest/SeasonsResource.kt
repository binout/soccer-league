package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.application.player.GetAllPlayers
import io.github.binout.soccer.application.season.AddSeason
import io.github.binout.soccer.application.season.GetAllSeasons
import io.github.binout.soccer.application.season.GetSeason
import io.github.binout.soccer.application.season.GetSeasonStats
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.season.Season
import io.github.binout.soccer.domain.season.SeasonStatistics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("rest/seasons")
class SeasonsResource {

    @Autowired
    internal var getAllSeasons: GetAllSeasons? = null

    @Autowired
    internal var addSeason: AddSeason? = null

    @Autowired
    internal var getSeason: GetSeason? = null

    @Autowired
    internal var getSeasonStats: GetSeasonStats? = null

    @Autowired
    internal var getAllPlayers: GetAllPlayers? = null

    @GetMapping
    fun all(): List<RestSeason> = getAllSeasons!!.execute().map { s -> toRestModel(s) }

    @PutMapping("{name}")
    fun put(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        addSeason!!.execute(seasonName)
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("{name}")
    operator fun get(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        return getSeason!!.execute(seasonName)
                .map { s -> RestSeason(s.name()) }
                .map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("{name}/stats")
    fun stats(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name()
        return getSeasonStats!!.execute(seasonName)
                .map { this.toRestStatList(it) }
                .map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.notFound().build())
    }

    private fun toRestStatList(s: SeasonStatistics): List<RestStat> {
        return getAllPlayers!!.execute()
                .map { p -> toRestStat(s, p) }
                .sortedByDescending { it.nbMatches }
    }

    private fun toRestStat(s: SeasonStatistics, p: Player): RestStat {
        val restStat = RestStat(p.name())
        restStat.nbFriendlyMatches = s.friendlyMatchPlayed(p)
        restStat.nbLeagueMatches = s.leagueMatchPlayed(p)
        restStat.nbMatches = s.matchPlayed(p)
        return restStat
    }

    private fun toRestModel(s: Season): RestSeason {
        val restSeason = RestSeason(s.name())
        //restSeason.addLinks(new RestLink(baseUri + "/" + s.name()));
        return restSeason
    }

}
