package io.github.binout.soccer;

import io.github.binout.soccer.infrastructure.ioc.WeldIocAdapter;
import io.github.binout.soccer.interfaces.rest.FriendlyMatchDateResource;
import io.github.binout.soccer.interfaces.rest.LeagueMatchDateResource;
import io.github.binout.soccer.interfaces.rest.PlayersResource;
import io.github.binout.soccer.interfaces.rest.SeasonsResource;
import net.codestory.http.Configuration;
import net.codestory.http.WebServer;
import net.codestory.http.injection.IocAdapter;
import net.codestory.http.routes.Routes;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class Server {

    public static void main(String[] args) {
        WeldContainer weld = new Weld().initialize();
        new WebServer().configure(new WebConfiguration(new WeldIocAdapter(weld))).start();
    }

    static class WebConfiguration implements Configuration {
        private final IocAdapter iocAdapter;

        public WebConfiguration(IocAdapter iocAdapter) {
            this.iocAdapter = iocAdapter;
        }

        @Override
        public void configure(Routes routes) {
            routes.setIocAdapter(iocAdapter);
            routes.add(SeasonsResource.class);
            routes.add(PlayersResource.class);
            routes.add(FriendlyMatchDateResource.class);
            routes.add(LeagueMatchDateResource.class);
        }
    }
}
