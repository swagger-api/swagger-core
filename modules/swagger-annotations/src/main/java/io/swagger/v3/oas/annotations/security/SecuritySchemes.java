package io.swagger.v3.oas.annotations.security;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Container for repeatable {@link SecurityScheme} annotation
 *
 * @see SecurityScheme
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SecuritySchemes {
    /**
     * An array of SecurityScheme annotations
     *
     * @return the array of the SecurityScheme
     **/
    SecurityScheme[] value() default {};

}
