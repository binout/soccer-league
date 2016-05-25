package io.github.binout.soccer.domain.season;

import java.util.Optional;

public interface SeasonRepository {

    void add(Season season);

    Optional<Season> byName(String name);
}
