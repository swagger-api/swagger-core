package io.swagger.v3.oas.annotations.media;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Container for repeatable {@link DependentRequired} annotation
 *
 * @see DependentRequired
 */
@Target({FIELD, METHOD, PARAMETER, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DependentRequireds {
    /**
     * An array of DependentRequired annotations
     *
     * @return the array of the DependentRequired
     **/
    DependentRequired[] value() default {};

}
