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
package io.github.binout.soccer.domain.notification

import java.util.ArrayList
import java.util.stream.Stream

interface MailService {

    fun sendMail(mail: Mail)

    class Mail(private val from: String, private val subject: String, private val content: String) {
        private val tos: MutableList<String>

        init {
            this.tos = ArrayList()
        }

        fun addRecipient(to: String): Mail {
            this.tos.add(to)
            return this
        }

        fun hasRecipients(): Boolean {
            return !tos.isEmpty()
        }

        fun from(): String {
            return from
        }

        fun subject(): String {
            return subject
        }

        fun content(): String {
            return content
        }

        fun recipients(): Stream<String> {
            return tos.stream()
        }
    }
}
