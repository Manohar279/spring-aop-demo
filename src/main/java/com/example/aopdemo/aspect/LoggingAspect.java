package com.example.aopdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    // Pointcut: every public method of every class in the service package.
    @Pointcut("execution(public * com.example.aopdemo.service.*.*(..))")
    public void serviceMethods() {
    }

    // Runs before the method.
    @Before("serviceMethods()")
    public void logBefore(JoinPoint jp) {
        System.out.println("@Before         -> " + jp.getSignature().getName()
                + " args=" + Arrays.toString(jp.getArgs()));
    }

    // Runs after the method returns normally; can read the return value.
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint jp, Object result) {
        System.out.println("@AfterReturning -> " + jp.getSignature().getName()
                + " returned=" + result);
    }

    // Runs only if the method throws an exception.
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint jp, Exception ex) {
        System.out.println("@AfterThrowing  -> " + jp.getSignature().getName()
                + " threw " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
    }

    // Runs after the method no matter how it ended (like a finally block).
    @After("serviceMethods()")
    public void logAfter(JoinPoint jp) {
        System.out.println("@After          -> " + jp.getSignature().getName() + " finished");
    }

    // Wraps the method: code before and after proceed(). Used here to time it.
    @Around("serviceMethods()")
    public Object measureTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        System.out.println("@Around (start) -> " + pjp.getSignature().getName());
        try {
            return pjp.proceed();
        } finally {
            long micros = (System.nanoTime() - start) / 1_000;
            System.out.println("@Around (end)   -> " + pjp.getSignature().getName()
                    + " took " + micros + " us");
        }
    }
}
