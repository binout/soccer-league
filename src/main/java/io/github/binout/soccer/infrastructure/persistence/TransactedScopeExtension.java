package io.github.binout.soccer.infrastructure.persistence;

import org.tomitribe.microscoped.core.ScopeContext;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
/**
 * LECTRA
 *
 * @author b.prioux
 */
public class TransactedScopeExtension implements Extension {

    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
        bbd.addScope(TransactedScope.class, true, false);
        bbd.addInterceptorBinding(TransactedScope.class);
    }

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {
        abd.addContext(new ScopeContext<>(TransactedScope.class));
    }
}
