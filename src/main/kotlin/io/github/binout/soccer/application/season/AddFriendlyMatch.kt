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
package io.github.binout.soccer.application.season

import io.github.binout.soccer.domain.date.FriendlyMatchDateRepository
import io.github.binout.soccer.domain.season.SeasonPlanning
import io.github.binout.soccer.domain.season.SeasonRepository
import org.springframework.stereotype.Component
import java.time.Month

@Component
class AddFriendlyMatch(
        private val seasonRepository: SeasonRepository,
        private val friendlyMatchDateRepository: FriendlyMatchDateRepository,
        private val seasonPlanning: SeasonPlanning) {

    fun execute(seasonName: String, year: Int, month: Month, day: Int) {
        val season = seasonRepository.byName(seasonName) ?: throw IllegalArgumentException("Can not add match to season")
        val matchDate = friendlyMatchDateRepository.byDate(year, month, day) ?: throw IllegalArgumentException("Can not add match to season")
        seasonPlanning.planFriendlyMatch(season, matchDate)
    }
}
