package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.application.player.GetAllPlayers;
import io.github.binout.soccer.application.season.*;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonStatistics;
import io.github.binout.soccer.domain.season.match.Match;
import io.github.binout.soccer.infrastructure.persistence.TransactedScopeEnabled;
import io.github.binout.soccer.interfaces.rest.model.*;
import net.codestory.http.Context;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Prefix("seasons")
@TransactedScopeEnabled
public class SeasonsResource {

    @Inject
    GetAllSeasons getAllSeasons;

    @Inject
    AddSeason addSeason;

    @Inject
    GetFriendlyMatches getFriendlyMatches;

    @Inject
    AddFriendlyMatch addFriendlyMatch;

    @Inject
    GetLeagueMatches getLeagueMatches;

    @Inject
    AddLeagueMatch addLeagueMatch;

    @Inject
    GetSeason getSeason;

    @Inject
    GetSeasonStats getSeasonStats;

    @Inject
    GetAllPlayers getAllPlayers;

    @Get
    public List<RestSeason> getAll(Context context) {
        return getAllSeasons.execute().map(s -> toRestModel(context.uri(), s)).collect(Collectors.toList());
    }

    @Put(":name")
    public Payload put(String name) {
        String seasonName = new SeasonName(name).name();
        addSeason.execute(seasonName);
        return Payload.ok();
    }

    @Get(":name/matches/friendly")
    public Payload getFriendlyMatch(String name) {
        String seasonName = new SeasonName(name).name();
        return getFriendlyMatches.execute(seasonName)
                .map(s -> s.map(SeasonsResource::toRestMatch).collect(Collectors.toList()))
                .map(Payload::new)
                .orElse(Payload.badRequest());
    }

    @Put(":name/matches/friendly/:dateParam")
    public Payload putFriendlyMatch(String name, String dateParam) {
        String seasonName = new SeasonName(name).name();
        RestDate date = new RestDate(dateParam);
        addFriendlyMatch.execute(seasonName, date.year(), date.month(), date.day());
        return Payload.ok();
    }

    @Get(":name/matches/league")
    public Payload getLeagueMatch(String name) {
        String seasonName = new SeasonName(name).name();
        return getLeagueMatches.execute(seasonName)
                .map(s -> s.map(SeasonsResource::toRestMatch).collect(Collectors.toList()))
                .map(Payload::new)
                .orElse(Payload.badRequest());
    }

    @Put(":name/matches/league/:dateParam")
    public Payload putLeagueMatch(String name, String dateParam) {
        String seasonName = new SeasonName(name).name();
        RestDate date = new RestDate(dateParam);
        addLeagueMatch.execute(seasonName, date.year(), date.month(), date.day());
        return Payload.ok();
    }

    @Get(":name")
    public Payload get(String name) {
        String seasonName = new SeasonName(name).name();
        return getSeason.execute(seasonName)
                .map(s -> new RestSeason(s.name()))
                .map(Payload::new)
                .orElse(Payload.notFound());
    }

    @Get(":name/stats")
    public Payload stats(String name) {
        String seasonName = new SeasonName(name).name();
        return getSeasonStats.execute(seasonName)
                .map(this::toRestStatList)
                .map(Payload::new)
                .orElse(Payload.notFound());
    }

    private static RestMatch toRestMatch(Match m) {
        RestMatch restMatch = new RestMatch(m.date());
        m.players().forEach(restMatch::addPlayer);
        return restMatch;
    }

    private List<RestStat> toRestStatList(SeasonStatistics s) {
        return getAllPlayers.execute()
                .map(p -> toRestStat(s, p))
                .sorted(Comparator.comparing(RestStat::getNbMatches).reversed())
                .collect(Collectors.toList());
    }

    private RestStat toRestStat(SeasonStatistics s, Player p) {
        RestStat restStat = new RestStat(p.name());
        restStat.setNbFriendlyMatches(s.friendlyMatchPlayed(p));
        restStat.setNbLeagueMatches(s.leagueMatchPlayed(p));
        restStat.setNbMatches(s.matchPlayed(p));
        return restStat;
    }

    private static RestSeason toRestModel(String baseUri, Season s) {
        RestSeason restSeason = new RestSeason(s.name());
        restSeason.addLinks(new RestLink(baseUri + "/" + s.name()));
        return restSeason;
    }

}
