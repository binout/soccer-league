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
package io.github.binout.soccer.infrastructure.mail

import feign.Feign
import feign.Headers
import feign.RequestLine
import io.github.binout.soccer.domain.MailService
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.*

@Component
class SendGridMailService : MailService {

    @Value("\${app.mail.sendgrid.url}")
    private val sendGridUrl: String? = null

    @Value("\${app.mail.sendgrid.api-key}")
    private val sendGridApiKey: String? = null

    override fun sendMail(mail: MailService.Mail) {
        if (!sendGridApiKey.isNullOrBlank()) {
            if (mail.hasRecipients()) {
                val jsonMail = toSendGridMail(mail)
                try {
                    Feign.builder()
                            .requestInterceptor { r -> r.header("Authorization", "Bearer $sendGridApiKey") }
                            .target(SendGrid::class.java, sendGridUrl)
                            .sendMail(jsonMail)
                } catch (e: Throwable) {
                    println(this.javaClass.name + " : " + "Cannot send mail to sendgrid api : $jsonMail")
                    println(this.javaClass.name + " : " + Arrays.toString(e.stackTrace))
                }

            }
        } else {
            println(this.javaClass.name + " : " + "No SendGrid Configuration")
        }
    }

    internal interface SendGrid {

        @Headers("Content-Type: application/json")
        @RequestLine("POST /mail/send")
        fun sendMail(body: String)
    }

    companion object {

        internal fun toSendGridMail(mail: MailService.Mail): String {
            try {
                val jsonObject = JSONObject()
                jsonObject.put("from", toEmail(mail.from()))
                jsonObject.put("personalizations", toPersonalizations(mail))
                jsonObject.put("content", toContent(mail))
                return jsonObject.toString()
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }

        }

        private fun toContent(mail: MailService.Mail): JSONArray {
            try {
                val jsonArray = JSONArray()
                val jsonObject = JSONObject()
                jsonObject.put("type", "text/html")
                jsonObject.put("value", mail.content())
                jsonArray.put(jsonObject)
                return jsonArray
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }

        }

        private fun toPersonalizations(mail: MailService.Mail): JSONArray {
            try {
                val jsonArray = JSONArray()
                val jsonObject = JSONObject()
                jsonObject.put("to", toTos(mail))
                jsonObject.put("subject", mail.subject())
                jsonArray.put(jsonObject)
                return jsonArray
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }

        }

        private fun toTos(mail: MailService.Mail): JSONArray {
            val tos = JSONArray()
            mail.recipients().map { toEmail(it) }.forEach { tos.put(it) }
            return tos
        }

        private fun toEmail(address: String): JSONObject {
            try {
                return JSONObject().put("email", address)
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }

        }
    }
}
