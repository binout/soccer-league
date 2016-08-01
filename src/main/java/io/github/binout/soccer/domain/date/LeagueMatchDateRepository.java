package io.github.binout.soccer.domain.date;

import java.time.Month;
import java.util.Optional;
import java.util.stream.Stream;

public interface LeagueMatchDateRepository {

    Stream<LeagueMatchDate> all();

    void add(LeagueMatchDate date);

    Optional<LeagueMatchDate> byDate(int year, Month month, int dayOfMonth);

}
