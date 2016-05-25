package io.github.binout.soccer.domain.date;

import java.time.Month;
import java.util.Optional;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public interface LeagueMatchDateRepository {

    void add(LeagueMatchDate date);

    Optional<LeagueMatchDate> byDate(int year, Month month, int dayOfMonth);

}
