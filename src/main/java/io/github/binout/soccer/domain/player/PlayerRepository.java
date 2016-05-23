package io.github.binout.soccer.domain.player;

import java.util.stream.Stream;

public interface PlayerRepository {

    void add(Player player);

    Stream<Player> all();
}
