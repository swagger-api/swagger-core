package io.swagger.v3.oas.annotations.tags;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Container for repeatable {@link Tag} annotation
 *
 * @see Tag
 */
@Target({METHOD, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Tags {
    /**
     * An array of Tag annotation objects which hold metadata for the API
     *
     * @return array of Tags
     */
    Tag[] value() default {};

}
