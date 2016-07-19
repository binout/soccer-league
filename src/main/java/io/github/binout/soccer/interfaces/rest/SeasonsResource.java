package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;
import io.github.binout.soccer.domain.season.SeasonService;
import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import io.github.binout.soccer.domain.season.match.LeagueMatch;
import io.github.binout.soccer.interfaces.rest.model.RestDate;
import io.github.binout.soccer.interfaces.rest.model.RestLink;
import io.github.binout.soccer.interfaces.rest.model.RestMatch;
import io.github.binout.soccer.interfaces.rest.model.RestSeason;
import net.codestory.http.Context;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Prefix("seasons")
public class SeasonsResource {

    @Inject
    SeasonRepository seasonRepository;

    @Inject
    SeasonService seasonService;

    @Inject
    FriendlyMatchDateRepository friendlyMatchDateRepository;

    @Inject
    LeagueMatchDateRepository leagueMatchDateRepository;

    @Get
    public List<RestSeason> getAll(Context context) {
        return seasonRepository.all().map(s -> toRestModel(context.uri(), s)).collect(Collectors.toList());
    }

    @Put(":name")
    public Payload put(String name) {
        String seasonName = new SeasonName(name).name();
        if (!seasonRepository.byName(seasonName).isPresent()) {
            seasonRepository.add(new Season(seasonName));
        }
        return Payload.ok();
    }

    @Get(":name/matches/friendly")
    public Payload getFriendlyMatch(String name) {
        String seasonName = new SeasonName(name).name();
        return seasonRepository.byName(seasonName)
                .map(Season::friendlyMatches)
                .map(s -> s.map(this::toRestMatch).collect(Collectors.toList()))
                .map(Payload::new)
                .orElse(Payload.badRequest());
    }

    private RestMatch toRestMatch(FriendlyMatch m) {
        RestMatch restMatch = new RestMatch(m.date());
        m.players().map(Player::name).forEach(restMatch::addPlayer);
        return restMatch;
    }

    @Put(":name/matches/friendly/:dateParam")
    public Payload putFriendlyMatch(String name, String dateParam) {
        String seasonName = new SeasonName(name).name();
        Optional<Season> season = seasonRepository.byName(seasonName);
        RestDate date = new RestDate(dateParam);
        Optional<FriendlyMatchDate> matchDate = friendlyMatchDateRepository.byDate(date.year(), date.month(), date.day());
        if (season.isPresent() && matchDate.isPresent()) {
            seasonService.planFriendlyMatch(season.get(), matchDate.get());
            return Payload.ok();
        } else {
            return Payload.badRequest();
        }
    }

    @Get(":name/matches/league")
    public Payload getLeagueMatch(String name) {
        String seasonName = new SeasonName(name).name();
        return seasonRepository.byName(seasonName)
                .map(Season::leagueMatches)
                .map(s -> s.map(this::toRestMatch).collect(Collectors.toList()))
                .map(Payload::new)
                .orElse(Payload.badRequest());
    }

    private RestMatch toRestMatch(LeagueMatch m) {
        RestMatch restMatch = new RestMatch(m.date());
        m.players().map(Player::name).forEach(restMatch::addPlayer);
        return restMatch;
    }

    @Put(":name/matches/league/:dateParam")
    public Payload putLeagueMatch(String name, String dateParam) {
        String seasonName = new SeasonName(name).name();
        Optional<Season> season = seasonRepository.byName(seasonName);
        RestDate date = new RestDate(dateParam);
        Optional<LeagueMatchDate> matchDate = leagueMatchDateRepository.byDate(date.year(), date.month(), date.day());
        if (season.isPresent() && matchDate.isPresent()) {
            seasonService.planLeagueMatch(season.get(), matchDate.get());
            return Payload.ok();
        } else {
            return Payload.badRequest();
        }
    }

    @Get(":name")
    public Payload get(String name) {
        String seasonName = new SeasonName(name).name();
        return seasonRepository.byName(seasonName)
                .map(s -> new RestSeason(s.name()))
                .map(Payload::new)
                .orElse(Payload.notFound());
    }

    private static RestSeason toRestModel(String baseUri, Season s) {
        RestSeason restSeason = new RestSeason(s.name());
        restSeason.addLinks(new RestLink(baseUri + "/" + s.name()));
        return restSeason;
    }

}
