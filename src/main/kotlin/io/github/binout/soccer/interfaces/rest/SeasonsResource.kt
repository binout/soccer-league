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
                ?.let { ResponseEntity.ok(RestSeason(it.name)) }
                ?: ResponseEntity.notFound().build<Any>()
    }

    @GetMapping("{name}/stats")
    fun stats(@PathVariable("name") name: String): ResponseEntity<*> {
        val seasonName = SeasonName(name).name
        return getSeasonStats.execute(seasonName)
                ?.let { ResponseEntity.ok(toRestStatList(it)) }
                ?: ResponseEntity.notFound().build<Any>()
    }

    private fun toRestStatList(s: SeasonStatistics): List<RestStat> {
        return getAllPlayers.execute()
                .map { s.toRestStat(it) }
                .sortedByDescending { it.nbMatches }
    }

}
