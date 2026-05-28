package io.swagger.v3.oas.annotations.responses;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;

import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * The annotation may be used at method level or as field of {@link io.swagger.v3.oas.annotations.Operation} to define one or more responses of the
 * Operation.
 *
 * <p>swagger-jaxrs2 reader engine considers this annotation along with method return type and context as input to
 * resolve the OpenAPI Operation responses.</p>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#response-object">Response (OpenAPI specification)</a>
 * @see io.swagger.v3.oas.annotations.Operation
 **/
@Target({METHOD, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Repeatable(ApiResponses.class)
public @interface ApiResponse {
    /**
     * A short description of the response.
     *
     * @return description of the response
     **/
    String description() default "";

    /**
     * The HTTP response code, or 'default', for the supplied response. May only have 1 default entry.
     *
     * @return response code
     **/
    String responseCode() default "default";

    /**
     * An array of response headers. Allows additional information to be included with response.
     *
     * @return array of headers
     **/
    Header[] headers() default {};

    /**
     * An array of operation links that can be followed from the response.
     *
     * @return array of links
     **/
    Link[] links() default {};

    /**
     * An array containing descriptions of potential response payloads, for different media types.
     *
     * @return array of content
     **/
    Content[] content() default {};

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    /**
     * A reference to a response defined in components responses.
     *
     * @since 2.0.3
     * @return the reference
     **/
    String ref() default "";

    /**
     * Set to true to resolve the response schema from method return type
     *
     * @since 2.2.0
     **/
    boolean useReturnTypeSchema() default false;

}
