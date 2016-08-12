package io.github.binout.soccer.application;

import io.github.binout.soccer.domain.event.FriendlyMatchPlanned;
import io.github.binout.soccer.domain.event.LeagueMatchPlanned;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.infrastructure.mail.MailService;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

public class NotificationService {

    @Inject
    MailService mailService;

    @Inject
    PlayerRepository playerRepository;

    public void newLeagueMatchPlanned(@Observes LeagueMatchPlanned event){
        MailService.Mail mail = new MailService.Mail("admin@pes5.com", "New League match", "Date = " + event.date());
        addRecipients(mail, event.players());
        mailService.sendMail(mail);
    }

    private void addRecipients(MailService.Mail mail, Stream<String> players) {
        players.map(p -> playerRepository.byName(p))
                .filter(Optional::isPresent).map(Optional::get)
                .map(Player::email)
                .filter(Optional::isPresent).map(Optional::get)
                .forEach(mail::addRecipient);
    }

    public void newFriendlyMatchPlanned(@Observes FriendlyMatchPlanned event){
        MailService.Mail mail = new MailService.Mail("admin@pes5.com", "New Friendly match", "Date = " + event.date());
        addRecipients(mail, event.players());
        mailService.sendMail(mail);
    }

}
