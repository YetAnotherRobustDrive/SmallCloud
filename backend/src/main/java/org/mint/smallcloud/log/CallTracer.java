package org.mint.smallcloud.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("test")
public class CallTracer {
    private static final Logger log = LoggerFactory.getLogger("Debug Tracer");

    @Value("${logging.call-tracer:false}")
    private boolean activate;

    @Before("execution(public * org.mint.smallcloud..* (..))")
    public void beforeLog(JoinPoint joinpoint) {
        if (!activate) return;
        String className = joinpoint.getTarget().getClass().getSimpleName();
        String methodName = joinpoint.getSignature().getName();
        log.info("{}: called {}", className, methodName);
    }
}
