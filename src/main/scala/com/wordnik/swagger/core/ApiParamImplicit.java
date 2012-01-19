package com.wordnik.swagger.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ayush
 * @since 12/20/11 3:35 PM
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParamImplicit {
    String name() default "";
    String value() default "";
    String defaultValue() default "";
    String allowableValues() default "";
    boolean required() default false;
    String access() default "";
    String internalDescription() default "";
    boolean allowMultiple() default false;
    String dataType() default "";
    String paramType() default "";
}
