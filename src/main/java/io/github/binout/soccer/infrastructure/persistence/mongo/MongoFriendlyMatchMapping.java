package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.domain.season.match.FriendlyMatch;
import org.mongolink.domain.mapper.ComponentMap;

public class MongoFriendlyMatchMapping extends ComponentMap<FriendlyMatch> {

    @Override
    public void map() {
        property().onField("friendlyDate");
        collection().onField("players");
    }
}
