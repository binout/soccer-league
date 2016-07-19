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
import io.github.binout.soccer.interfaces.rest.model.RestMatch;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Collectors;

@Prefix("seasons/:name/matches")
public class SeasonMatchesResource {

    @Inject
    SeasonRepository seasonRepository;

    @Inject
    SeasonService seasonService;

    @Inject
    FriendlyMatchDateRepository friendlyMatchDateRepository;

    @Inject
    LeagueMatchDateRepository leagueMatchDateRepository;

    @Get("friendly")
    public Payload getFriendly(String name) {
        return seasonRepository.byName(name)
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

    @Put("friendly/:dateParam")
    public Payload putFriendly(String name, String dateParam) {
        Optional<Season> season = seasonRepository.byName(name);
        RestDate date = new RestDate(dateParam);
        Optional<FriendlyMatchDate> matchDate = friendlyMatchDateRepository.byDate(date.year(), date.month(), date.day());
        if (season.isPresent() && matchDate.isPresent()) {
            seasonService.planFriendlyMatch(season.get(), matchDate.get());
            return Payload.ok();
        } else {
            return Payload.badRequest();
        }
    }

    @Get("league")
    public Payload getLeague(String name) {
        return seasonRepository.byName(name)
                .map(Season::leagueMatches)
                .map(s -> s.map(this::toRestMatch).collect(Collectors.toList()))
                .map(Payload::new)
                .orElse(Payload.badRequest());
    }

    @Put("league/:dateParam")
    public Payload putLeague(String name, String dateParam) {
        Optional<Season> season = seasonRepository.byName(name);
        RestDate date = new RestDate(dateParam);
        Optional<LeagueMatchDate> matchDate = leagueMatchDateRepository.byDate(date.year(), date.month(), date.day());
        if (season.isPresent() && matchDate.isPresent()) {
            seasonService.planLeagueMatch(season.get(), matchDate.get());
            return Payload.ok();
        } else {
            return Payload.badRequest();
        }
    }

    private RestMatch toRestMatch(LeagueMatch m) {
        RestMatch restMatch = new RestMatch(m.date());
        m.players().map(Player::name).forEach(restMatch::addPlayer);
        return restMatch;
    }

}
