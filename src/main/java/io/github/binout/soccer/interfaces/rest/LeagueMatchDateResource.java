package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.interfaces.rest.model.RestDate;
import io.github.binout.soccer.interfaces.rest.model.RestLink;
import io.github.binout.soccer.interfaces.rest.model.RestMatchDate;
import net.codestory.http.Context;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Prefix("match-dates/league")
public class LeagueMatchDateResource {

    @Inject
    LeagueMatchDateRepository repository;

    @Get
    public List<RestMatchDate> all(Context context) {
        return repository.all().map(m -> toRestModel(context.uri(), m)).collect(Collectors.toList());
    }

    @Get("next")
    public List<RestMatchDate> next(Context context) {
        return repository.all()
                .filter(d -> d.date().isAfter(LocalDate.now()))
                .map(m -> toRestModel(context.uri(), m)).collect(Collectors.toList());
    }

    @Put(":dateParam")
    public Payload put(String dateParam) {
        RestDate date = new RestDate(dateParam);
        Optional<LeagueMatchDate> leagueMatchDate = repository.byDate(date.year(), date.month(), date.day());
        if (!leagueMatchDate.isPresent()) {
            repository.add(MatchDate.newDateForLeague(date.year(), date.month(), date.day()));
        }
        return Payload.ok();
    }

    @Get(":dateParam")
    public Payload get(String dateParam) {
        RestDate date = new RestDate(dateParam);
        return repository.byDate(date.year(), date.month(), date.day())
                .map(m -> new RestMatchDate(m.date()))
                .map(Payload::new)
                .orElse(Payload.notFound());
    }

    private RestMatchDate toRestModel(String baseUri, MatchDate m) {
        RestMatchDate restMatchDate = new RestMatchDate(m.date());
        restMatchDate.addLinks(new RestLink(baseUri + restMatchDate.getDate()));
        return restMatchDate;
    }

}
