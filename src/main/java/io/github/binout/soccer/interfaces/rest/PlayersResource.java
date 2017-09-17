package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.application.player.GetAllLeaguePlayers;
import io.github.binout.soccer.application.player.GetAllPlayers;
import io.github.binout.soccer.application.player.GetPlayer;
import io.github.binout.soccer.application.player.ReplacePlayer;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.infrastructure.persistence.TransactedScopeEnabled;
import io.github.binout.soccer.interfaces.rest.model.RestLink;
import io.github.binout.soccer.interfaces.rest.model.RestPlayer;
import net.codestory.http.Context;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Prefix("players")
@TransactedScopeEnabled
public class PlayersResource {

    @Inject
    GetAllPlayers getAllPlayers;

    @Inject
    GetAllLeaguePlayers getAllLeaguePlayers;

    @Inject
    ReplacePlayer replacePlayer;

    @Inject
    GetPlayer getPlayer;

    @Get
    public List<RestPlayer> getAll(Context context) {
        return getAllPlayers.execute().map(p -> toRestModel(context.uri(), p)).collect(Collectors.toList());
    }

    @Get("league")
    public List<RestPlayer> getAllLeague(Context context) {
        return getAllLeaguePlayers.execute().map(p -> toRestModel(context.uri(), p)).collect(Collectors.toList());
    }

    @Put(":name")
    public Payload put(String name, RestPlayer restPlayer) {
        replacePlayer.execute(name, restPlayer.getEmail(), restPlayer.isPlayerLeague(), restPlayer.isGoalkeeper());
        return Payload.ok();
    }

    @Get(":name")
    public Payload get(String name) {
        return getPlayer.execute(name)
                .map(PlayersResource::toRestModel)
                .map(Payload::new)
                .orElse(Payload.notFound());
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
