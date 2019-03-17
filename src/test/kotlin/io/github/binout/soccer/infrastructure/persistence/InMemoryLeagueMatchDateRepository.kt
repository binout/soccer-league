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

    private val dates: MutableMap<LocalDate, LeagueMatchDate> = ConcurrentHashMap()

    override fun all(): List<LeagueMatchDate> = dates.values.sortedBy{ it.date }

    override fun replace(date: LeagueMatchDate) {
        dates[date.date] = date
    }

    override fun byDate(year: Int, month: Month, dayOfMonth: Int): LeagueMatchDate? =
            dates[LocalDate.of(year, month, dayOfMonth)]
}