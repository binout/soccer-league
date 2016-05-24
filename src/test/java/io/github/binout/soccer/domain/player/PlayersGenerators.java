package io.github.binout.soccer.domain.player;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.UUID;

public interface PlayersGenerators {

    static Player newPlayer() {
        return new Player(UUID.randomUUID().toString());
    }

    class Players extends Generator<Player> {

        public Players() {
            super(Player.class);
        }
        @Override
        public Player generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
            return newPlayer();
        }

    }

    class LeaguePlayers extends Generator<Player> {

        public LeaguePlayers() {
            super(Player.class);
        }

        @Override
        public Player generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
            Player player = newPlayer();
            player.playsInLeague(true);
            return player;
        }
    }
}
