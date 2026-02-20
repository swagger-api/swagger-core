package io.swagger.v3.oas.annotations.parameters;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.Content;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * The annotation may be used on a method parameter to define it as the Request Body of the operation, and/or to define
 * additional properties for such request body.
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#request-body-object">Request Body (OpenAPI specification)</a>
 * @see io.swagger.v3.oas.annotations.Parameter
 * @see Content
 **/
@Target({METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RequestBody {
    /**
     * A brief description of the request body.
     *
     * @return description of the request body
     **/
    String description() default "";

    /**
     * The content of the request body.
     *
     * @return array of content
     **/
    Content[] content() default {};

    /**
     * Determines if the request body is required in the request. Defaults to false.
     *
     * @return boolean
     **/
    boolean required() default false;

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    /**
     * A reference to a RequestBody defined in components RequestBodies.
     *
     * @since 2.0.3
     * @return the reference
     **/
    String ref() default "";

    /**
     * Set to true to resolve the request body schema from parameter type
     *
     * @since 2.2.15
     **/
    boolean useParameterTypeSchema() default false;

}
