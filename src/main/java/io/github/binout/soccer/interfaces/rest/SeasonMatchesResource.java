package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.application.season.*;
import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonService;
import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import io.github.binout.soccer.domain.season.match.LeagueMatch;
import io.github.binout.soccer.infrastructure.persistence.TransactedScopeEnabled;
import io.github.binout.soccer.interfaces.rest.model.RestDate;
import io.github.binout.soccer.interfaces.rest.model.RestMatch;
import net.codestory.http.annotations.Delete;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Collectors;

@Prefix("seasons/:name/matches")
@TransactedScopeEnabled
public class SeasonMatchesResource {

    @Inject
    GetSeason getSeason;

    @Inject
    GetFriendlyMatches getFriendlyMatches;

    @Inject
    GetNextFriendlyMatches getNextFriendlyMatches;

    @Inject
    AddFriendlyMatch addFriendlyMatch;

    @Inject
    GetLeagueMatches getLeagueMatches;

    @Inject
    GetNextLeagueMatches getNextLeagueMatches;

    @Inject
    AddLeagueMatch addLeagueMatch;

    @Inject
    SeasonService seasonService;

    @Inject
    GetToPlanFriendlyMatches getToPlanFriendlyMatches;

    @Inject
    GetToPlanLeagueMatches getToPlanLeagueMatches;

    @Inject
    SubstitutePlayerInFriendlyMatches substitutePlayerInFriendlyMatches;

    @Inject
    SubstitutePlayerInLeagueMatches substitutePlayerInLeagueMatches;

    @Get("friendly")
    public Payload getFriendlyMatch(String name) {
        String seasonName = new SeasonName(name).name();
        Optional<Season> season = getSeason.execute(name);
        return season.map(s -> getFriendlyMatches.execute(seasonName).map(m -> toRestMatch(s, m)).collect(Collectors.toList()))
                .map(Payload::new)
                .orElseGet(Payload::badRequest);
    }

    @Put("friendly/:dateParam")
    public Payload putFriendlyMatch(String name, String dateParam) {
        String seasonName = new SeasonName(name).name();
        RestDate date = new RestDate(dateParam);
        addFriendlyMatch.execute(seasonName, date.year(), date.month(), date.day());
        return Payload.ok();
    }

    @Get("friendly/next")
    public Payload getNextFriendly(String name) {
        String seasonName = new SeasonName(name).name();
        Optional<Season> season = getSeason.execute(seasonName);
        return season.map(s -> getNextFriendlyMatches.execute(s.name()).map(m -> toRestMatch(s, m)).collect(Collectors.toList()))
                .map(Payload::new)
                .orElseGet(Payload::badRequest);
    }

    @Get("friendly/to-plan")
    public Payload friendlyToPlan(String name) {
        String seasonName = new SeasonName(name).name();
        return new Payload(getToPlanFriendlyMatches.execute(seasonName)
                        .map(FriendlyMatchDate::date)
                        .map(RestMatch::new)
                        .collect(Collectors.toList()));
    }

    @Delete("friendly/:dateParam/players/:playerName")
    public Payload susbstitutePlayerFriendly(String name, String dateParam, String playerName) {
        String seasonName = new SeasonName(name).name();
        RestDate date = new RestDate(dateParam);
        substitutePlayerInFriendlyMatches.execute(seasonName, date.asLocalDate(), playerName);
        return Payload.ok();
    }


    private RestMatch toRestMatch(Season s, FriendlyMatch m) {
        RestMatch restMatch = new RestMatch(m.date());
        m.players().forEach(restMatch::addPlayer);
        seasonService.getSubstitutes(s, m).stream().map(Player::name).forEach(restMatch::addSub);
        return restMatch;
    }

    @Get("league")
    public Payload getLeagueMatch(String name) {
        String seasonName = new SeasonName(name).name();
        Optional<Season> season = getSeason.execute(name);
        return season.map(s -> getLeagueMatches.execute(seasonName).map(m -> toRestMatch(s, m)).collect(Collectors.toList()))
                .map(Payload::new)
                .orElseGet(Payload::badRequest);
    }

    @Put("league/:dateParam")
    public Payload putLeagueMatch(String name, String dateParam) {
        String seasonName = new SeasonName(name).name();
        RestDate date = new RestDate(dateParam);
        addLeagueMatch.execute(seasonName, date.year(), date.month(), date.day());
        return Payload.ok();
    }

    @Get("league/next")
    public Payload getNextLeague(String name) {
        String seasonName = new SeasonName(name).name();
        Optional<Season> season = getSeason.execute(seasonName);
        return season.map(s -> getNextLeagueMatches.execute(s.name()).map(m -> toRestMatch(s, m)).collect(Collectors.toList()))
                .map(Payload::new)
                .orElseGet(Payload::badRequest);
    }

    @Get("league/to-plan")
    public Payload leagueToPlan(String name) {
        String seasonName = new SeasonName(name).name();
        return new Payload(getToPlanLeagueMatches.execute(seasonName)
                .map(LeagueMatchDate::date)
                .map(RestMatch::new)
                .collect(Collectors.toList()));
    }

    @Delete("league/:dateParam/players/:playerName")
    public Payload susbstitutePlayerLeague(String name, String dateParam, String playerName) {
        String seasonName = new SeasonName(name).name();
        RestDate date = new RestDate(dateParam);
        substitutePlayerInLeagueMatches.execute(seasonName, date.asLocalDate(), playerName);
        return Payload.ok();
    }

    private RestMatch toRestMatch(Season s, LeagueMatch m) {
        RestMatch restMatch = new RestMatch(m.date());
        m.players().forEach(restMatch::addPlayer);
        seasonService.getSubstitutes(s, m).stream().map(Player::name).forEach(restMatch::addSub);
        return restMatch;
    }

}
