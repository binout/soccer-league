package io.github.binout.soccer.application

import io.github.binout.soccer.domain.date.FriendlyMatchDate
import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository
import io.github.binout.soccer.domain.date.LeagueMatchDate
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.PlayerName
import io.github.binout.soccer.domain.player.PlayerRepository
import io.github.binout.soccer.domain.season.*
import io.github.binout.soccer.domain.season.FriendlyMatch
import io.github.binout.soccer.domain.season.LeagueMatch
import java.time.LocalDate
import java.time.Month
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.context.Initialized
import javax.enterprise.event.Observes
import javax.inject.Inject


@ApplicationScoped
class InitializeSeason {

    @Inject lateinit var seasonRepository: SeasonRepository

    fun initCurrentSeason(@Observes @Initialized(ApplicationScoped::class) init: Any) {
        val currentSeason = Season.currentSeasonName()
        val optSeason = seasonRepository.all().firstOrNull { s -> s.name == currentSeason }
        if (optSeason == null) {
            seasonRepository.replace(Season(currentSeason))
        }
    }
}


class AddFriendlyMatch(
        private val seasonRepository: SeasonRepository,
        private val friendlyMatchDateRepository: FriendlyMatchDateRepository,
        private val seasonPlanning: SeasonPlanning) {

    fun execute(seasonName: String, year: Int, month: Month, day: Int) {
        val season = seasonRepository.byName(seasonName) ?: throw IllegalArgumentException("Can not replace match to season")
        val matchDate = friendlyMatchDateRepository.byDate(year, month, day) ?: throw IllegalArgumentException("Can not replace match to season")
        seasonPlanning.planFriendlyMatch(season, matchDate)
    }
}


class AddLeagueMatch(
        private val seasonRepository: SeasonRepository,
        private val leagueMatchDateRepository: LeagueMatchDateRepository,
        private val seasonPlanning: SeasonPlanning) {

    fun execute(seasonName: String, year: Int, month: Month, day: Int) {
        val season = seasonRepository.byName(seasonName) ?: throw IllegalArgumentException("Can not replace match to season")
        val matchDate = leagueMatchDateRepository.byDate(year, month, day) ?: throw IllegalArgumentException("Can not replace match to season")
        seasonPlanning.planLeagueMatch(season, matchDate)
    }
}



class AddSeason(private val seasonRepository: SeasonRepository) {

    fun execute(seasonName: String) {
        if (seasonRepository.byName(seasonName) == null) {
            seasonRepository.replace(Season(seasonName))
        }
    }
}


class GetAllSeasons(private val seasonRepository: SeasonRepository) {

    fun execute(): List<Season> = seasonRepository.all()
}


class GetFriendlyMatches(
        private val seasonRepository: SeasonRepository,
        private val matchPlanning: MatchPlanning) {

    fun execute(seasonName: String): List<Pair<FriendlyMatch, List<Player>>> {
        val season = seasonRepository.byName(seasonName) ?: throw IllegalArgumentException("Invalid season")
        return season.friendlyMatches().map { it to matchPlanning.getSubstitutes(season, it) }
    }
}


class GetLeagueMatches(
        private val seasonRepository: SeasonRepository,
        private val matchPlanning: MatchPlanning) {

    fun execute(seasonName: String): List<Pair<LeagueMatch, List<Player>>> {
        val season = seasonRepository.byName(seasonName) ?: throw IllegalArgumentException("Invalid season")
        return season.leagueMatches().map { it to matchPlanning.getSubstitutes(season, it) }
    }
}



class GetNextFriendlyMatches(
        private val seasonRepository: SeasonRepository,
        private val matchPlanning: MatchPlanning) {

    fun execute(seasonName: String): List<Pair<FriendlyMatch, List<Player>>> {
        val season = seasonRepository.byName(seasonName) ?: throw IllegalArgumentException("Invalid season")
        return season.friendlyMatches()
                .filter{ it.isNowOrFuture() }
                .map { it to matchPlanning.getSubstitutes(season, it) }
    }
}


class GetNextLeagueMatches(
        private val seasonRepository: SeasonRepository,
        private val matchPlanning: MatchPlanning) {

    fun execute(seasonName: String): List<Pair<LeagueMatch, List<Player>>> {
        val season = seasonRepository.byName(seasonName) ?: throw IllegalArgumentException("Invalid season")
        return season.leagueMatches()
                .filter { it.isNowOrFuture() }
                .map { it to matchPlanning.getSubstitutes(season, it) }
    }
}



class GetSeason(private val seasonRepository: SeasonRepository) {

    fun execute(name: String): Season? = seasonRepository.byName(name)
}


class GetSeasonStats(private val seasonRepository: SeasonRepository) {

    fun execute(name: String): SeasonStatistics? = seasonRepository.byName(name)?.statistics()
}


class GetToPlanFriendlyMatches(private val seasonRepository: SeasonRepository,
                               private val seasonPlanning: SeasonPlanning) {

    fun execute(seasonName: String): List<FriendlyMatchDate> = seasonRepository.byName(seasonName)
            ?.let { s -> seasonPlanning.friendlyMatchDatesToPlan(s) }
            ?: throw IllegalArgumentException("Invalid season")
}


class GetToPlanLeagueMatches(private val seasonRepository: SeasonRepository,
                             private val seasonPlanning: SeasonPlanning) {

    fun execute(seasonName: String): List<LeagueMatchDate> = seasonRepository.byName(seasonName)
            ?.let { s -> seasonPlanning.leagueMatchDatesToPlan(s) }
            ?: throw IllegalArgumentException("Invalid season")
}


class SubstitutePlayerInFriendlyMatches(
        private val playerRepository: PlayerRepository,
        private val seasonRepository: SeasonRepository,
        private val matchPlanning: MatchPlanning) {

    fun execute(seasonName: String, date: LocalDate, name: String) {
        val playerName = PlayerName(name)
        val season = seasonRepository.byName(seasonName) ?: throw IllegalArgumentException("Invalid season")
        val player = playerRepository.byName(playerName) ?: throw  IllegalArgumentException("Invalid player")
        val match = season.friendlyMatches().first { m -> m.date == date }
        matchPlanning.substitutePlayer(season, match, player)
    }
}


class SubstitutePlayerInLeagueMatches(
        private val playerRepository: PlayerRepository,
        private val seasonRepository: SeasonRepository,
        private val matchPlanning: MatchPlanning) {

    fun execute(seasonName: String, date: LocalDate, name: String) {
        val playerName = PlayerName(name)
        val season = seasonRepository.byName(seasonName) ?: throw IllegalArgumentException("Invalid season")
        val player = playerRepository.byName(playerName) ?: throw IllegalArgumentException("Invalid player")
        val match = season.leagueMatches().first { m -> m.date == date }
        matchPlanning.substitutePlayer(season, match, player)
    }
}