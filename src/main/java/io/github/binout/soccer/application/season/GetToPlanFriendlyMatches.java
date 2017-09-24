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

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import io.github.binout.soccer.domain.season.SeasonRepository;
import io.github.binout.soccer.domain.season.SeasonPlanning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class GetToPlanFriendlyMatches {

    private final SeasonRepository seasonRepository;
    private final SeasonPlanning seasonPlanning;

    @Autowired
    public GetToPlanFriendlyMatches(SeasonRepository seasonRepository, SeasonPlanning seasonPlanning) {
        this.seasonRepository = seasonRepository;
        this.seasonPlanning = seasonPlanning;
    }

    public Stream<FriendlyMatchDate> execute(String seasonName) {
        return seasonRepository.byName(seasonName)
                .map(s -> seasonPlanning.friendlyMatchDatesToPlan(s).stream())
                .orElseThrow(() -> new IllegalArgumentException("Invalid season"));
    }
}