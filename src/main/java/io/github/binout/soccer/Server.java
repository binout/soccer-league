package io.github.binout.soccer;

import io.github.binout.soccer.infrastructure.ioc.WeldIocAdapter;
import io.github.binout.soccer.interfaces.rest.*;
import net.codestory.http.Configuration;
import net.codestory.http.Context;
import net.codestory.http.WebServer;
import net.codestory.http.constants.Methods;
import net.codestory.http.filters.Filter;
import net.codestory.http.filters.PayloadSupplier;
import net.codestory.http.injection.IocAdapter;
import net.codestory.http.payload.Payload;
import net.codestory.http.routes.Routes;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class Server {

    private final WebServer webServer;
    private final WeldContainer weld;

    Server() {
        weld = new Weld().initialize();
        webServer = new WebServer().configure(new WebConfiguration(new WeldIocAdapter(weld)));
    }

    void start() {
        webServer.start();
    }

    int startOnRandomPort() {
        webServer.startOnRandomPort();
        return webServer.port();
    }

    void stop() {
        webServer.stop();
        weld.shutdown();
    }

    public static void main(String[] args) {
        new Server().start();
    }

    static class WebConfiguration implements Configuration {
        private final IocAdapter iocAdapter;

        public WebConfiguration(IocAdapter iocAdapter) {
            this.iocAdapter = iocAdapter;
        }

        @Override
        public void configure(Routes routes) {
            routes.setIocAdapter(iocAdapter);
            routes.filter(new CorsFilter());
            routes.add("rest", SeasonsResource.class);
            routes.add("rest", SeasonMatchesResource.class);
            routes.add("rest", PlayersResource.class);
            routes.add("rest", FriendlyMatchDateResource.class);
            routes.add("rest", LeagueMatchDateResource.class);
            routes.add(FakerResource.class);
        }
    }

    static class CorsFilter implements Filter {
        @Override
        public Payload apply(String uri, Context context, PayloadSupplier nextFilter) throws Exception {
            Payload payload;
            if (context.method().equals(Methods.OPTIONS)) {
                payload = Payload.ok();
            } else {
                payload = nextFilter.get();
            }
            return payload
                    .withHeader("Access-Control-Allow-Origin", "*")
                    .withHeader("Access-Control-Allow-Credentials", "true")
                    .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .withHeader("Access-Control-Max-Age", "3600");
        }
    }
}
