package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Restrictions;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class MongoPlayerRepository implements PlayerRepository {

    @Inject
    MongoSession mongoSession;

    @Override
    public void add(Player player) {
        mongoSession.save(player);
    }

    @Override
    public Stream<Player> all() {
        return mongoSession.createCriteria(Player.class).list().stream();
    }

    @Override
    public Optional<Player> byName(String name) {
        Criteria criteria = mongoSession.createCriteria(Player.class);
        criteria.add(Restrictions.equals("name", name));
        return criteria.list().stream().findFirst();
    }
}
