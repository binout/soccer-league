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
package io.github.binout.soccer.application.season;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.domain.season.MatchPlanning;
import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;
import io.github.binout.soccer.domain.season.match.LeagueMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Component
public class SubstitutePlayerInLeagueMatches {

    private final PlayerRepository playerRepository;
    private final SeasonRepository seasonRepository;
    private final MatchPlanning matchPlanning;

    @Autowired
    public SubstitutePlayerInLeagueMatches(PlayerRepository playerRepository,
                                           SeasonRepository seasonRepository,
                                           MatchPlanning matchPlanning) {
        this.playerRepository = playerRepository;
        this.seasonRepository = seasonRepository;
        this.matchPlanning = matchPlanning;
    }

    @Transactional
    public void execute(String seasonName, LocalDate date, String playerName)  {
        Season season = seasonRepository.byName(seasonName).orElseThrow(() -> new IllegalArgumentException("Invalid season"));
        Player player = playerRepository.byName(playerName).orElseThrow(() -> new IllegalArgumentException("Invalid player"));

        LeagueMatch match = season.leagueMatches()
                .filter(m -> m.date().equals(date))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid date"));

        matchPlanning.substitutePlayer(season, match, player);
    }
}