package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.player.Player;
import org.mongolink.domain.mapper.AggregateMap;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class MongoPlayerMapping extends AggregateMap<Player> {

    @Override
    public void map() {
        id().onField("id").natural();
        property().onField("name");
        property().onField("email");
        property().onField("isPlayerLeague");
    }
}
