package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.date.FriendlyMatchDate;
import org.mongolink.domain.mapper.AggregateMap;

public class MongoFriendlyMatchDateMapping extends AggregateMap<FriendlyMatchDate> {

    @Override
    public void map() {
        id().onField("id").natural();
        property().onField("date");
        collection().onField("presents");
    }
}
