package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.season.match.LeagueMatch;
import org.mongolink.domain.mapper.ComponentMap;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class MongoLeagueMatchMapping extends ComponentMap<LeagueMatch> {

    @Override
    public void map() {
        property().onField("leagueDate");
        collection().onField("players");
    }
}
