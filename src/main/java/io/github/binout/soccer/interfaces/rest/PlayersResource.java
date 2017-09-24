package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.application.player.GetAllLeaguePlayers;
import io.github.binout.soccer.application.player.GetAllPlayers;
import io.github.binout.soccer.application.player.GetPlayer;
import io.github.binout.soccer.application.player.ReplacePlayer;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.interfaces.rest.model.RestLink;
import io.github.binout.soccer.interfaces.rest.model.RestPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("players")
public class PlayersResource {

    @Autowired
    GetAllPlayers getAllPlayers;

    @Autowired
    GetAllLeaguePlayers getAllLeaguePlayers;

    @Autowired
    ReplacePlayer replacePlayer;

    @Autowired
    GetPlayer getPlayer;

    @GetMapping
    public List<RestPlayer> getAll() {
        return getAllPlayers.execute().map(p -> toRestModel(p)).collect(Collectors.toList());
    }

    @GetMapping("league")
    public List<RestPlayer> getAllLeague() {
        return getAllLeaguePlayers.execute().map(p -> toRestModel(p)).collect(Collectors.toList());
    }

    @PutMapping("{name}")
    public ResponseEntity put(@PathParam("name") String name, RestPlayer restPlayer) {
        replacePlayer.execute(name, restPlayer.getEmail(), restPlayer.isPlayerLeague(), restPlayer.isGoalkeeper());
        return ResponseEntity.ok().build();
    }

    @GetMapping("{name}")
    public ResponseEntity get(String name) {
        return getPlayer.execute(name)
                .map(PlayersResource::toRestModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static RestPlayer toRestModel(Player p) {
        RestPlayer restPlayer = new RestPlayer(p.name());
        restPlayer.setPlayerLeague(p.isPlayerLeague());
        restPlayer.setGoalkeeper(p.isGoalkeeper());
        p.email().ifPresent(restPlayer::setEmail);
        return restPlayer;
    }


    private static RestPlayer toRestModel(String baseUri, Player p) {
        RestPlayer restPlayer = toRestModel(p);
        restPlayer.addLinks(new RestLink(baseUri + "/" + p.name()));
        return restPlayer;
    }

}
