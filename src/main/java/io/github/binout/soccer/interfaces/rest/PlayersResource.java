package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.interfaces.rest.model.RestPlayer;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Prefix("players")
public class PlayersResource {

    @Inject
    PlayerRepository playerRepository;

    @Get
    public List<RestPlayer> getAll() {
        return playerRepository.all().map(p -> toRestModel(p)).collect(Collectors.toList());
    }

    @Put(":name")
    public Payload put(String name, RestPlayer restPlayer) {
        Player player = playerRepository.byName(name).orElse(new Player(name));
        if (restPlayer != null) {
            Optional.ofNullable(restPlayer.getEmail()).ifPresent(player::setEmail);
            Optional.ofNullable(restPlayer.isPlayerLeague()).ifPresent(player::playsInLeague);
        }
        playerRepository.add(player);
        return Payload.ok();
    }

    @Get(":name")
    public Payload get(String name) {
        return playerRepository.byName(name)
                .map(PlayersResource::toRestModel)
                .map(Payload::new)
                .orElse(Payload.notFound());
    }

    private static RestPlayer toRestModel(Player p) {
        RestPlayer restPlayer = new RestPlayer(p.name());
        restPlayer.setPlayerLeague(p.isPlayerLeague());
        p.email().ifPresent(restPlayer::setEmail);
        return restPlayer;
    }

    /*
    private static RestPlayer toRestModel(Player p) {
        RestPlayer restPlayer = new RestPlayer(p.name());
        URI uri = uriInfo.getAbsolutePathBuilder().path(p.name()).build();
        restPlayer.addLinks(new RestLink(uri));
        return restPlayer;
    }*/

}
