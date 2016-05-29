package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("seasons")
public class SeasonsResource {

    @Inject
    SeasonRepository seasonRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestSeason> get(@Context UriInfo uriInfo) {
        return seasonRepository.all().map(s -> toRestModel(uriInfo, s)).collect(Collectors.toList());
    }

    @Path("{name}")
    @PUT
    public Response put(@PathParam("name") String name) {
        if (!seasonRepository.byName(name).isPresent()) {
            seasonRepository.add(new Season(name));
        }
        return Response.ok().build();
    }

    private static RestSeason toRestModel(UriInfo uriInfo, Season s) {
        RestSeason restSeason = new RestSeason();
        restSeason.setName(s.name());
        URI uri = uriInfo.getAbsolutePathBuilder().path(s.name()).build();
        restSeason.addLinks(new RestLink(uri));
        return restSeason;
    }

    private static class RestSeason extends RestModel {

        private String name;

        RestSeason(){}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
