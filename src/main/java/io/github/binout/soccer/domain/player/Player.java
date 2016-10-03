package io.github.binout.soccer.domain.player;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Player {

    private final String id;
    private String name;
    private String email;
    private boolean isPlayerLeague;
    private boolean isGoalkeeper;

    Player() {
        this.id = UUID.randomUUID().toString();
    }

    public Player(String name) {
        this();
        this.name = Objects.requireNonNull(name);
        this.isPlayerLeague = false;
        this.isGoalkeeper = false;
    }

    public Player(String name, String email) {
        this(name);
        this.email = Objects.requireNonNull(email);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Optional<String> email() {
        return Optional.ofNullable(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPlayerLeague() {
        return isPlayerLeague;
    }

    public void playsInLeague(boolean playerLeague) {
        this.isPlayerLeague = playerLeague;
    }

    public boolean isGoalkeeper() {
        return isGoalkeeper;
    }

    public void playsAsGoalkeeper(boolean isGoalkeeper) {
        this.isGoalkeeper = isGoalkeeper;
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
