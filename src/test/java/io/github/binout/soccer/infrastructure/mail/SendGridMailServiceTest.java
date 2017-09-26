package io.github.binout.soccer.infrastructure.mail;

import io.github.binout.soccer.domain.notification.MailService;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SendGridMailServiceTest {

    @Test
    public void toSendGridMail() {
        MailService.Mail test = new MailService.Mail("from@google.fr", "Test", "Hello World");
        test.addRecipient("to@google.fr");

        String json = SendGridMailService.toSendGridMail(test);

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
                "}");
    }

}