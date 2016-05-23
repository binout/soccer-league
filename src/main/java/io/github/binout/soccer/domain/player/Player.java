package io.github.binout.soccer.domain.player;

import java.util.Objects;
import java.util.Optional;

public class Player {

    private final String name;
    private String email;
    private boolean isPlayerLeague;

    public Player(String name) {
        this.name = Objects.requireNonNull(name);
        this.isPlayerLeague = false;
    }

    public Player(String name, String email) {
        this(name);
        this.email = Objects.requireNonNull(email);
    }

    public String name() {
        return name;
    }

    public Optional<String> email() {
        return Optional.ofNullable(email);
    }

    public boolean isPlayerLeague() {
        return isPlayerLeague;
    }

    public void playsInLeague(boolean playerLeague) {
        this.isPlayerLeague = playerLeague;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return name.equals(player.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
