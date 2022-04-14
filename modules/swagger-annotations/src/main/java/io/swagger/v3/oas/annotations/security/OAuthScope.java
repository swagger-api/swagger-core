package io.swagger.v3.oas.annotations.security;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an OAuth scope.
 **/
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OAuthScope {
    /**
     * Name of the scope.
     *
     * @return String name
     */
    String name() default "";

    /**
     * Short description of the scope.
     *
     * @return String description
     */
    String description() default "";
}
