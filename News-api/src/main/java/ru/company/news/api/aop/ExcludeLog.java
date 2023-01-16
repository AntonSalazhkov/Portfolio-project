package ru.company.news.api.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation of exclusion of data logging of method operation.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcludeLog {
}
