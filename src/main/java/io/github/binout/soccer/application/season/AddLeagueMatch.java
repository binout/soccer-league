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
import io.github.binout.soccer.domain.date.LeagueMatchDateRepository;
import io.github.binout.soccer.domain.season.Season;
import io.github.binout.soccer.domain.season.SeasonRepository;
import io.github.binout.soccer.domain.season.SeasonPlanning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.Optional;

@Component
public class AddLeagueMatch {

    private final SeasonRepository seasonRepository;
    private final LeagueMatchDateRepository leagueMatchDateRepository;
    private final SeasonPlanning seasonPlanning;

    @Autowired
    public AddLeagueMatch(SeasonRepository seasonRepository,
                          LeagueMatchDateRepository leagueMatchDateRepository,
                          SeasonPlanning seasonPlanning) {
        this.seasonRepository = seasonRepository;
        this.leagueMatchDateRepository = leagueMatchDateRepository;
        this.seasonPlanning = seasonPlanning;
    }

    public void execute(String seasonName, int year, Month month, int day) {
        Optional<Season> season = seasonRepository.byName(seasonName);
        Optional<LeagueMatchDate> matchDate = leagueMatchDateRepository.byDate(year, month, day);
        if (season.isPresent() && matchDate.isPresent()) {
            seasonPlanning.planLeagueMatch(season.get(), matchDate.get());
        } else {
            throw new IllegalArgumentException("Can not add match to season");
        }
    }
}
