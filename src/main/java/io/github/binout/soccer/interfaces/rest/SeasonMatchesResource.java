package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.application.season.*;
import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.season.match.Match;
import io.github.binout.soccer.interfaces.rest.model.RestDate;
import io.github.binout.soccer.interfaces.rest.model.RestMatch;
import io.vavr.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("rest/seasons/{name}/matches")
public class SeasonMatchesResource {

    @Autowired
    GetFriendlyMatches getFriendlyMatches;

    @Autowired
    GetNextFriendlyMatches getNextFriendlyMatches;

    @Autowired
    AddFriendlyMatch addFriendlyMatch;

    @Autowired
    GetLeagueMatches getLeagueMatches;

    @Autowired
    GetNextLeagueMatches getNextLeagueMatches;

    @Autowired
    AddLeagueMatch addLeagueMatch;

    @Autowired
    GetToPlanFriendlyMatches getToPlanFriendlyMatches;

    @Autowired
    GetToPlanLeagueMatches getToPlanLeagueMatches;

    @Autowired
    SubstitutePlayerInFriendlyMatches substitutePlayerInFriendlyMatches;

    @Autowired
    SubstitutePlayerInLeagueMatches substitutePlayerInLeagueMatches;

    @GetMapping("friendly")
    public ResponseEntity getFriendlyMatch(@PathVariable("name") String name) {
        String seasonName = new SeasonName(name).name();
        return ResponseEntity.ok(getFriendlyMatches.execute(seasonName).map(this::toRestMatch).collect(Collectors.toList()));
    }

    @PutMapping("friendly/{dateParam}")
    public ResponseEntity putFriendlyMatch(@PathVariable("name") String name, @PathVariable("dateParam") String dateParam) {
        String seasonName = new SeasonName(name).name();
        RestDate date = new RestDate(dateParam);
        addFriendlyMatch.execute(seasonName, date.year(), date.month(), date.day());
        return ResponseEntity.ok().build();
    }

    @GetMapping("friendly/next")
    public ResponseEntity getNextFriendly(@PathVariable("name") String name) {
        String seasonName = new SeasonName(name).name();
        return ResponseEntity.ok(getNextFriendlyMatches.execute(seasonName).map(this::toRestMatch).collect(Collectors.toList()));
    }

    @GetMapping("friendly/to-plan")
    public ResponseEntity friendlyToPlan(@PathVariable("name") String name) {
        String seasonName = new SeasonName(name).name();
        return ResponseEntity.ok(getToPlanFriendlyMatches.execute(seasonName)
                        .map(FriendlyMatchDate::date)
                        .map(RestMatch::new)
                        .collect(Collectors.toList()));
    }

    @DeleteMapping("friendly/{dateParam}/players/{playerName}")
    public ResponseEntity susbstitutePlayerFriendly(@PathVariable("name") String name, @PathVariable("dateParam") String dateParam, @PathVariable("playerName") String playerName) {
        String seasonName = new SeasonName(name).name();
        RestDate date = new RestDate(dateParam);
        substitutePlayerInFriendlyMatches.execute(seasonName, date.asLocalDate(), playerName);
        return ResponseEntity.ok().build();
    }


    @GetMapping("league")
    public ResponseEntity getLeagueMatch(@PathVariable("name") String name) {
        String seasonName = new SeasonName(name).name();
        return ResponseEntity.ok(getLeagueMatches.execute(seasonName).map(this::toRestMatch).collect(Collectors.toList()));
    }

    @PutMapping("league/{dateParam}")
    public ResponseEntity putLeagueMatch(@PathVariable("name") String name, @PathVariable("dateParam") String dateParam) {
        String seasonName = new SeasonName(name).name();
        RestDate date = new RestDate(dateParam);
        addLeagueMatch.execute(seasonName, date.year(), date.month(), date.day());
        return ResponseEntity.ok().build();
    }

    @GetMapping("league/next")
    public ResponseEntity getNextLeague(@PathVariable("name") String name) {
        String seasonName = new SeasonName(name).name();
        return ResponseEntity.ok(getNextLeagueMatches.execute(seasonName).map(this::toRestMatch).collect(Collectors.toList()));
    }

    @GetMapping("league/to-plan")
    public ResponseEntity leagueToPlan(@PathVariable("name") String name) {
        String seasonName = new SeasonName(name).name();
        return ResponseEntity.ok(getToPlanLeagueMatches.execute(seasonName)
                .map(LeagueMatchDate::date)
                .map(RestMatch::new)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("league/{dateParam}/players/{playerName}")
    public ResponseEntity susbstitutePlayerLeague(@PathVariable("name") String name, @PathVariable("dateParam") String dateParam, @PathVariable("playerName") String playerName) {
        String seasonName = new SeasonName(name).name();
        RestDate date = new RestDate(dateParam);
        substitutePlayerInLeagueMatches.execute(seasonName, date.asLocalDate(), playerName);
        return ResponseEntity.ok().build();
    }


    private RestMatch toRestMatch(Tuple2<? extends Match,List<Player>> m) {
        RestMatch restMatch = new RestMatch(m._1.date());
        m._1.players().forEach(restMatch::addPlayer);
        m._2.stream().map(Player::name).forEach(restMatch::addSub);
        return restMatch;
    }
}
