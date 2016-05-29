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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeasonsResource {

    @Inject
    SeasonRepository seasonRepository;

    @GET
    public List<RestSeason> getAll(@Context UriInfo uriInfo) {
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

    @Path("{name}")
    @GET
    public Response get(@PathParam("name") String name) {
        return seasonRepository.byName(name)
                .map(s -> new RestSeason(s.name()))
                .map(s -> Response.ok(s).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private static RestSeason toRestModel(UriInfo uriInfo, Season s) {
        RestSeason restSeason = new RestSeason(s.name());
        URI uri = uriInfo.getAbsolutePathBuilder().path(s.name()).build();
        restSeason.addLinks(new RestLink(uri));
        return restSeason;
    }

    private static class RestSeason extends RestModel {

        private String name;

        RestSeason(){}

        RestSeason(String name) {
            this();
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
