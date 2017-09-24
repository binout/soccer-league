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
import io.github.binout.soccer.domain.season.MatchPlanning;
import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;
import io.github.binout.soccer.domain.season.match.LeagueMatch;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

@Component
public class GetNextLeagueMatches {

    private final SeasonRepository seasonRepository;
    private final MatchPlanning matchPlanning;

    @Autowired
    public GetNextLeagueMatches(SeasonRepository seasonRepository, MatchPlanning matchPlanning) {
        this.seasonRepository = seasonRepository;
        this.matchPlanning = matchPlanning;
    }

    @Transactional
    public Stream<Tuple2<LeagueMatch,List<Player>>> execute(String seasonName) {
        Season season = seasonRepository.byName(seasonName).orElseThrow(() -> new IllegalArgumentException("Invalid season"));
        return season.leagueMatches()
                .filter(LeagueMatch::isNowOrFuture)
                .map(m -> Tuple.of(m, matchPlanning.getSubstitutes(season, m)));
    }
}
