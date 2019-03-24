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
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("rest/match-dates/friendly")
class FriendlyMatchDateResource(
        val allFriendlyMatchDates: GetAllFriendlyMatchDates,
        val nextFriendlyMatchDates: GetNextFriendlyMatchDates,
        val addFriendlyMatchDate: AddFriendlyMatchDate,
        val getFriendlyMatchDate: GetFriendlyMatchDate,
        val addPlayerToFriendlyMatchDate: AddPlayerToFriendlyMatchDate,
        val removePlayerToFriendlyMatchDate: RemovePlayerToFriendlyMatchDate) {

    @GetMapping
    fun all(): List<RestMatchDate> = allFriendlyMatchDates.execute().map { it.toRestModel() }

    @GetMapping("next")
    fun next(): List<RestMatchDate> = nextFriendlyMatchDates.execute().map{ it.toRestModel() }

    @PutMapping("{dateParam}")
    fun put(@PathVariable("dateParam") dateParam: String): ResponseEntity<*> {
        val date = dateParam.toRestDate()
        addFriendlyMatchDate.execute(date.year, date.month, date.day)
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("{dateParam}")
    fun get(@PathVariable("dateParam") dateParam: String): ResponseEntity<*> {
        val date = dateParam.toRestDate()
        return getFriendlyMatchDate.execute(date.year, date.month, date.day)
                ?.let{ ResponseEntity.ok(it.toRestModel()) }
                ?: ResponseEntity.notFound().build<Any>()
    }

    @PutMapping("{dateParam}/players/{name}")
    fun putPlayers(@PathVariable("dateParam") dateParam: String, @PathVariable("name") name: String): ResponseEntity<*> {
        val date = dateParam.toRestDate()
        addPlayerToFriendlyMatchDate.execute(name, date.year, date.month, date.day)
        return ResponseEntity.ok().build<Any>()
    }

    @DeleteMapping("{dateParam}/players/{name}")
    fun deletePlayers(@PathVariable("dateParam") dateParam: String, @PathVariable("name") name: String): ResponseEntity<*> {
        val date = dateParam.toRestDate()
        removePlayerToFriendlyMatchDate.execute(name, date.year, date.month, date.day)
        return ResponseEntity.ok().build<Any>()
    }

}
