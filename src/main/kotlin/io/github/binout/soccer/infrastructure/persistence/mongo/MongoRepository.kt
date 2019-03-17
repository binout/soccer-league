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
package io.github.binout.soccer.infrastructure.persistence.mongo

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions
import io.github.binout.soccer.domain.date.FriendlyMatchDate
import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository
import io.github.binout.soccer.domain.date.LeagueMatchDate
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.PlayerRepository
import io.github.binout.soccer.domain.season.Season
import io.github.binout.soccer.domain.season.SeasonRepository
import io.github.binout.soccer.domain.season.match.FriendlyMatch
import io.github.binout.soccer.domain.season.match.LeagueMatch
import org.bson.Document
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.*

private fun Date.toLocalDate() = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate()

@Component
class MongoFriendlyMatchDateRepository(mongoDatabase: MongoDatabase):  FriendlyMatchDateRepository {

    private val collection = mongoDatabase.getCollection("friendlymatchdate")

    private fun FriendlyMatchDate.toDocument(): Document = Document()
            .append("date", date)
            .append("presents", presents())

    private fun Document.toFriendlyMatchDate(): FriendlyMatchDate {
        val friendlyMatchDate = FriendlyMatchDate(getDate("date").toLocalDate())
        (get("presents") as List<String>).forEach { friendlyMatchDate.present(Player(it)) }
        return friendlyMatchDate
    }

    override fun add(date: FriendlyMatchDate) {
        collection.replaceOne(eq("date", date.date), date.toDocument(), ReplaceOptions().upsert(true))
    }

    override fun byDate(year: Int, month: Month, dayOfMonth: Int): FriendlyMatchDate? =
            collection.find(eq("date", LocalDate.of(year, month, dayOfMonth))).first()?.toFriendlyMatchDate()

    override fun all(): List<FriendlyMatchDate> = collection.find().map { it.toFriendlyMatchDate() }.toList()
}

@Component
class MongoLeagueMatchDateRepository(mongoDatabase: MongoDatabase) : LeagueMatchDateRepository {

    private val collection = mongoDatabase.getCollection("leaguematchdate")

    private fun LeagueMatchDate.toDocument(): Document = Document()
            .append("date", date)
            .append("presents", presents())

    private fun Document.toLeagueMatchDate(): LeagueMatchDate {
        val leagueMatchDate = LeagueMatchDate(getDate("date").toLocalDate())
        (get("presents") as List<String>).forEach { leagueMatchDate.present(Player(it)) }
        return leagueMatchDate
    }

    private fun Date.toLocalDate() = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate()

    override fun add(date: LeagueMatchDate) {
        collection.replaceOne(eq("date", date.date), date.toDocument(), ReplaceOptions().upsert(true))
    }

    override fun byDate(year: Int, month: Month, dayOfMonth: Int): LeagueMatchDate? =
            collection.find(eq("date", LocalDate.of(year, month, dayOfMonth))).first()?.toLeagueMatchDate()

    override fun all(): List<LeagueMatchDate> = collection.find().map { it.toLeagueMatchDate() }.toList()
}

@Component
class MongoPlayerRepository(mongoDatabase: MongoDatabase) : PlayerRepository {

    private val collection = mongoDatabase.getCollection("player")

    private fun Player.toDocument(): Document = Document()
            .append("name", name)
            .append("email", email)
            .append("isPlayerLeague", isPlayerLeague)
            .append("isGoalkeeper", isGoalkeeper)

    private fun Document.toPlayer(): Player = Player(
            getString("name"),
            getString("email"),
            getBoolean("isPlayerLeague"),
            getBoolean("isGoalkeeper")
    )

    override fun add(player: Player) {
        collection.replaceOne(eq("name", player.name), player.toDocument(), ReplaceOptions().upsert(true))
    }

    override fun byName(name: String): Player? = collection.find(eq("name", name)).first()?.toPlayer()

    override fun all(): List<Player> = collection.find().map { it.toPlayer() }.toList()
}


@Component
class MongoSeasonRepository(mongoDatabase: MongoDatabase) : SeasonRepository {

    private val collection = mongoDatabase.getCollection("season")

    private fun Season.toDocument(): Document = Document()
            .append("name", name)
            .append("friendlyMatches", friendlyMatches().map { it.toDocument() })
            .append("leagueMatches", leagueMatches().map { it.toDocument() })

    private fun FriendlyMatch.toDocument(): Document = Document()
            .append("friendlyDate", date)
            .append("players", players())

    private fun LeagueMatch.toDocument(): Document = Document()
            .append("leagueDate", date)
            .append("players", players())

    private fun Document.toSeason(): Season {
        val season = Season(getString("name"))
        (get("friendlyMatches") as List<Document>).map { it.toFriendlyMatch() }.forEach { season.addFriendlyMatch(it.first, it.second) }
        (get("leagueMatches") as List<Document>).map { it.toLeagueMatch() }.forEach { season.addLeagueMatch(it.first, it.second) }
        return season
    }

    private fun Document.toFriendlyMatch() : Pair<FriendlyMatchDate, Set<Player>> {
        val friendlyMatchDate = FriendlyMatchDate(getDate("friendlyDate").toLocalDate())
        val players = (get("players") as List<String>).map { Player(it) }.toSet()
        players.forEach { friendlyMatchDate.present(it) }
        return friendlyMatchDate to players
    }

    private fun Document.toLeagueMatch() : Pair<LeagueMatchDate, Set<Player>> {
        val leagueMatchDate = LeagueMatchDate(getDate("leagueDate").toLocalDate())
        val players = (get("players") as List<String>).map { Player(it) }.toSet()
        players.forEach { leagueMatchDate.present(it) }
        return leagueMatchDate to players
    }


    override fun add(season: Season) {
        collection.replaceOne(eq("name", season.name), season.toDocument(), ReplaceOptions().upsert(true))
    }

    override fun byName(name: String): Season?  =
        collection.find(eq("name", name)).first()?.toSeason()

    override fun all(): List<Season> = collection.find().map { it.toSeason() }.toList()
}






