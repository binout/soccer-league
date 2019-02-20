package io.github.binout.soccer.infrastructure.persistence.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import org.mongolink.MongoSessionManager;
import org.mongolink.Settings;
import org.mongolink.domain.mapper.ContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;

@Configuration
public class MongoConfiguration {

    @Value("${app.mongodb.uri}")
    private String uri;

    @Bean
    public MongoDatabase database() {
        if (StringUtils.isEmpty(uri)) {
            MongoServer mongoServer = new MongoServer(new MemoryBackend());
            InetSocketAddress serverAddress = mongoServer.bind();
            MongoClient client = new MongoClient(new ServerAddress(serverAddress));
            return client.getDatabase("dev");
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
