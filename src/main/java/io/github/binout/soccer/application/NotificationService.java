package io.github.binout.soccer.application;

import io.github.binout.soccer.domain.event.FriendlyMatchPlanned;
import io.github.binout.soccer.domain.event.LeagueMatchPlanned;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import io.github.binout.soccer.infrastructure.mail.MailService;
import io.github.binout.soccer.infrastructure.template.TemplateEngine;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class NotificationService {

    private static final String NO_REPLY = "no-reply@pes5.com";

    @Inject
    MailService mailService;

    @Inject
    PlayerRepository playerRepository;

    @Inject
    TemplateEngine templateEngine;

    public void newLeagueMatchPlanned(@Observes LeagueMatchPlanned event){
        String date = DateTimeFormatter.ISO_DATE.format(event.date());
        sendMail("ASL Soccer 5 League : " + date, date, event.players().collect(Collectors.toList()), event.substitutes().collect(Collectors.toList()));
    }

    public void newFriendlyMatchPlanned(@Observes FriendlyMatchPlanned event){
        String date = DateTimeFormatter.ISO_DATE.format(event.date());
        sendMail("ASL Soccer 5 : " + date, date, event.players().collect(Collectors.toList()), event.substitutes().collect(Collectors.toList()));
    }

    private void sendMail(String title, String date, List<String> players, List<String> subs) {
        String body = body(date, players, subs);
        MailService.Mail mail = new MailService.Mail(NO_REPLY, title, body);
        addRecipients(mail, players);
        mailService.sendMail(mail);
    }

    private String body(String date, List<String> players, List<String> subs) {
        Map<String, Object> params = new HashMap<>();
        params.put("date", date);
        params.put("players", players);
        params.put("subs", subs);
        return templateEngine.render("plan-match.ftlh", params);
    }

    private void addRecipients(MailService.Mail mail, List<String> players) {
        players.stream().map(p -> playerRepository.byName(p))
                .filter(Optional::isPresent).map(Optional::get)
                .map(Player::email)
                .filter(Optional::isPresent).map(Optional::get)
                .forEach(mail::addRecipient);
    }

}
