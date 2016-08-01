package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.season.Season;
import org.mongolink.domain.mapper.AggregateMap;

public class MongoSeasonMapping extends AggregateMap<Season> {

    @Override
    public void map() {
        id().onField("id").natural();
        property().onField("name");
        collection().onField("friendlyMatches");
        collection().onField("leagueMatches");
    }
}
