package io.swagger.v3.oas.annotations.callbacks;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * The annotation may be used at method level to add one ore more callbacks to the operation definition.
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#callback-object">Callback (OpenAPI specification)</a>
 **/
@Target({FIELD, METHOD, PARAMETER, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Callbacks.class)
@Inherited
public @interface Callback {
    /**
     * The friendly name used to refer to this callback
     *
     * @return the name of the callback
     **/
    String name() default "";

    /**
     * An absolute URL which defines the destination which will be called with the supplied operation definition.
     *
     * @return the callback URL
     */
    String callbackUrlExpression() default "";

    /**
     * The array of operations that will be called out-of band
     *
     * @return the callback operations
     **/
    Operation[] operation() default {};

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    /**
     * A reference to a Callback defined in components Callbacks.
     *
     * @since 2.0.3
     * @return the reference
     **/
    String ref() default "";
}
