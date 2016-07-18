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
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Prefix("match-dates/league")
public class LeagueMatchDateResource {

    @Inject
    LeagueMatchDateRepository repository;

    private TemporalAccessor getTemporalAccessor(String date) {
        return DateTimeFormatter.ISO_LOCAL_DATE.parse(date);
    }

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

    @Put(":date")
    public Payload put(String date) {
        TemporalAccessor accessor = getTemporalAccessor(date);
        int year = accessor.get(ChronoField.YEAR);
        Month month = Month.of(accessor.get(ChronoField.MONTH_OF_YEAR));
        int day = accessor.get(ChronoField.DAY_OF_MONTH);
        Optional<LeagueMatchDate> leagueMatchDate = repository.byDate(year, month, day);
        if (!leagueMatchDate.isPresent()) {
            repository.add(MatchDate.newDateForLeague(year, month, day));
        }
        return Payload.ok();
    }

    @Get(":date")
    public Payload get(String date) {
        TemporalAccessor accessor = getTemporalAccessor(date);
        int year = accessor.get(ChronoField.YEAR);
        Month month = Month.of(accessor.get(ChronoField.MONTH_OF_YEAR));
        int day = accessor.get(ChronoField.DAY_OF_MONTH);
        return repository.byDate(year, month, day)
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
