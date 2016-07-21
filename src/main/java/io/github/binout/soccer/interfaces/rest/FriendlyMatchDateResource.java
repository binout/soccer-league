package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Prefix("match-dates/friendly")
public class FriendlyMatchDateResource {

    @Inject
    FriendlyMatchDateRepository repository;

    @Inject
    PlayerRepository playerRepository;

    @Get
    public List<RestMatchDate> all(Context context) {
        return repository.all()
                .map(m -> toRestModel(context.uri(), m))
                .collect(Collectors.toList());
    }

    @Get("next")
    public List<RestMatchDate> next(Context context) {
        LocalDate now = LocalDate.now();
        return repository.all()
                .filter(d -> d.date().isAfter(now) || d.date().isEqual(now))
                .map(m -> toRestModel(context.uri(), m))
                .collect(Collectors.toList());
    }

    public RestMatchDate toRestModel(String baseUri, FriendlyMatchDate m) {
        RestMatchDate restMatchDate = toRestModel(m);
        restMatchDate.addLinks(new RestLink(baseUri + "/" + restMatchDate.getDate()));
        return restMatchDate;
    }

    @Put(":dateParam")
    public Payload put(String dateParam) {
        RestDate date = new RestDate(dateParam);
        Optional<FriendlyMatchDate> leagueMatchDate = repository.byDate(date.year(), date.month(), date.day());
        if (!leagueMatchDate.isPresent()) {
            repository.add(MatchDate.newDateForFriendly(date.year(), date.month(), date.day()));
        }
        return Payload.ok();
    }

    @Get(":dateParam")
    public Payload get(String dateParam) {
        RestDate date = new RestDate(dateParam);
        return repository.byDate(date.year(), date.month(), date.day())
                .map(this::toRestModel)
                .map(Payload::new)
                .orElse(Payload.notFound());
    }

    private RestMatchDate toRestModel(MatchDate m) {
        RestMatchDate restMatchDate = new RestMatchDate(m.date());
        m.presents().map(Player::name).forEach(restMatchDate::addPresent);
        restMatchDate.setCanBePlanned(m.canBePlanned());
        return restMatchDate;
    }

    @Put(":dateParam/players/:name")
    public Payload putPlayers(String dateParam, String name) {
        return managePlayers(dateParam, name, FriendlyMatchDate::present);
    }

    @Delete(":dateParam/players/:name")
    public Payload deletePlayers(String dateParam, String name) {
        return managePlayers(dateParam, name, FriendlyMatchDate::absent);
    }

    private Payload managePlayers(String dateParam, String name, BiConsumer<FriendlyMatchDate, Player> consumer) {
        RestDate date = new RestDate(dateParam);
        Optional<FriendlyMatchDate> leagueMatchDate = repository.byDate(date.year(), date.month(), date.day());
        Optional<Player> player = playerRepository.byName(name);
        if (player.isPresent() && leagueMatchDate.isPresent()) {
            consumer.accept(leagueMatchDate.get(), player.get());
            return Payload.ok();
        } else {
            return Payload.badRequest();
        }
    }
}
