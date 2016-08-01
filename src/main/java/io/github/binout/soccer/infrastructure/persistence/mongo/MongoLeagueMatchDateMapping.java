package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.date.LeagueMatchDate;
import org.mongolink.domain.mapper.AggregateMap;

public class MongoLeagueMatchDateMapping extends AggregateMap<LeagueMatchDate> {

    @Override
    public void map() {
        id().onField("id").natural();
        property().onField("date");
        collection().onField("presents");
    }
}
