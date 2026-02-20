package io.swagger.v3.oas.annotations.callbacks;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Container for repeatable {@link Callback} annotation
 *
 * @see Callback
 */
@Target({METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Callbacks {
    /**
     * An array of Callback annotations which are a map of possible out-of band callbacks related to the parent operation
     *
     * @return the array of the callbacks
     **/
    Callback[] value() default {};

}
