package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlayersResource {

    @Inject
    PlayerRepository playerRepository;

    @GET
    public List<RestPlayer> getAll(@Context UriInfo uriInfo) {
        return playerRepository.all().map(p -> toRestModel(uriInfo, p)).collect(Collectors.toList());
    }

    @Path("{name}")
    @PUT
    public Response put(@PathParam("name") String name, RestPlayer restPlayer) {
        Player player = playerRepository.byName(name).orElse(new Player(name));
        if (restPlayer != null) {
            Optional.ofNullable(restPlayer.getEmail()).ifPresent(player::setEmail);
            Optional.ofNullable(restPlayer.isPlayerLeague()).ifPresent(player::playsInLeague);
        }
        playerRepository.add(player);
        return Response.ok().build();
    }

    @Path("{name}")
    @GET
    public Response get(@PathParam("name") String name) {
        return playerRepository.byName(name)
                .map(PlayersResource::toRestModel)
                .map(p -> Response.ok(p).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private static RestPlayer toRestModel(Player p) {
        RestPlayer restPlayer = new RestPlayer(p.name());
        restPlayer.setPlayerLeague(p.isPlayerLeague());
        p.email().ifPresent(restPlayer::setEmail);
        return restPlayer;
    }

    private static RestPlayer toRestModel(UriInfo uriInfo, Player p) {
        RestPlayer restPlayer = new RestPlayer(p.name());
        URI uri = uriInfo.getAbsolutePathBuilder().path(p.name()).build();
        restPlayer.addLinks(new RestLink(uri));
        return restPlayer;
    }

    private static class RestPlayer extends RestModel {

        private String name;
        private String email;
        private Boolean isPlayerLeague;

        RestPlayer(){}

        RestPlayer(String name) {
            this();
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean isPlayerLeague() {
            return isPlayerLeague;
        }

        public void setPlayerLeague(Boolean playerLeague) {
            isPlayerLeague = playerLeague;
        }
    }
}
