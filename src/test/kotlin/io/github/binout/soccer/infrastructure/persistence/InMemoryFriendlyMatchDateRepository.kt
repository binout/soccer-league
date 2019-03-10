package io.github.binout.soccer.infrastructure.persistence

import io.github.binout.soccer.domain.date.FriendlyMatchDate
import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository

import java.time.LocalDate
import java.time.Month
import java.util.Comparator
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream

class InMemoryFriendlyMatchDateRepository : FriendlyMatchDateRepository {

    private val dates: MutableMap<LocalDate, FriendlyMatchDate>

    init {
        dates = ConcurrentHashMap()
    }

    override fun all(): Stream<FriendlyMatchDate> {
        return dates.values.stream().sorted(Comparator.comparing<FriendlyMatchDate, LocalDate> { it.date() })
    }

    override fun add(date: FriendlyMatchDate) {
        dates[date.date()] = date
    }

    override fun byDate(year: Int, month: Month, dayOfMonth: Int): Optional<FriendlyMatchDate> {
        return Optional.ofNullable(dates[LocalDate.of(year, month, dayOfMonth)])
    }
}
