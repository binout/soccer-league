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

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.ServerAddress
import com.mongodb.client.MongoDatabase
import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.StringUtils

@Configuration
class MongoConfiguration(@Value("\${app.mongodb.uri}") private val uri: String) {

    @Bean
    fun database(): MongoDatabase = if (StringUtils.isEmpty(uri)) {
        val mongoServer = MongoServer(MemoryBackend())
        val serverAddress = mongoServer.bind()
        val client = MongoClient(ServerAddress(serverAddress))
        client.getDatabase("dev")
    } else {
        val mongoClientURI = MongoClientURI(uri!!)
        val client = MongoClient(mongoClientURI)
        client.getDatabase(mongoClientURI.database!!)
    }

}
