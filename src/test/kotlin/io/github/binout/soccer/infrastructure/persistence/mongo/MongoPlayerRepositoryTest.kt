package io.github.binout.soccer.infrastructure.persistence.mongo

import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.PlayerName
import io.github.binout.soccer.infrastructure.persistence.MongoConfiguration
import io.github.binout.soccer.infrastructure.persistence.MongoPlayerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MongoPlayerRepositoryTest {

    private lateinit var repository: MongoPlayerRepository

    @BeforeEach
    fun initRepository() {
        repository = MongoPlayerRepository(MongoConfiguration("").database())
    }

    @Test
    fun should_persist_player() {
        repository.add(Player(PlayerName("benoit")))

        val benoit = repository.byName(PlayerName("benoit"))
        assertThat(benoit).isNotNull
        assertThat(benoit!!.email).isNull()
        assertThat(benoit.isPlayerLeague).isFalse()
    }

    @Test
    fun should_persist_league_player() {
        val leaguePlayer = Player(name = PlayerName("benoit"), email = "mail@google.com")
        leaguePlayer.isPlayerLeague = true
        repository.add(leaguePlayer)

        val benoit = repository.byName(PlayerName("benoit"))
        assertThat(benoit).isNotNull
        assertThat(benoit!!.email).isEqualTo("mail@google.com")
        assertThat(benoit.isPlayerLeague).isTrue()
    }

    @Test
    fun should_persist_goalkeeper() {
        val leaguePlayer = Player(name = PlayerName("thomas"), email = "mail@google.com")
        leaguePlayer.isPlayerLeague = true
        leaguePlayer.isGoalkeeper = true
        repository.add(leaguePlayer)

        val thomas = repository.byName(PlayerName("thomas"))
        assertThat(thomas).isNotNull
        assertThat(thomas!!.email).isEqualTo("mail@google.com")
        assertThat(thomas.isPlayerLeague).isTrue()
        assertThat(thomas.isGoalkeeper).isTrue()
    }

}