package io.github.binout.soccer.application

import io.github.binout.soccer.domain.date.*
import java.time.Month
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class AddFriendlyMatchDate {

    @Inject
    private lateinit var repository: FriendlyMatchDateRepository

    fun execute(year: Int, month: Month, day: Int) {
        val friendlyMatchDate = repository.byDate(year, month, day)
        if (friendlyMatchDate == null) {
            repository.replace(MatchDate.newDateForFriendly(year, month, day))
        }
    }
}

@ApplicationScoped
class AddLeagueMatchDate {

    @Inject
    lateinit var repository: LeagueMatchDateRepository

    fun execute(year: Int, month: Month, day: Int) {
        val leagueMatchDate = repository.byDate(year, month, day)
        if (leagueMatchDate == null) {
            repository.replace(MatchDate.newDateForLeague(year, month, day))
        }
    }
}

@ApplicationScoped
class AddPlayerToFriendlyMatchDate {
    @Inject
    lateinit var friendlyMatchDateRegistration: FriendlyMatchDateRegistration

    fun execute(playerName: String, year: Int, month: Month, day: Int) =
            friendlyMatchDateRegistration.addPlayer(playerName, year, month, day)
}

@ApplicationScoped
class AddPlayerToLeagueMatchDate {
    @Inject
    lateinit var leagueMatchDateRegistration: LeagueMatchDateRegistration

    fun execute(playerName: String, year: Int, month: Month, day: Int) =
            leagueMatchDateRegistration.addPlayer(playerName, year, month, day)
}

@ApplicationScoped
class GetAllFriendlyMatchDates {
    @Inject
    lateinit var repository: FriendlyMatchDateRepository

    fun execute(): List<FriendlyMatchDate> = repository.all()
}

@ApplicationScoped
class GetAllLeagueMatchDates {
    @Inject
    lateinit var repository: LeagueMatchDateRepository

    fun execute(): List<LeagueMatchDate> = repository.all()
}

@ApplicationScoped
class GetFriendlyMatchDate {
    @Inject
    lateinit var repository: FriendlyMatchDateRepository

    fun execute(year: Int, month: Month, day: Int): FriendlyMatchDate? = repository.byDate(year, month, day)
}

@ApplicationScoped
class GetLeagueMatchDate {
    @Inject
    lateinit var repository: LeagueMatchDateRepository

    fun execute(year: Int, month: Month, day: Int): LeagueMatchDate? = repository.byDate(year, month, day)
}

@ApplicationScoped
class GetNextFriendlyMatchDates {
    @Inject
    lateinit var repository: FriendlyMatchDateRepository

    fun execute(): List<FriendlyMatchDate> = repository.all().filter { it.isNowOrFuture() }
}

@ApplicationScoped
class GetNextLeagueMatchDates {
    @Inject
    lateinit var repository: LeagueMatchDateRepository

    fun execute(): List<LeagueMatchDate> = repository.all().filter { it.isNowOrFuture() }
}

@ApplicationScoped
class RemovePlayerToFriendlyMatchDate {
    @Inject
    lateinit var friendlyMatchDateRegistration: FriendlyMatchDateRegistration

    fun execute(playerName: String, year: Int, month: Month, day: Int) =
            friendlyMatchDateRegistration.removePlayer(playerName, year, month, day)
}

@ApplicationScoped
class RemovePlayerToLeagueMatchDate {
    @Inject
    lateinit var leagueMatchDateRegistration: LeagueMatchDateRegistration

    fun execute(playerName: String, year: Int, month: Month, day: Int) =
            leagueMatchDateRegistration.removePlayer(playerName, year, month, day)
}