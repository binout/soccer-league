package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Restrictions;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class MongoPlayerRepository extends MongoRepository<Player> implements PlayerRepository {

    @Inject
    MongoPlayerRepository(MongoSession mongoSession) {
        super(mongoSession);
    }

    @Override
    protected Class<Player> clazz() {
        return Player.class;
    }

    @Override
    public void add(Player player) {
        super.add(player, player::id);
    }

    @Override
    public Optional<Player> byName(String name) {
        return super.findBy(Restrictions.equals("name", name)).findFirst();
    }

    @Override
    public Stream<Player> all() {
        return super.all();
    }
}
