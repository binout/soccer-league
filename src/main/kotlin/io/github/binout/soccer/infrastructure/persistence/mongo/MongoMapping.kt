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

import io.github.binout.soccer.domain.date.FriendlyMatchDate
import io.github.binout.soccer.domain.date.LeagueMatchDate
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.season.Season
import io.github.binout.soccer.domain.season.match.FriendlyMatch
import io.github.binout.soccer.domain.season.match.LeagueMatch
import org.mongolink.domain.mapper.AggregateMap
import org.mongolink.domain.mapper.ComponentMap

class MongoFriendlyMatchDateMapping : AggregateMap<FriendlyMatchDate>() {

    override fun map() {
        id().onField("id").natural()
        property().onField("date")
        collection().onField("presents")
    }
}

class MongoFriendlyMatchMapping : ComponentMap<FriendlyMatch>() {

    override fun map() {
        property().onField("friendlyDate")
        collection().onField("players")
    }
}

class MongoLeagueMatchMapping : ComponentMap<LeagueMatch>() {

    override fun map() {
        property().onField("leagueDate")
        collection().onField("players")
    }
}


class MongoLeagueMatchDateMapping : AggregateMap<LeagueMatchDate>() {

    override fun map() {
        id().onField("id").natural()
        property().onField("date")
        collection().onField("presents")
    }
}

class MongoPlayerMapping : AggregateMap<Player>() {

    override fun map() {
        id().onField("id").natural()
        property().onField("name")
        property().onField("email")
        property().onField("isPlayerLeague")
        property().onField("isGoalkeeper")
    }
}

class MongoSeasonMapping : AggregateMap<Season>() {

    override fun map() {
        id().onField("id").natural()
        property().onField("name")
        collection().onField("friendlyMatches")
        collection().onField("leagueMatches")
    }
}
