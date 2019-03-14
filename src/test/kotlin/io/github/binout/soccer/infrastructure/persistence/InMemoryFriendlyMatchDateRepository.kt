package io.github.binout.soccer.infrastructure.persistence

import io.github.binout.soccer.domain.date.FriendlyMatchDate
import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository
import java.time.LocalDate
import java.time.Month
import java.util.concurrent.ConcurrentHashMap

class InMemoryFriendlyMatchDateRepository : FriendlyMatchDateRepository {

    private val dates: MutableMap<LocalDate, FriendlyMatchDate> = ConcurrentHashMap()

    override fun all(): List<FriendlyMatchDate> = dates.values.sortedBy{ it.date }

    override fun add(date: FriendlyMatchDate) {
        dates[date.date] = date
    }

    override fun byDate(year: Int, month: Month, dayOfMonth: Int): FriendlyMatchDate? =
            dates[LocalDate.of(year, month, dayOfMonth)]
}
