package com.wordnik.swagger.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ayush
 * @since 6/23/11 12:27 PM
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParam {
    String name() default "";
    String value() default "";
    String defaultValue() default "";
    String allowableValues() default "";
    boolean required() default false;
    String access() default "";
    String internalDescription() default "";
    boolean allowMultiple() default false;
}

