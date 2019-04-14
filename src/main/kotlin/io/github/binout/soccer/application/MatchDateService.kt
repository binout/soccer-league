package io.github.binout.soccer.application

import io.github.binout.soccer.domain.date.*
import java.time.Month
import javax.inject.Inject

class AddFriendlyMatchDate {
    @Inject lateinit var repository: FriendlyMatchDateRepository

    fun execute(year: Int, month: Month, day: Int) {
        val friendlyMatchDate = repository.byDate(year, month, day)
        if (friendlyMatchDate == null) {
            repository.replace(MatchDate.newDateForFriendly(year, month, day))
        }
    }
}


class AddLeagueMatchDate {
    @Inject lateinit var  repository: LeagueMatchDateRepository

    fun execute(year: Int, month: Month, day: Int) {
        val leagueMatchDate = repository.byDate(year, month, day)
        if (leagueMatchDate == null) {
            repository.replace(MatchDate.newDateForLeague(year, month, day))
        }
    }
}


class AddPlayerToFriendlyMatchDate {
    @Inject lateinit var friendlyMatchDateRegistration: FriendlyMatchDateRegistration

    fun execute(playerName: String, year: Int, month: Month, day: Int) =
            friendlyMatchDateRegistration.addPlayer(playerName, year, month, day)
}


class AddPlayerToLeagueMatchDate {
    @Inject lateinit var leagueMatchDateRegistration : LeagueMatchDateRegistration

    fun execute(playerName: String, year: Int, month: Month, day: Int) =
            leagueMatchDateRegistration.addPlayer(playerName, year, month, day)
}


class GetAllFriendlyMatchDates {
    @Inject lateinit var repository: FriendlyMatchDateRepository

    fun execute(): List<FriendlyMatchDate> = repository.all()
}


class GetAllLeagueMatchDates {
    @Inject lateinit var  repository: LeagueMatchDateRepository

    fun execute(): List<LeagueMatchDate> = repository.all()
}


class GetFriendlyMatchDate {
    @Inject lateinit var  repository: FriendlyMatchDateRepository

    fun execute(year: Int, month: Month, day: Int): FriendlyMatchDate? = repository.byDate(year, month, day)
}


class GetLeagueMatchDate(private val repository: LeagueMatchDateRepository) {

    fun execute(year: Int, month: Month, day: Int): LeagueMatchDate? = repository.byDate(year, month, day)
}


class GetNextFriendlyMatchDates(private val repository: FriendlyMatchDateRepository) {

    fun execute(): List<FriendlyMatchDate> = repository.all().filter { it.isNowOrFuture() }
}


class GetNextLeagueMatchDates(private val repository: LeagueMatchDateRepository) {

    fun execute(): List<LeagueMatchDate> = repository.all().filter { it.isNowOrFuture() }
}


class RemovePlayerToFriendlyMatchDate(private val friendlyMatchDateRegistration: FriendlyMatchDateRegistration) {

    fun execute(playerName: String, year: Int, month: Month, day: Int) =
            friendlyMatchDateRegistration.removePlayer(playerName, year, month, day)
}


class RemovePlayerToLeagueMatchDate(private val leagueMatchDateRegistration: LeagueMatchDateRegistration) {

    fun execute(playerName: String, year: Int, month: Month, day: Int) =
            leagueMatchDateRegistration.removePlayer(playerName, year, month, day)
}