package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.interfaces.rest.model.RestLink;
import io.github.binout.soccer.interfaces.rest.model.RestMatchDate;
import net.codestory.http.Context;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.time.Month;
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

    @Put(":year-:month-:day")
    public Payload put(int year, int month, int day) {
        Month monthOf = Month.of(month);
        Optional<LeagueMatchDate> leagueMatchDate = repository.byDate(year, monthOf, day);
        if (!leagueMatchDate.isPresent()) {
            repository.add(MatchDate.newDateForLeague(year, monthOf, day));
        }
        return Payload.ok();
    }

    @Get(":year-:month-:day")
    public Payload get(int year, int month, int day) {
        Month monthOf = Month.of(month);
        return repository.byDate(year, monthOf, day)
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
