package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.application.player.GetAllPlayers
import io.github.binout.soccer.application.season.AddSeason
import io.github.binout.soccer.application.season.GetAllSeasons
import io.github.binout.soccer.application.season.GetSeason
import io.github.binout.soccer.application.season.GetSeasonStats
import io.github.binout.soccer.domain.season.SeasonStatistics
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("rest/seasons")
class SeasonsResource (
        val getAllSeasons: GetAllSeasons,
        val addSeason: AddSeason,
        val getSeason: GetSeason,
        val getSeasonStats: GetSeasonStats,
        val getAllPlayers: GetAllPlayers) {

    @GetMapping
    fun all(): List<RestSeason> = getAllSeasons.execute().map { it.toRestModel() }

    @PutMapping("{name}")
    fun put(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        addSeason.execute(seasonName)
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("{name}")
    fun get(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        return getSeason.execute(seasonName)
                .map { RestSeason(it.name()) }
                .map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("{name}/stats")
    fun stats(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        return getSeasonStats.execute(seasonName)
                .map { toRestStatList(it) }
                .map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.notFound().build())
    }

    private fun toRestStatList(s: SeasonStatistics): List<RestStat> {
        return getAllPlayers.execute()
                .map { s.toRestStat(it) }
                .sortedByDescending { it.nbMatches }
    }

}
