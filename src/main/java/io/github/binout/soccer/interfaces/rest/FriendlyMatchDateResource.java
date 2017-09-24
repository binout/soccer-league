package io.github.binout.soccer.interfaces.rest;

import io.github.binout.soccer.application.date.*;
import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.interfaces.rest.model.RestDate;
import io.github.binout.soccer.interfaces.rest.model.RestMatchDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("match-dates/friendly")
public class FriendlyMatchDateResource {

    @Autowired
    GetAllFriendlyMatchDates allFriendlyMatchDates;

    @Autowired
    GetNextFriendlyMatchDates nextFriendlyMatchDates;

    @Autowired
    AddFriendlyMatchDate addFriendlyMatchDate;

    @Autowired
    GetFriendlyMatchDate getFriendlyMatchDate;

    @Autowired
    AddPlayerToFriendlyMatchDate addPlayerToFriendlyMatchDate;

    @Autowired
    RemovePlayerToFriendlyMatchDate removePlayerToFriendlyMatchDate;

    @GetMapping
    public List<RestMatchDate> all() {
        return allFriendlyMatchDates.execute()
                .map(this::toRestModel)
                .collect(Collectors.toList());
    }

    @GetMapping("next")
    public List<RestMatchDate> next() {
        return nextFriendlyMatchDates.execute()
                .map(this::toRestModel)
                .collect(Collectors.toList());
    }

    private RestMatchDate toRestModel(FriendlyMatchDate m) {
        RestMatchDate restMatchDate = toRestModel(m);
        //restMatchDate.addLinks(new RestLink(baseUri + "/" + restMatchDate.getDate()));
        return restMatchDate;
    }

    @PutMapping("{dateParam}")
    public ResponseEntity put(@PathParam("dateParam") String dateParam) {
        RestDate date = new RestDate(dateParam);
        addFriendlyMatchDate.execute(date.year(), date.month(), date.day());
        return ResponseEntity.ok().build();
    }

    @GetMapping("{dateParam}")
    public ResponseEntity get(@PathParam("dateParam") String dateParam) {
        RestDate date = new RestDate(dateParam);
        return getFriendlyMatchDate.execute(date.year(), date.month(), date.day())
                .map(this::toRestModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private RestMatchDate toRestModel(MatchDate m) {
        RestMatchDate restMatchDate = new RestMatchDate(m.date());
        m.presents().forEach(restMatchDate::addPresent);
        restMatchDate.setCanBePlanned(m.canBePlanned());
        return restMatchDate;
    }

    @PutMapping("{dateParam}/players/{name}")
    public ResponseEntity putPlayers(@PathParam("dateParam") String dateParam, @PathParam("name") String name) {
        RestDate date = new RestDate(dateParam);
        addPlayerToFriendlyMatchDate.execute(name, date.year(), date.month(), date.day());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{dateParam}/players/{name}")
    public ResponseEntity deletePlayers(@PathParam("dateParam") String dateParam, @PathParam("name") String name) {
        RestDate date = new RestDate(dateParam);
        removePlayerToFriendlyMatchDate.execute(name, date.year(), date.month(), date.day());
        return ResponseEntity.ok().build();
    }

}
