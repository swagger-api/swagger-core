package io.swagger.v3.oas.annotations.media;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.headers.Header;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation may be used to add encoding details to the definition of a parameter, request or response content,
 * by defining it as field {@link Content#encoding()}
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#encodingObject">Encoding (OpenAPI specification)</a>
 * @see Content
 **/
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Encoding {

    /**
     * The name of this encoding object instance.
     * This property is a key in encoding map of MediaType object and
     * MUST exist in a schema as a property.
     *
     * @return name of the encoding
     **/
    String name() default "";

    /**
     * The Content-Type for encoding a specific property.
     *
     * @return content type of the encoding
     **/
    String contentType() default "";

    /**
     * Describes how a specific property value will be serialized depending on its type
     *
     * @return style of the encoding
     **/
    String style() default "";

    /**
     * When this is true, property values of type array or object generate separate parameters for each value of the array,
     * or key-value-pair of the map.
     *
     * @return boolean
     **/
    boolean explode() default false;

    /**
     * Determines whether the parameter value SHOULD allow reserved characters,
     * as defined by RFC3986 to be included without percent-encoding.
     *
     * @return boolean
     **/
    boolean allowReserved() default false;

    /**
     * An array of header objects
     *
     * @return array of headers
     */
    Header[] headers() default {};

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

}
