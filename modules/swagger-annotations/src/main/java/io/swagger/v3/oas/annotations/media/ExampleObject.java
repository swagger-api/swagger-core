package io.swagger.v3.oas.annotations.media;

import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation may be used to add one or more examples to the definition of a parameter, request or response content,
 * by defining it as field {@link io.swagger.v3.oas.annotations.Parameter#examples()} or {@link Content#examples()}
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#example-object">Example (OpenAPI specification)</a>
 **/
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExampleObject {
    /**
     * A unique name to identify this particular example
     *
     * @return the name of the example
     **/
    String name() default "";

    /**
     * A brief summary of the purpose or context of the example
     *
     * @return a summary of the example
     **/
    String summary() default "";

    /**
     * A string representation of the example.  This is mutually exclusive with the externalValue property, and ignored if the externalValue property is specified.  If the media type associated with the example allows parsing into an object, it may be converted from a string
     *
     * @return the value of the example
     **/
    String value() default "";

    /**
     * A URL to point to an external document to be used as an example.  This is mutually exclusive with the value property.
     *
     * @return an external URL of the example
     **/
    String externalValue() default "";

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    /**
     * A reference to a example defined in components examples.
     *
     * @since 2.0.3
     * @return the reference
     **/
    String ref() default "";

    /**
     * A description of the purpose or context of the example
     *
     * @since 2.1.0
     * @return a description of the example
     **/
    String description() default "";

}
