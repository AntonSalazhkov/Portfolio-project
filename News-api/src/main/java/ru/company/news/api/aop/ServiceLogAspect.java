package ru.company.news.api.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service data logging.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Slf4j
@Aspect
@Component
public class ServiceLogAspect extends BaseAspect {

    /**
     * Determining where the aspect applies.
     */
    @Pointcut("execution(* ru.company.news.api.service..*(..)) && !@annotation(ru.company.news.api.aop.ExcludeLog)")
    public void around() {
    }

    /**
     * Logging before method execution.
     *
     * @param joinPoint Getting arguments from a specific location.
     */
    @Before("around()")
    public void logServicesBefore(JoinPoint joinPoint) {

        log.info(BEFORE_SERVICE_PATTERN,
                joinPoint.getSignature().toShortString(),
                getArgsWithNames(joinPoint));
    }

    /**
     * Logging after method execution.
     *
     * @param joinPoint Getting arguments from a specific location.
     * @param result    the result of executing the method at a specific location.
     */
    @AfterReturning(pointcut = "around()", returning = "result")
    public void logServicesAfter(JoinPoint joinPoint, Object result) {

        log.info(AFTER_SERVICE_PATTERN,
                joinPoint.getSignature().toShortString(),
                getStringInstanceOf(Optional.ofNullable(result).orElse("not defined")),
                getArgsWithNames(joinPoint));
    }
}
