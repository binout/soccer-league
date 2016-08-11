package io.github.binout.soccer.application;

import io.github.binout.soccer.domain.event.FriendlyMatchPlanned;
import io.github.binout.soccer.domain.event.LeagueMatchPlanned;
import io.github.binout.soccer.infrastructure.mail.MailService;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class NotificationService {

    @Inject
    MailService mailService;

    public void newLeagueMatchPlanned(@Observes LeagueMatchPlanned event){
        MailService.Mail mail = new MailService.Mail("admin@pes5.com", "New League match", "Date = " + event.date());
        event.players().forEach(mail::addRecipient);
        mailService.sendMail(mail);
    }

    public void newFriendlyMatchPlanned(@Observes FriendlyMatchPlanned event){
        MailService.Mail mail = new MailService.Mail("admin@pes5.com", "New Friendly match", "Date = " + event.date());
        event.players().forEach(mail::addRecipient);
        mailService.sendMail(mail);
    }

}
