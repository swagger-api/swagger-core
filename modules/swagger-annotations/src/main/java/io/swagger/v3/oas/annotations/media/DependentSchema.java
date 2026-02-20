package io.swagger.v3.oas.annotations.media;

import io.swagger.v3.oas.annotations.OpenAPI31;

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
 * The annotation may be used to define dependent schemas for an Object Schema
 *
 * @see Schema
 *
 * @since 2.2.12 / OpenAPI 3.1
 **/
@Target({FIELD, METHOD, PARAMETER, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Repeatable(DependentSchemas.class)
@OpenAPI31
public @interface DependentSchema {
    /**
     * The name.
     *
     * @return the key of the dependent schema map item
     **/
    String name() default "";

    /**
     * The value (Schema) of the dependent schema map item.
     * Alternative to `array()`. Applied when the schema is not of type "array".
     * Use `array()` when schema is of type "array"
     *
     * @return the schema
     **/
    Schema schema() default @Schema();

    /**
     * The value (ArraySchema) of the dependent schema map item.
     * Alternative to `schema()`. Applied when the schema is of type "array".
     * Use `schema()` when schema is not of type "array"
     *
     * @return the value of the array schema
     */
    ArraySchema array() default @ArraySchema();

}
