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

import io.github.binout.soccer.domain.date.LeagueMatchDate;
import io.github.binout.soccer.domain.season.SeasonRepository;
import io.github.binout.soccer.domain.season.SeasonPlanning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GetToPlanLeagueMatches {

    private final SeasonRepository seasonRepository;
    private final SeasonPlanning seasonPlanning;

    @Autowired
    public GetToPlanLeagueMatches(SeasonRepository seasonRepository, SeasonPlanning seasonPlanning) {
        this.seasonRepository = seasonRepository;
        this.seasonPlanning = seasonPlanning;
    }

    @Transactional
    public List<LeagueMatchDate> execute(String seasonName) {
        return seasonRepository.byName(seasonName)
                .map(s -> seasonPlanning.leagueMatchDatesToPlan(s).stream())
                .orElseThrow(() -> new IllegalArgumentException("Invalid season"))
                .collect(Collectors.toList());
    }
}
