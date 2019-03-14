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
package io.github.binout.soccer.infrastructure.persistence.mongo

import io.github.binout.soccer.infrastructure.transaction.TransactionManager
import org.mongolink.MongoSession
import org.mongolink.MongoSessionManager
import org.springframework.stereotype.Component

@Component
class MongoSessionTransactionManager(private val sessionManager: MongoSessionManager)
    : TransactionManager<MongoSession> {

    override fun doGetTransaction(): MongoSession {
        return SESSIONS.get()
    }

    override fun doBegin() {
        val mongoSession = this.sessionManager.createSession()
        mongoSession.start()
        SESSIONS.set(mongoSession)
    }

    override fun doCommit() {
        val mongoSession = SESSIONS.get()
        mongoSession.stop()
        SESSIONS.remove()
    }

    override fun doRollback() {
        val mongoSession = SESSIONS.get()
        mongoSession.clear()
        SESSIONS.remove()
    }

    companion object {

        private val SESSIONS = ThreadLocal<MongoSession>()
    }
}
