package io.github.binout.soccer.interfaces.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Path("hello")
public class HelloResource {

    private static AtomicLong counter = new AtomicLong();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> hello() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "World");
        map.put("counter", Long.toString(counter.getAndIncrement()));
        map.put("timestamp", OffsetDateTime.now().toString());
        return map;
    }
}
