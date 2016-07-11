package io.github.binout.soccer.infrastructure.ioc;

import net.codestory.http.injection.IocAdapter;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * LECTRA
 *
 * @author b.prioux
 */
public class WeldIocAdapter implements IocAdapter {

    private final WeldContainer weldContainer;

    public WeldIocAdapter(WeldContainer weldContainer) {
        this.weldContainer = weldContainer;
    }

    @Override
    public <T> T get(Class<T> aClass) {
        return weldContainer.select(aClass).get();
    }
}
