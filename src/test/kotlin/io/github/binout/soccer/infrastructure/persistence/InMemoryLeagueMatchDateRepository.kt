package io.github.binout.soccer.infrastructure.persistence

import io.github.binout.soccer.domain.date.LeagueMatchDate
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository

import java.time.LocalDate
import java.time.Month
import java.util.Comparator
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream

class InMemoryLeagueMatchDateRepository : LeagueMatchDateRepository {

    private val dates: MutableMap<LocalDate, LeagueMatchDate>

    init {
        dates = ConcurrentHashMap()
    }

    override fun all(): Stream<LeagueMatchDate> {
        return dates.values.stream().sorted(Comparator.comparing<LeagueMatchDate, LocalDate> { it.date() })
    }

    override fun add(date: LeagueMatchDate) {
        dates[date.date()] = date
    }

    override fun byDate(year: Int, month: Month, dayOfMonth: Int): Optional<LeagueMatchDate> {
        return Optional.ofNullable(dates[LocalDate.of(year, month, dayOfMonth)])
    }
}
