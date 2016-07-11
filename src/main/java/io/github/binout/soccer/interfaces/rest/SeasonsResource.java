package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;
import io.github.binout.soccer.interfaces.rest.model.RestSeason;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Prefix("seasons")
public class SeasonsResource {

    @Inject
    SeasonRepository seasonRepository;

    @Get
    public List<RestSeason> getAll() {
        return seasonRepository.all().map(s -> toRestModel(s)).collect(Collectors.toList());
    }

    @Put(":name")
    public Payload put(String name) {
        if (!seasonRepository.byName(name).isPresent()) {
            seasonRepository.add(new Season(name));
        }
        return Payload.ok();
    }

    @Get(":name")
    public Payload get(String name) {
        return seasonRepository.byName(name)
                .map(s -> new RestSeason(s.name()))
                .map(Payload::new)
                .orElse(Payload.notFound());
    }

    private static RestSeason toRestModel(Season s) {
        RestSeason restSeason = new RestSeason(s.name());
        //URI uri = uriInfo.getAbsolutePathBuilder().path(s.name()).build();
        //restSeason.addLinks(new RestLink(uri));
        return restSeason;
    }

}
