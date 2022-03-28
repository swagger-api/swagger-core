package io.swagger.v3.oas.annotations.security;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Container for repeatable {@link SecurityRequirement} annotation
 *
 * @see SecurityRequirement
 */
@Target({METHOD, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SecurityRequirements {
    /**
     * An array of SecurityRequirement annotations
     *
     * @return the array of the SecurityRequirement
     **/
    SecurityRequirement[] value() default {};

}
