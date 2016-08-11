package io.github.binout.soccer.infrastructure.mail;

import com.sendgrid.*;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * LECTRA
 *
 * @author b.prioux
 */
class SendGridMailService implements MailService {

    @Override
    public void sendMail(Mail email) {
        String apiKey = System.getenv("SENDGRID_API_KEY");
        if (apiKey != null) {
            try {
                SendGrid sg = new SendGrid(apiKey);
                Request request = new Request();
                request.method = Method.POST;
                request.endpoint = "mail/send";
                request.body = toSendGridMail(email).build();
                sg.api(request);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
    }

    private com.sendgrid.Mail toSendGridMail(Mail email) {
        com.sendgrid.Mail mail = new com.sendgrid.Mail();
        mail.setFrom(new Email(email.from()));
        mail.setSubject(email.subject());
        mail.addContent(new Content("text/plain", email.content()));
        Personalization personalization = new Personalization();
        email.recipients().map(Email::new).forEach(personalization::addTo);
        mail.addPersonalization(personalization);
        return mail;
    }
}
