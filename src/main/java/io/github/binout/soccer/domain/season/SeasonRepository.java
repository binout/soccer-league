package io.github.binout.soccer.domain.season;

import java.util.Optional;
import java.util.stream.Stream;

public interface SeasonRepository {

    void add(Season season);

    Optional<Season> byName(String name);

    Stream<Season> all();
}
