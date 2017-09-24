package io.github.binout.soccer.infrastructure.persistence.mongo;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.mongolink.MongoSessionManager;
import org.mongolink.Settings;
import org.mongolink.domain.mapper.ContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MongoConfiguration {

    @Bean
    public MongoDatabase database() {
        String uri = System.getenv("MONGODB_URI");
        if (uri == null) {
            Fongo fongo = new Fongo("Dev Server");
            return fongo.getDatabase("dev");
        } else {
            MongoClientURI mongoClientURI = new MongoClientURI(uri);
            MongoClient client = new MongoClient(mongoClientURI);
            return client.getDatabase(mongoClientURI.getDatabase());
        }
    }

    @Bean
    public MongoSessionManager sessionManager(MongoDatabase database) {
        ContextBuilder contextBuilder = new ContextBuilder(getClass().getPackage().getName());
        return MongoSessionManager.create(contextBuilder, Settings.defaultInstance().withDatabase(database));
    }

}
