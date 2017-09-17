package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.application.date.*;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.infrastructure.persistence.TransactedScopeEnabled;
import io.github.binout.soccer.interfaces.rest.model.RestDate;
import io.github.binout.soccer.interfaces.rest.model.RestLink;
import io.github.binout.soccer.interfaces.rest.model.RestMatchDate;
import net.codestory.http.Context;
import net.codestory.http.annotations.Delete;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Prefix("match-dates/league")
@TransactedScopeEnabled
public class LeagueMatchDateResource {

    @Inject
    GetAllLeagueMatchDates allLeagueMatchDates;

    @Inject
    GetNextLeagueMatchDates nextLeagueMatchDates;

    @Inject
    AddLeagueMatchDate addLeagueMatchDate;

    @Inject
    GetLeagueMatchDate getLeagueMatchDate;

    @Inject
    AddPlayerToLeagueMatchDate addPlayerToLeagueMatchDate;

    @Inject
    RemovePlayerToLeagueMatchDate removePlayerToLeagueMatchDate;

    @Get
    public List<RestMatchDate> all(Context context) {
        return allLeagueMatchDates.execute()
                .map(m -> toRestModel(context.uri(), m))
                .collect(Collectors.toList());
    }

    @Get("next")
    public List<RestMatchDate> next(Context context) {
        return nextLeagueMatchDates.execute()
                .filter(LeagueMatchDate::isNowOrFuture)
                .map(m -> toRestModel(context.uri(), m))
                .collect(Collectors.toList());
    }

    public RestMatchDate toRestModel(String baseUri, LeagueMatchDate m) {
        RestMatchDate restMatchDate = toRestModel(m);
        restMatchDate.addLinks(new RestLink(baseUri + restMatchDate.getDate()));
        return restMatchDate;
    }

    @Put(":dateParam")
    public Payload put(String dateParam) {
        RestDate date = new RestDate(dateParam);
        addLeagueMatchDate.execute(date.year(), date.month(), date.day());
        return Payload.ok();
    }

    @Get(":dateParam")
    public Payload get(String dateParam) {
        RestDate date = new RestDate(dateParam);
        return getLeagueMatchDate.execute(date.year(), date.month(), date.day())
                .map(this::toRestModel)
                .map(Payload::new)
                .orElse(Payload.notFound());
    }

    private RestMatchDate toRestModel(MatchDate m) {
        RestMatchDate restMatchDate = new RestMatchDate(m.date());
        m.presents().forEach(restMatchDate::addPresent);
        restMatchDate.setCanBePlanned(m.canBePlanned());
        return restMatchDate;
    }

    @Put(":dateParam/players/:name")
    public Payload putPlayers(String dateParam, String name) {
        RestDate date = new RestDate(dateParam);
        addPlayerToLeagueMatchDate.execute(name, date.year(), date.month(), date.day());
        return Payload.ok();
    }

    @Delete(":dateParam/players/:name")
    public Payload deletePlayers(String dateParam, String name) {
        RestDate date = new RestDate(dateParam);
        removePlayerToLeagueMatchDate.execute(name, date.year(), date.month(), date.day());
        return Payload.ok();    }
}
