package io.github.binout.soccer;

import io.github.binout.soccer.infrastructure.ioc.WeldIocAdapter;
import io.github.binout.soccer.interfaces.rest.*;
import net.codestory.http.Configuration;
import net.codestory.http.WebServer;
import net.codestory.http.injection.IocAdapter;
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
            routes.add("rest", SeasonsResource.class);
            routes.add("rest", SeasonMatchesResource.class);
            routes.add("rest", PlayersResource.class);
            routes.add("rest", FriendlyMatchDateResource.class);
            routes.add("rest", LeagueMatchDateResource.class);
            routes.add(FakerResource.class);
        }
    }
}
