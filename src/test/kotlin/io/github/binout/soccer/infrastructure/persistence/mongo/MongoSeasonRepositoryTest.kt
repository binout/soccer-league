package io.github.binout.soccer.infrastructure.persistence.mongo

import io.github.binout.soccer.domain.season.Season
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mongolink.MongoSession

@ExtendWith(MongolinkExtension::class)
class MongoSeasonRepositoryTest {

    private lateinit var repository: MongoSeasonRepository

    @BeforeEach
    fun initRepository(currentSession: MongoSession) {
        repository = MongoSeasonRepository { currentSession }
    }

    @Test
    fun should_persist_season() {
        repository.add(Season("2016"))
        repository.session().flush()

        val season = repository.byName("2016")
        assertThat(season).isNotNull
        assertThat(season!!.id).isNotNull()
        assertThat(season.friendlyMatches()).isEmpty()
        assertThat(season.leagueMatches()).isEmpty()
    }

}