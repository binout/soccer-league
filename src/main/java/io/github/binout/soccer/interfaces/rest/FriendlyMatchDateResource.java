package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.interfaces.rest.model.RestLink;
import io.github.binout.soccer.interfaces.rest.model.RestMatchDate;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("match-dates/friendly")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FriendlyMatchDateResource {

    @Inject
    FriendlyMatchDateRepository repository;

    @GET
    public List<RestMatchDate> all(@Context UriInfo uriInfo) {
        return repository.all().map(m -> toRestModel(uriInfo, m)).collect(Collectors.toList());
    }

    @PUT
    @Path("{year}-{month}-{day}")
    public Response put(@PathParam("year") int year, @PathParam("month") int month, @PathParam("day") int day) {
        Month monthOf = Month.of(month);
        Optional<FriendlyMatchDate> leagueMatchDate = repository.byDate(year, monthOf, day);
        if (!leagueMatchDate.isPresent()) {
            repository.add(MatchDate.newDateForFriendly(year, monthOf, day));
        }
        return Response.ok().build();
    }

    @GET
    @Path("{year}-{month}-{day}")
    public Response get(@PathParam("year") int year, @PathParam("month") int month, @PathParam("day") int day) {
        Month monthOf = Month.of(month);
        return repository.byDate(year, monthOf, day)
                .map(m -> new RestMatchDate(m.date()))
                .map(r -> Response.ok(r).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private RestMatchDate toRestModel(@Context UriInfo uriInfo, MatchDate m) {
        RestMatchDate restMatchDate = new RestMatchDate(m.date());
        restMatchDate.addLinks(new RestLink(uriInfo.getAbsolutePathBuilder().path(restMatchDate.getDate()).build()));
        return restMatchDate;
    }

}
