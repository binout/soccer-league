package io.github.binout.soccer.infrastructure.persistence;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Vetoed;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@ApplicationScoped
@Vetoed
public class InMemoryPlayerRepository implements PlayerRepository {

    private Map<String, Player> players;

    public InMemoryPlayerRepository() {
        players = new ConcurrentHashMap<>();
    }

    @Override
    public void add(Player player) {
        players.put(player.name(), player);
    }

    @Override
    public Stream<Player> all() {
        return players.values().stream();
    }

    @Override
    public Optional<Player> byName(String name) {
        return Optional.ofNullable(players.get(name));
    }
}
