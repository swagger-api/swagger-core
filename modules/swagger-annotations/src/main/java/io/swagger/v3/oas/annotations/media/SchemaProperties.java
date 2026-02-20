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
 * Container for repeatable {@link SchemaProperty} annotation
 *
 * @see SchemaProperty
 */
@Target({FIELD, METHOD, PARAMETER, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SchemaProperties {
    /**
     * An array of SchemaProperty annotations
     *
     * @return the array of the SchemaProperty
     **/
    SchemaProperty[] value() default {};

}
