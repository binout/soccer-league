package io.github.binout.soccer.infrastructure.persistence.mongo

import io.github.binout.soccer.domain.date.MatchDate
import io.github.binout.soccer.domain.player.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mongolink.MongoSession
import java.time.Month

@ExtendWith(MongolinkExtension::class)
class MongoFriendlyMatchDateRepositoryTest {

    private lateinit var repository: MongoFriendlyMatchDateRepository
    private lateinit var playerRepository: MongoPlayerRepository

    @BeforeEach
    fun initRepository(currentSession: MongoSession) {
        playerRepository = MongoPlayerRepository { currentSession }
        repository = MongoFriendlyMatchDateRepository { currentSession }
    }

    @Test
    fun should_persist_date_without_player() {
        repository.add(MatchDate.newDateForFriendly(2016, Month.APRIL, 1))
        repository.session().flush()

        val matchDate = repository.byDate(2016, Month.APRIL, 1)
        assertThat(matchDate).isNotNull
        assertThat(matchDate!!.id).isNotNull()
        assertThat(matchDate.presents().count()).isZero()
    }

    @Test
    fun should_persist_date_with_player() {
        val benoit = Player("benoit")
        playerRepository.add(benoit)
        repository.session().flush()

        val date = MatchDate.newDateForFriendly(2016, Month.APRIL, 1)
        date.present(benoit)
        repository.add(date)
        repository.session().flush()

        val matchDate = repository.byDate(2016, Month.APRIL, 1)
        assertThat(matchDate).isNotNull
        assertThat(matchDate!!.id).isNotNull()
        assertThat(matchDate.presents()).containsOnly("benoit")
    }
}