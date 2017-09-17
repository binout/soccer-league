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
package io.github.binout.soccer.domain.date;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;

import javax.inject.Inject;
import java.time.Month;
import java.util.Optional;
import java.util.function.BiConsumer;

public class FriendlyMatchDateRegistration {

    @Inject
    FriendlyMatchDateRepository repository;

    @Inject
    PlayerRepository playerRepository;

    public void addPlayer(String playerName, int year, Month month, int day) {
        managePlayers(playerName, year, month, day, FriendlyMatchDate::present);
    }

    public void removePlayer(String playerName, int year, Month month, int day) {
        managePlayers(playerName, year, month, day, FriendlyMatchDate::absent);
    }

    private void managePlayers(String playerName, int year, Month month, int day, BiConsumer<FriendlyMatchDate, Player> inscription) {
        Optional<FriendlyMatchDate> friendlyMatchDate = repository.byDate(year, month, day);
        Optional<Player> player = playerRepository.byName(playerName);
        if (player.isPresent() && friendlyMatchDate.isPresent()) {
            inscription.accept(friendlyMatchDate.get(), player.get());
        } else {
            throw new IllegalArgumentException("Player or date is invalid");
        }
    }
}
