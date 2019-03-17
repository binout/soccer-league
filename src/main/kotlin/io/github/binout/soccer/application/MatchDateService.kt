package io.github.binout.soccer.application

import io.github.binout.soccer.domain.date.*
import org.springframework.stereotype.Component
import java.time.Month

@Component
class AddFriendlyMatchDate(private val repository: FriendlyMatchDateRepository) {

    fun execute(year: Int, month: Month, day: Int) {
        val friendlyMatchDate = repository.byDate(year, month, day)
        if (friendlyMatchDate == null) {
            repository.replace(MatchDate.newDateForFriendly(year, month, day))
        }
    }
}

@Component
class AddLeagueMatchDate(private val repository: LeagueMatchDateRepository) {

    fun execute(year: Int, month: Month, day: Int) {
        val leagueMatchDate = repository.byDate(year, month, day)
        if (leagueMatchDate == null) {
            repository.replace(MatchDate.newDateForLeague(year, month, day))
        }
    }
}

@Component
class AddPlayerToFriendlyMatchDate(private val friendlyMatchDateRegistration: FriendlyMatchDateRegistration) {

    fun execute(playerName: String, year: Int, month: Month, day: Int) =
            friendlyMatchDateRegistration.addPlayer(playerName, year, month, day)
}

@Component
class AddPlayerToLeagueMatchDate(private val leagueMatchDateRegistration: LeagueMatchDateRegistration) {

    fun execute(playerName: String, year: Int, month: Month, day: Int) =
            leagueMatchDateRegistration.addPlayer(playerName, year, month, day)
}

@Component
class GetAllFriendlyMatchDates(private val repository: FriendlyMatchDateRepository) {

    fun execute(): List<FriendlyMatchDate> = repository.all()
}

@Component
class GetAllLeagueMatchDates(private val repository: LeagueMatchDateRepository) {

    fun execute(): List<LeagueMatchDate> = repository.all()
}

@Component
class GetFriendlyMatchDate(private val repository: FriendlyMatchDateRepository) {

    fun execute(year: Int, month: Month, day: Int): FriendlyMatchDate? = repository.byDate(year, month, day)
}

@Component
class GetLeagueMatchDate(private val repository: LeagueMatchDateRepository) {

    fun execute(year: Int, month: Month, day: Int): LeagueMatchDate? = repository.byDate(year, month, day)
}

@Component
class GetNextFriendlyMatchDates(private val repository: FriendlyMatchDateRepository) {

    fun execute(): List<FriendlyMatchDate> = repository.all().filter { it.isNowOrFuture() }
}

@Component
class GetNextLeagueMatchDates(private val repository: LeagueMatchDateRepository) {

    fun execute(): List<LeagueMatchDate> = repository.all().filter { it.isNowOrFuture() }
}

@Component
class RemovePlayerToFriendlyMatchDate(private val friendlyMatchDateRegistration: FriendlyMatchDateRegistration) {

    fun execute(playerName: String, year: Int, month: Month, day: Int) =
            friendlyMatchDateRegistration.removePlayer(playerName, year, month, day)
}

@Component
class RemovePlayerToLeagueMatchDate(private val leagueMatchDateRegistration: LeagueMatchDateRegistration) {

    fun execute(playerName: String, year: Int, month: Month, day: Int) =
            leagueMatchDateRegistration.removePlayer(playerName, year, month, day)
}