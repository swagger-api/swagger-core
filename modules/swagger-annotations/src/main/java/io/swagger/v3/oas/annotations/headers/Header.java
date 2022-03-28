package io.swagger.v3.oas.annotations.headers;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation may be used to add one or more headers to the definition of a response or as attribute of content
 * encoding by defining it as field {@link io.swagger.v3.oas.annotations.responses.ApiResponse#headers()} or {@link io.swagger.v3.oas.annotations.media.Content#encoding()}.
 * <p>Please note that request headers are defined as Header {@link io.swagger.v3.oas.annotations.Parameter}.</p>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#headerObject">Header (OpenAPI specification)</a>
 * @see io.swagger.v3.oas.annotations.responses.ApiResponse
 * @see io.swagger.v3.oas.annotations.Parameter
 * @see io.swagger.v3.oas.annotations.media.Encoding
 **/
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Header {
    /**
     * Required: The name of the header. The name is only used as the key to store this header in a map.
     *
     * @return the header's name
     **/
    String name();

    /**
     * Additional description data to provide on the purpose of the header
     *
     * @return the header's description
     **/
    String description() default "";

    /**
     * The schema defining the type used for the header. Ignored if the properties content or array are specified.
     *
     * @return the schema of the header
     **/
    Schema schema() default @Schema();

    /**
     * Determines whether this header is mandatory. The property may be included and its default value is false.
     *
     * @return whether or not the header is required
     **/
    boolean required() default false;

    /**
     * Specifies that a header is deprecated and should be transitioned out of usage.
     *
     * @return whether or not the header is deprecated
     **/
    boolean deprecated() default false;

    /**
     * A reference to a header defined in components headers.
     *
     * @since 2.0.3
     * @return the reference
     **/
    String ref() default "";

}
