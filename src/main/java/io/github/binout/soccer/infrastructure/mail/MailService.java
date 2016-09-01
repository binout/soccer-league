package io.github.binout.soccer.infrastructure.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public interface MailService {

    void sendMail(Mail mail);

    class Mail {
        private final String from;
        private final String subject;
        private final String content;
        private final List<String> tos;

        public Mail(String from, String subject, String content) {
            this.from = from;
            this.subject = subject;
            this.content = content;
            this.tos = new ArrayList<>();
        }

        public Mail addRecipient(String to) {
            this.tos.add(to);
            return this;
        }

        public boolean hasRecipients() {
            return !tos.isEmpty();
        }

        public String from() {
            return from;
        }

        public String subject() {
            return subject;
        }

        public String content() {
            return content;
        }

        public Stream<String> recipients() {
            return tos.stream();
        }
    }
}
