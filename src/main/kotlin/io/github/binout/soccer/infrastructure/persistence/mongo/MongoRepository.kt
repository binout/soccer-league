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
import org.bson.Document
import org.mongolink.MongoSession
import org.mongolink.domain.criteria.Restriction
import org.mongolink.domain.criteria.Restrictions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.*

abstract class MongoRepository<T> protected constructor(private val mongoSessionSupplier: () -> MongoSession) {

    protected abstract fun clazz(): Class<T>

    fun session(): MongoSession = mongoSessionSupplier()

    protected fun add(`object`: T, idSupplier: () -> String) {
        val mongoSession = session()
        if (mongoSession.get(idSupplier(), clazz()) == null) {
            mongoSession.save(`object`)
        }
    }

    protected fun findBy(vararg restrictions: Restriction): List<T> {
        val mongoSession = session()
        val criteria = mongoSession.createCriteria(clazz())
        restrictions.forEach { criteria.add(it) }
        return criteria.list() as List<T>
    }

    protected open fun all(): List<T> = session().createCriteria(clazz()).list() as List<T>
}


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

    private fun Date.toLocalDate() = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate()

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
class MongoSeasonRepository(mongoSessionSupplier: () -> MongoSession)
    : MongoRepository<Season>(mongoSessionSupplier), SeasonRepository {

    @Autowired
    constructor(transactionManager: MongoSessionTransactionManager) : this({ transactionManager.doGetTransaction() })

    override fun clazz(): Class<Season> = Season::class.java

    override fun add(season: Season) = super.add(season, season::id)

    override fun byName(name: String): Season? = findBy(Restrictions.equals("name", name)).firstOrNull()

    override fun all(): List<Season> = super.all()
}