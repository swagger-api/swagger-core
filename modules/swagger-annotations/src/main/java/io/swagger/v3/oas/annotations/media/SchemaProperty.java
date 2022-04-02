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
 * The annotation may be used to define properties for an Object Schema
 *
 * @see Schema
 *
 * @since 2.1.8
 **/
@Target({FIELD, METHOD, PARAMETER, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Repeatable(SchemaProperties.class)
public @interface SchemaProperty {
    /**
     * The name.
     *
     * @return the name
     **/
    String name() default "";

    /**
     * The schema of the property.
     *
     * @return the schema
     **/
    Schema schema() default @Schema();

    /**
     * The schema of the array.
     *
     * @return the schema of the array
     */
    ArraySchema array() default @ArraySchema();

}
