package io.github.binout.soccer.infrastructure.persistence;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

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
        return players.values().stream().sorted(Comparator.comparing(Player::name));
    }

    @Override
    public Optional<Player> byName(String name) {
        return Optional.ofNullable(players.get(name));
    }
}
