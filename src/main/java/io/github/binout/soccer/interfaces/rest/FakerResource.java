package io.github.binout.soccer.interfaces.rest;

import com.github.javafaker.Faker;
import io.github.binout.soccer.domain.date.*;
import io.github.binout.soccer.domain.player.Player;
import io.github.binout.soccer.domain.player.PlayerRepository;
import net.codestory.http.Request;
import net.codestory.http.annotations.Post;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * LECTRA
 *
 * @author b.prioux
 */
@Prefix("faker")
public class FakerResource {

    @Inject
    PlayerRepository playerRepository;

    @Inject
    LeagueMatchDateRepository leagueMatchDateRepository;

    @Inject
    FriendlyMatchDateRepository friendlyMatchDateRepository;

    @Post
    public Payload fakes(Request request) {
        Faker faker = new Faker();
        int players = request.query().getInteger("players", 0);
        IntStream.range(0, players).mapToObj(i -> faker.name().fullName()).map(Player::new).forEach(playerRepository::add);

        int friendly = request.query().getInteger("friendly", 0);
        IntStream.range(0, friendly).mapToObj(i -> faker.date().future(7, TimeUnit.DAYS, new Date())).map(FakerResource::toFriendlyMatchDate).forEach(friendlyMatchDateRepository::add);

        int league = request.query().getInteger("league", 0);
        IntStream.range(0, league).mapToObj(i -> faker.date().future(7, TimeUnit.DAYS, new Date())).map(FakerResource::toLeagueMatchDate).forEach(leagueMatchDateRepository::add);

        return Payload.ok();
    }

    private static FriendlyMatchDate toFriendlyMatchDate(Date d) {
        LocalDate localDate = LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault()).toLocalDate();
        int year = localDate.get(ChronoField.YEAR);
        Month month = Month.of(localDate.get(ChronoField.MONTH_OF_YEAR));
        int day = localDate.get(ChronoField.DAY_OF_MONTH);
        return  MatchDate.newDateForFriendly(year, month, day);
    }

    private static LeagueMatchDate toLeagueMatchDate(Date d) {
        LocalDate localDate = LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault()).toLocalDate();
        int year = localDate.get(ChronoField.YEAR);
        Month month = Month.of(localDate.get(ChronoField.MONTH_OF_YEAR));
        int day = localDate.get(ChronoField.DAY_OF_MONTH);
        return  MatchDate.newDateForLeague(year, month, day);
    }

}
