/*
 * Copyright 2016 Benoît Prioux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.binout.soccer.infrastructure.persistence.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.junit.rules.ExternalResource;
import org.mongolink.*;
import org.mongolink.domain.mapper.ContextBuilder;

public class MongolinkRule extends ExternalResource {

    public static MongolinkRule withPackage(String... packagesToScan) {
        MongolinkRule result = new MongolinkRule();
        ContextBuilder contextBuilder = new ContextBuilder(packagesToScan);
        sesionManager = MongoSessionManager.create(contextBuilder, Settings.defaultInstance().withDatabase(mongoDatabase));
        return result;
    }

    private MongolinkRule() {
    }

    @Override
    public void before() throws Throwable {
        session = sesionManager.createSession();
        session.start();
    }

    @Override
    public void after() {
        session.stop();
        cleanDatabase();
    }

    public MongoDatabase getDatabase() {
        return mongoDatabase;
    }

    private void cleanDatabase() {
        for (String collectionName : mongoDatabase.listCollectionNames()) {
            mongoDatabase.getCollection(collectionName).drop();
        }
    }

    public void cleanSession() {
        session.flush();
        session.clear();
    }

    public MongoSession getCurrentSession() {
        return session;
    }

    private static MongoSessionManager sesionManager;
    private MongoSession session;
    private static final MongoDatabase mongoDatabase = new MongoConfiguration().database();

}