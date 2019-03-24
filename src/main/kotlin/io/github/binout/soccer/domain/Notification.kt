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
package io.github.binout.soccer.domain

import io.github.binout.soccer.domain.player.PlayerRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.HashMap

interface TemplateEngine {

    fun render(templateName: String, params: Map<String, Any>): String
}

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

        fun recipients(): List<String> {
            return tos
        }
    }
}


@Component
class NotificationService(
        private val mailService: MailService,
        private val playerRepository: PlayerRepository,
        private val templateEngine: TemplateEngine,
        @Value("\${app.url}") private val url: String,
        @Value("\${app.mail.no-reply}") private val noReply: String,
        @Value("\${app.mail.title}") private val title: String) {

    @EventListener
    fun newLeagueMatchPlanned(event: LeagueMatchPlanned) {
        val date = DateTimeFormatter.ISO_DATE.format(event.date)
        sendMail("$title League : $date", date, event.players, event.substitutes)
    }

    @EventListener
    fun newFriendlyMatchPlanned(event: FriendlyMatchPlanned) {
        val date = DateTimeFormatter.ISO_DATE.format(event.date)
        sendMail("$title : $date", date, event.players, event.substitutes)
    }

    private fun sendMail(title: String, date: String, players: List<String>, subs: List<String>) {
        val body = body(date, players, subs)
        val mail = MailService.Mail(noReply, title, body)
        addRecipients(mail, players)
        mailService.sendMail(mail)
    }

    private fun body(date: String, players: List<String>, subs: List<String>): String {
        val params = HashMap<String, Any>()
        params["date"] = date
        params["url"] = url
        params["players"] = players
        params["subs"] = subs
        return templateEngine.render("plan-match.ftlh", params)
    }

    private fun addRecipients(mail: MailService.Mail, players: List<String>) {
        players.mapNotNull { playerRepository.byName(it) }
                .mapNotNull { it.email }
                .filterNot { it.isEmpty() }
                .forEach { mail.addRecipient(it) }
    }

}

