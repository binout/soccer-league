package io.github.binout.soccer.domain.date;

import java.time.Month;
import java.util.Optional;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public interface FriendlyMatchDateRepository {

    void add(FriendlyMatchDate date);

    Optional<FriendlyMatchDate> byDate(int year, Month month, int dayOfMonth);

}
