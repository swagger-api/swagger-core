package io.swagger.v3.oas.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Container for repeatable {@link Parameter} annotation
 *
 * @see Parameter
 */
@Target({METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Parameters {
    /**
     * An array of Parameters Objects for the operation
     *
     * @return the parameters
     */
    Parameter[] value() default {};
}
