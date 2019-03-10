package io.github.binout.soccer.infrastructure.mail

import io.github.binout.soccer.domain.notification.MailService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class SendGridMailServiceTest {

    @Test
    fun toSendGridMail() {
        val test = MailService.Mail("from@google.fr", "Test", "Hello World")
        test.addRecipient("to@google.fr")

        val json = SendGridMailService.toSendGridMail(test)

        Assertions.assertThat(json).isEqualTo("{" +
                "\"personalizations\":[" +
                "{" +
                "\"subject\":\"Test\"," +
                "\"to\":[" +
                "{" +
                "\"email\":\"to@google.fr\"" +
                "}" +
                "]" +
                "}" +
                "]," +
                "\"from\":" +
                "{" +
                "\"email\":\"from@google.fr\"" +
                "}," +
                "\"content\":[" +
                "{" +
                "\"type\":\"text/html\"," +
                "\"value\":\"Hello World\"" +
                "}" +
                "]" +
                "}")
    }

}