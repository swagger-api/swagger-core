package io.swagger.v3.oas.annotations.media;

import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

/**
 * The annotation may be used in OpenAPI 3.1 schemas / JSON Schema.
 *
 * @see <a target="_new" href="https://tools.ietf.org/html/draft-bhutton-json-schema-00#section-10.3.2.2">JSON Schema section 10.3.2.2</a>
 * @see Schema
 *
 * @since 2.1.8
 **/
@Target({FIELD, METHOD, PARAMETER, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Repeatable(PatternProperties.class)
public @interface PatternProperty {
    /**
     * The regex.
     *
     * @return the regex
     **/
    String regex() default "";

    /**
     * The schema to validate against for properties matching the regex.
     *
     * @return the schema
     **/
    Schema schema() default @Schema();

    /**
     * The schema of the array to validate against for properties matching the regex.
     *
     * @return the schema of the array
     */
    ArraySchema array() default @ArraySchema();

}
