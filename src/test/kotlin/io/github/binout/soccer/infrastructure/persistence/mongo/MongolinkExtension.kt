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

import com.mongodb.client.MongoDatabase
import org.junit.jupiter.api.extension.*
import org.mongolink.MongoSession
import org.mongolink.MongoSessionManager
import org.mongolink.Settings
import org.mongolink.domain.mapper.ContextBuilder
import java.lang.IllegalArgumentException

class MongolinkExtension : BeforeEachCallback, AfterEachCallback, ParameterResolver {

    val mongoDatabase = MongoConfiguration().database()
    val sessionManager = MongoSessionManager.create(ContextBuilder(javaClass.`package`.name), Settings.defaultInstance().withDatabase(mongoDatabase))

    lateinit var currentSession: MongoSession

    override fun beforeEach(executionContext: ExtensionContext) {
        currentSession = sessionManager.createSession()
        currentSession.start()
    }

    override fun afterEach(executionContext: ExtensionContext) {
        currentSession.stop()
        cleanDatabase()
    }

    private fun cleanDatabase() {
        for (collectionName in mongoDatabase.listCollectionNames()) {
            mongoDatabase.getCollection(collectionName).drop()
        }
    }

    override fun supportsParameter(parameterContext: ParameterContext, executionContext: ExtensionContext): Boolean =
            parameterContext.parameter.type == MongoSessionManager::class.java ||
                    parameterContext.parameter.type == MongoDatabase::class.java ||
                    parameterContext.parameter.type == MongoSession::class.java

    override fun resolveParameter(parameterContext: ParameterContext, executionContext: ExtensionContext): Any = when {
        parameterContext.parameter.type == MongoSessionManager::class.java -> sessionManager
        parameterContext.parameter.type == MongoDatabase::class.java -> mongoDatabase
        parameterContext.parameter.type == MongoSession::class.java -> currentSession
        else -> throw IllegalArgumentException()
    }
}