package io.github.binout.soccer.infrastructure.persistence

import io.github.binout.soccer.domain.season.Season
import io.github.binout.soccer.domain.season.SeasonRepository
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream

class InMemorySeasonRepository : SeasonRepository {

    private val seasons: MutableMap<String, Season>

    init {
        seasons = ConcurrentHashMap()
    }

    override fun add(season: Season) {
        seasons[season.name] = season
    }

    override fun byName(name: String): Season? = seasons[name]

    override fun all(): List<Season> = seasons.values.toList()

}
