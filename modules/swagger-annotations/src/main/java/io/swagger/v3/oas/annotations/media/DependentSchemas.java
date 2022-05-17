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
 * Container for repeatable {@link DependentSchema} annotation
 *
 * @see DependentSchema
 */
@Target({FIELD, METHOD, PARAMETER, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DependentSchemas {
    /**
     * An array of DependentSchema annotations
     *
     * @return the array of the DependentSchema
     **/
    DependentSchema[] value() default {};

}
