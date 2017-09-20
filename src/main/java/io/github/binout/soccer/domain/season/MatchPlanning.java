/*
 * Copyright 2016 Benoît Prioux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.binout.soccer.domain.season;

import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository;
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository;
import io.github.binout.soccer.domain.date.MatchDate;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import io.github.binout.soccer.domain.season.match.LeagueMatch;
import io.github.binout.soccer.domain.season.match.Match;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MatchPlanning {

    @Inject
    PlayerRepository playerRepository;

    @Inject
    FriendlyMatchDateRepository friendlyMatchDateRepository;

    @Inject
    LeagueMatchDateRepository leagueMatchDateRepository;

    public void substitutePlayer(Season season, Match match, Player player) {
        Player by = getSubstitute(season, match);
        MatchDate matchDate = getMatchDate(match).orElseThrow(() -> new IllegalArgumentException("Unknown match date"));
        if (matchDate.isAbsent(by)) {
            throw new IllegalArgumentException(by.name() + " is not present for this date");
        }
        match.replacePlayer(player, by);
        matchDate.absent(player);
    }

    private Player getSubstitute(Season season, Match match) {
        List<Player> substitutes = getSubstitutes(season, match);
        if (substitutes.isEmpty()) {
            throw new IllegalArgumentException("No substitutes available");
        }
        return substitutes.iterator().next();
    }

    private Optional<? extends MatchDate> getMatchDate(Match match) {
        LocalDate date = match.date();
        if (match instanceof FriendlyMatch) {
            return friendlyMatchDateRepository.byDate(date.getYear(), date.getMonth(), date.getDayOfMonth());
        } else {
            return leagueMatchDateRepository.byDate(date.getYear(), date.getMonth(), date.getDayOfMonth());
        }

    }

    public List<Player> getSubstitutes(Season season, Match match) {
        MatchDate date = getMatchDate(match).orElseThrow(() -> new IllegalArgumentException("Unknown match date"));
        List<String> players = match.players().collect(Collectors.toList());
        Comparator<Player> gamesPlayedComparator = getPlayerComparator(season, match);
        return playerRepository.all()
                .filter(date::isPresent)
                .filter(p -> !players.contains(p.name()))
                .sorted(gamesPlayedComparator)
                .collect(Collectors.toList());
    }

    private Comparator<Player> getPlayerComparator(Season season, Match match) {
        Function<Player, Integer> counter = match instanceof LeagueMatch ? leagueCounter(season) : globalCounter(season);
        return Comparator.comparingInt(counter::apply);
    }

    static Function<Player, Integer> leagueCounter(Season season) {
        return p -> season.statistics().leagueMatchPlayed(p);
    }

    static Function<Player, Integer> globalCounter(Season season) {
        return p -> season.statistics().matchPlayed(p);
    }

}
