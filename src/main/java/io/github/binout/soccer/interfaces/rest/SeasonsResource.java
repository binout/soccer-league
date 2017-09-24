package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.application.player.GetAllPlayers;
import io.github.binout.soccer.application.season.AddSeason;
import io.github.binout.soccer.application.season.GetAllSeasons;
import io.github.binout.soccer.application.season.GetSeason;
import io.github.binout.soccer.application.season.GetSeasonStats;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonStatistics;
import io.github.binout.soccer.interfaces.rest.model.RestSeason;
import io.github.binout.soccer.interfaces.rest.model.RestStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("seasons")
public class SeasonsResource {

    @Autowired
    GetAllSeasons getAllSeasons;

    @Autowired
    AddSeason addSeason;

    @Autowired
    GetSeason getSeason;

    @Autowired
    GetSeasonStats getSeasonStats;

    @Autowired
    GetAllPlayers getAllPlayers;

    @GetMapping
    public List<RestSeason> getAll() {
        return getAllSeasons.execute().map(s -> toRestModel(s)).collect(Collectors.toList());
    }

    @PutMapping("{name}")
    public ResponseEntity put(@PathParam("name") String name) {
        String seasonName = new SeasonName(name).name();
        addSeason.execute(seasonName);
        return ResponseEntity.ok().build();
    }


    @GetMapping("{name}")
    public ResponseEntity get(@PathParam("name") String name) {
        String seasonName = new SeasonName(name).name();
        return getSeason.execute(seasonName)
                .map(s -> new RestSeason(s.name()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("{name}/stats")
    public ResponseEntity stats(@PathParam("name") String name) {
        String seasonName = new SeasonName(name).name();
        return getSeasonStats.execute(seasonName)
                .map(this::toRestStatList)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

    private static RestSeason toRestModel(Season s) {
        RestSeason restSeason = new RestSeason(s.name());
        //restSeason.addLinks(new RestLink(baseUri + "/" + s.name()));
        return restSeason;
    }

}
