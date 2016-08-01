package io.github.binout.soccer.domain.date;

import java.time.Month;
import java.util.Optional;
import java.util.stream.Stream;

public interface FriendlyMatchDateRepository {

    Stream<FriendlyMatchDate> all();

    void add(FriendlyMatchDate date);

    Optional<FriendlyMatchDate> byDate(int year, Month month, int dayOfMonth);

}
