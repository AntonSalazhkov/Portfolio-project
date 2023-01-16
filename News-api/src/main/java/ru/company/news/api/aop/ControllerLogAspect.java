package ru.company.news.api.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Controller data logging.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Slf4j
@Aspect
@Component
public class ControllerLogAspect extends BaseAspect {

    /**
     * Determining where the aspect applies.
     */
    @Pointcut("execution(* ru.company.news.api.controller..*(..)) && !@annotation(ru.company.news.api.aop.ExcludeLog)")
    public void around() {
    }

    /**
     * Logging before method execution.
     *
     * @param joinPoint Getting arguments from a specific location.
     */
    @Before("around()")
    public void logControllersBefore(JoinPoint joinPoint) {
        final HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info(BEFORE_PATTERN_CONTROLLER,
                request.getMethod(),
                joinPoint.getSignature().toShortString(),
                request.getRequestURI(),
                getArgsWithNames(joinPoint));
    }

    /**
     * Logging after method execution.
     *
     * @param joinPoint Getting arguments from a specific location.
     * @param result    the result of executing the method at a specific location.
     */
    @AfterReturning(pointcut = "around()", returning = "result")
    public void logControllerAfter(JoinPoint joinPoint, Object result) {
        final HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info(AFTER_PATTERN_CONTROLLER,
                request.getMethod(),
                joinPoint.getSignature().toShortString(),
                request.getRequestURI(),
                getStringInstanceOf(Optional.ofNullable(result).orElse("not defined")),
                getArgsWithNames(joinPoint));
    }
}
