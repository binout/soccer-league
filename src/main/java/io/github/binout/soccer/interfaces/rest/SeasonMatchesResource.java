package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;
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
    SeasonRepository seasonRepository;

    @Inject
    SeasonService seasonService;

    @Inject
    FriendlyMatchDateRepository friendlyMatchDateRepository;

    @Inject
    LeagueMatchDateRepository leagueMatchDateRepository;

    @Inject
    PlayerRepository playerRepository;

    @Get("friendly")
    public Payload getFriendly(String name) {
        String seasonName = new SeasonName(name).name();
        Optional<Season> season = seasonRepository.byName(seasonName);
        if (!season.isPresent()) {
            return Payload.badRequest();
        } else {
            return new Payload(season.get().friendlyMatches()
                    .map(m -> toRestMatch(season.get(), m))
                    .collect(Collectors.toList()));
        }
    }

    private RestMatch toRestMatch(Season s, FriendlyMatch m) {
        RestMatch restMatch = new RestMatch(m.date());
        m.players().map(Player::name).forEach(restMatch::addPlayer);
        seasonService.getSubstitutes(s, m).stream().map(Player::name).forEach(restMatch::addSub);
        return restMatch;
    }

    @Get("friendly/to-plan")
    public Payload friendlyToPlan(String name) {
        String seasonName = new SeasonName(name).name();
        Optional<Season> season = seasonRepository.byName(seasonName);
        if (!season.isPresent()) {
            return Payload.badRequest();
        } else {
            return new Payload(seasonService.friendlyMatchDatesToPlan(season.get()).stream()
                    .map(FriendlyMatchDate::date)
                    .map(RestMatch::new)
                    .collect(Collectors.toList()));
        }
    }

    @Put("friendly/:dateParam")
    public Payload putFriendly(String name, String dateParam) {
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

    @Delete("friendly/:dateParam/players/:playerName")
    public Payload susbstitutePlayerFriendly(String name, String dateParam, String playerName) {
        String seasonName = new SeasonName(name).name();
        Optional<Season> season = seasonRepository.byName(seasonName);
        Optional<Player> player = playerRepository.byName(playerName);
        if (season.isPresent() && player.isPresent()) {
            RestDate date = new RestDate(dateParam);
            Optional<FriendlyMatch> match = season.get().friendlyMatches().filter(m -> m.date().equals(date.asLocalDate())).findFirst();
            if (!match.isPresent()) {
                return Payload.badRequest();
            }
            seasonService.substitutePlayer(season.get(), match.get(), player.get());
            return Payload.ok();
        } else {
            return Payload.badRequest();
        }
    }

    @Get("league")
    public Payload getLeague(String name) {
        String seasonName = new SeasonName(name).name();
        Optional<Season> season = seasonRepository.byName(seasonName);
        if (!season.isPresent()) {
            return Payload.badRequest();
        } else {
            return new Payload(season.get().leagueMatches()
                    .map(m -> toRestMatch(season.get(), m))
                    .collect(Collectors.toList()));
        }
    }

    @Get("league/to-plan")
    public Payload leagueToPlan(String name) {
        String seasonName = new SeasonName(name).name();
        Optional<Season> season = seasonRepository.byName(seasonName);
        if (!season.isPresent()) {
            return Payload.badRequest();
        } else {
            return new Payload(seasonService.leagueMatchDatesToPlan(season.get()).stream()
                    .map(LeagueMatchDate::date)
                    .map(RestMatch::new)
                    .collect(Collectors.toList()));
        }
    }

    @Put("league/:dateParam")
    public Payload putLeague(String name, String dateParam) {
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

    @Delete("league/:dateParam/players/:playerName")
    public Payload susbstitutePlayerLeague(String name, String dateParam, String playerName) {
        String seasonName = new SeasonName(name).name();
        Optional<Season> season = seasonRepository.byName(seasonName);
        Optional<Player> player = playerRepository.byName(playerName);
        if (season.isPresent() && player.isPresent()) {
            RestDate date = new RestDate(dateParam);
            Optional<LeagueMatch> match = season.get().leagueMatches().filter(m -> m.date().equals(date.asLocalDate())).findFirst();
            if (!match.isPresent()) {
                return Payload.badRequest();
            }
            seasonService.substitutePlayer(season.get(), match.get(), player.get());
            return Payload.ok();
        } else {
            return Payload.badRequest();
        }
    }

    private RestMatch toRestMatch(Season s, LeagueMatch m) {
        RestMatch restMatch = new RestMatch(m.date());
        m.players().map(Player::name).forEach(restMatch::addPlayer);
        seasonService.getSubstitutes(s, m).stream().map(Player::name).forEach(restMatch::addSub);
        return restMatch;
    }

}
