package io.github.binout.soccer.interfaces.rest;

import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Prefix("hello")
public class HelloResource {

    private static AtomicLong counter = new AtomicLong();

    @Get
    public Map<String, String> hello() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "World");
        map.put("counter", Long.toString(counter.getAndIncrement()));
        map.put("timestamp", OffsetDateTime.now().toString());
        return map;
    }
}
