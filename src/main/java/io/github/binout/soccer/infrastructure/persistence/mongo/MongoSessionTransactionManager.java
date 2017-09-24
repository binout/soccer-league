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

import io.github.binout.soccer.infrastructure.transaction.TransactionManager;
import org.mongolink.MongoSession;
import org.mongolink.MongoSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoSessionTransactionManager implements TransactionManager<MongoSession> {

    private final static ThreadLocal<MongoSession> SESSIONS = new ThreadLocal<>();
    private final MongoSessionManager sessionManager;

    @Autowired
    public MongoSessionTransactionManager(MongoSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public MongoSession doGetTransaction() {
        return SESSIONS.get();
    }

    @Override
    public void doBegin() {
        MongoSession mongoSession = this.sessionManager.createSession();
        mongoSession.start();
        SESSIONS.set(mongoSession);
    }

    @Override
    public void doCommit() {
        MongoSession mongoSession = SESSIONS.get();
        mongoSession.stop();
        SESSIONS.remove();
    }

    @Override
    public void doRollback() {
        MongoSession mongoSession = SESSIONS.get();
        mongoSession.clear();
        SESSIONS.remove();
    }
}
