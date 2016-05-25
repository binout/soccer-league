package io.github.binout.soccer.domain.player;

import java.util.Optional;
import java.util.stream.Stream;

public interface PlayerRepository {

    void add(Player player);

    Stream<Player> all();

    Optional<Player> byName(String name);
}
