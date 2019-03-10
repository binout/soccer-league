package io.github.binout.soccer.infrastructure.persistence.mongo

import io.github.binout.soccer.domain.player.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mongolink.MongoSession

@ExtendWith(MongolinkExtension::class)
class MongoPlayerRepositoryTest {

    private lateinit var repository: MongoPlayerRepository

    @BeforeEach
    fun initRepository(currentSession: MongoSession) {
        repository = MongoPlayerRepository { currentSession }
    }

    private fun persistPlayer(leaguePlayer: Player) {
        repository.add(leaguePlayer)
        repository.session().flush()
    }

    @Test
    fun should_persist_player() {
        persistPlayer(Player("benoit"))

        val benoit = repository.byName("benoit")
        assertThat(benoit).isPresent
        assertThat(benoit.get().id()).isNotNull()
        assertThat(benoit.get().email()).isEmpty
        assertThat(benoit.get().isPlayerLeague).isFalse()
    }

    @Test
    fun should_persist_league_player() {
        val leaguePlayer = Player("benoit", "mail@google.com")
        leaguePlayer.playsInLeague(true)
        persistPlayer(leaguePlayer)

        val benoit = repository.byName("benoit")
        assertThat(benoit).isPresent
        assertThat(benoit.get().id()).isNotNull()
        assertThat(benoit.get().email()).contains("mail@google.com")
        assertThat(benoit.get().isPlayerLeague).isTrue()
    }

    @Test
    fun should_persist_goalkeeper() {
        val leaguePlayer = Player("thomas", "mail@google.com")
        leaguePlayer.playsInLeague(true)
        leaguePlayer.playsAsGoalkeeper(true)
        persistPlayer(leaguePlayer)

        val thomas = repository.byName("thomas")
        assertThat(thomas).isPresent
        assertThat(thomas.get().id()).isNotNull()
        assertThat(thomas.get().email()).contains("mail@google.com")
        assertThat(thomas.get().isPlayerLeague).isTrue()
        assertThat(thomas.get().isGoalkeeper).isTrue()
    }

}