package io.github.binout.soccer.infrastructure.persistence.mongo;

import io.github.binout.soccer.infrastructure.persistence.TransactedScope;
import io.github.binout.soccer.infrastructure.persistence.TransactedScopeEnabled;
import org.mongolink.MongoSession;
import org.tomitribe.microscoped.core.ScopeContext;

import javax.enterprise.inject.spi.CDI;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.UUID;

@Interceptor
@TransactedScopeEnabled
public class MongoTransactedScopeInterceptor {

    @AroundInvoke
    public Object invoke(InvocationContext invocation) throws Exception {
        final ScopeContext<String> context = (ScopeContext<String>) CDI.current().getBeanManager().getContext(TransactedScope.class);
        final String previous = context.enter(UUID.randomUUID().toString());
        MongoSession currentSession = CDI.current().select(MongoSession.class).get();
        try {
            currentSession.start();
            return invocation.proceed();
        } finally {
            currentSession.stop();
            context.exit(previous);
        }
    }
}
