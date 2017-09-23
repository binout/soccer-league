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
package io.github.binout.soccer.application.date;

import io.github.binout.soccer.domain.date.FriendlyMatchDateRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Month;

@Component
public class AddPlayerToFriendlyMatchDate {

    private final FriendlyMatchDateRegistration friendlyMatchDateRegistration;

    @Autowired
    public AddPlayerToFriendlyMatchDate(FriendlyMatchDateRegistration friendlyMatchDateRegistration) {
        this.friendlyMatchDateRegistration = friendlyMatchDateRegistration;
    }

    public void execute(String playerName, int year, Month month, int day) {
        friendlyMatchDateRegistration.addPlayer(playerName, year, month, day);
    }
}
