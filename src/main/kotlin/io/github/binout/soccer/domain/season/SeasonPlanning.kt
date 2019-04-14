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
package io.github.binout.soccer.domain.season

import io.github.binout.soccer.domain.date.*
import io.github.binout.soccer.domain.FriendlyMatchPlanned
import io.github.binout.soccer.domain.LeagueMatchPlanned
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.PlayerRepository

import java.time.LocalDate
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Event
import javax.inject.Inject

@ApplicationScoped
class SeasonPlanning() {

    private var seasonRepository: SeasonRepository? = null
    private var playerRepository: PlayerRepository? = null
    private var friendlyMatchDateRepository: FriendlyMatchDateRepository? = null
    private var leagueMatchDateRepository: LeagueMatchDateRepository? = null
    private var matchPlanning: MatchPlanning? = null

    @Inject
    var friendlyMatchPlannedPublisher: Event<FriendlyMatchPlanned>? = null
    @Inject
    var leagueMatchPlannedPublisher: Event<LeagueMatchPlanned>? = null

    @Inject
    constructor(seasonRepository: SeasonRepository,
                playerRepository: PlayerRepository,
                friendlyMatchDateRepository: FriendlyMatchDateRepository,
                leagueMatchDateRepository: LeagueMatchDateRepository,
                matchPlanning: MatchPlanning) : this() {
        this.seasonRepository = seasonRepository
        this.playerRepository = playerRepository
        this.friendlyMatchDateRepository = friendlyMatchDateRepository
        this.leagueMatchDateRepository = leagueMatchDateRepository
        this.matchPlanning = matchPlanning
    }

    fun planLeagueMatch(season: Season, date: LeagueMatchDate): LeagueMatch {
        val treeMap = computeGamesPlayed(date, playerRepository!!.all().filter { it.isPlayerLeague }, MatchPlanning.leagueCounter(season))
        val leagueMatch = season.addLeagueMatch(date, extractPlayers(treeMap, LeagueMatch.MAX_PLAYERS, true))
        seasonRepository!!.replace(season)
        leagueMatchPlannedPublisher?.fire(LeagueMatchPlanned(leagueMatch, matchPlanning!!.getSubstitutes(season, leagueMatch)))
        return leagueMatch
    }

    fun planFriendlyMatch(season: Season, date: FriendlyMatchDate): FriendlyMatch {
        val treeMap = computeGamesPlayed(date, playerRepository!!.all(), MatchPlanning.globalCounter(season))
        val friendlyMatch = season.addFriendlyMatch(date, extractPlayers(treeMap, FriendlyMatch.MAX_PLAYERS, false))
        seasonRepository!!.replace(season)
        friendlyMatchPlannedPublisher?.fire(FriendlyMatchPlanned(friendlyMatch, matchPlanning!!.getSubstitutes(season, friendlyMatch)))
        return friendlyMatch
    }

    private fun computeGamesPlayed(date: MatchDate, players: List<Player>, counter: (Player) -> Int): TreeMap<Int, List<Player>> =
            TreeMap(players.filter { date.isPresent(it) }.groupBy(counter))

    private fun extractPlayers(treeMap: TreeMap<Int, List<Player>>, maxPlayers: Int, goalPriority: Boolean): Set<Player> {
        val players = HashSet<Player>()
        if (goalPriority) {
            treeMap.values.flatten().filter { it.isGoalkeeper }.forEach { players.add(it) }
        }
        val iterator = treeMap.entries.iterator()
        while (iterator.hasNext() && teamIsNotFull(players, maxPlayers)) {
            val entry = iterator.next()
            val playersForCurrentCount = entry.value.shuffled()
            val playerIterator = playersForCurrentCount.iterator()
            while (playerIterator.hasNext() && teamIsNotFull(players, maxPlayers)) {
                players.add(playerIterator.next())
            }
        }
        return players
    }

    private fun teamIsNotFull(players: Set<Player>, maxPlayers: Int): Boolean = players.size < maxPlayers

    fun friendlyMatchDatesToPlan(season: Season): List<FriendlyMatchDate> {
        val dates = season.friendlyMatches().map { it.date }.toSet()
        return matchDatesToPlan(dates, friendlyMatchDateRepository!!.all())
    }

    fun leagueMatchDatesToPlan(season: Season): List<LeagueMatchDate> {
        val dates = season.leagueMatches().map { it.date }.toSet()
        return matchDatesToPlan(dates, leagueMatchDateRepository!!.all())
    }

    private fun <T : MatchDate> matchDatesToPlan(dates: Set<LocalDate>, allDates: List<T>): List<T> {
        val now = LocalDate.now()
        return allDates
                .filter { d -> d.date.isAfter(now) || d.date.isEqual(now) }
                .filter { d -> !dates.contains(d.date) }
                .filter { it.canBePlanned() }
    }


}
