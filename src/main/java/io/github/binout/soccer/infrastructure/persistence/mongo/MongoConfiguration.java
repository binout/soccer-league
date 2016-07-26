package io.github.binout.soccer.infrastructure.persistence.mongo;

import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoDatabase;
import io.github.binout.soccer.infrastructure.persistence.TransactedScope;
import org.mongolink.MongoSession;
import org.mongolink.MongoSessionManager;
import org.mongolink.Settings;
import org.mongolink.domain.mapper.ContextBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class MongoConfiguration {

    @Produces
    @ApplicationScoped
    public MongoDatabase database() {
        Fongo fongo = new Fongo("Dev Server");
        return fongo.getDatabase("dev");
    }

    @Produces
    @ApplicationScoped
    public MongoSessionManager sessionManager(MongoDatabase database) {
        ContextBuilder contextBuilder = new ContextBuilder(getClass().getPackage().getName());
        return MongoSessionManager.create(contextBuilder, Settings.defaultInstance().withDatabase(database));
    }

    @Produces
    @TransactedScope
    public MongoSession session(MongoSessionManager sessionManager) {
        return sessionManager.createSession();
    }
}
