package io.swagger.v3.oas.annotations.media;

import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * The annotation may be used to define a schema of type "array" for a set of elements of the OpenAPI spec, and/or to define additional
 * properties for the schema. It is applicable e.g. to parameters, schema classes (aka "models"), properties of such
 * models, request and response content, header.
 *
 * <p>swagger-core resolver and swagger-jaxrs2 reader engine consider this annotation along with JAX-RS annotations,
 * element type and context as input to resolve the annotated element into an OpenAPI schema definition for such element.</p>
 *
 * <p>The annotation {@link Schema} shall be used for non array elements; {@link ArraySchema} and {@link Schema} cannot
 * coexist</p>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#schema-object">Schema (OpenAPI specification)</a>
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.1.1/versions/3.1.1.md#schema-object">Schema (OpenAPI specification)</a>
 * @see Schema
 **/
@Target({FIELD, METHOD, PARAMETER, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ArraySchema {

    /**
     * The schemas of the items in the array
     *
     * @since 2.2.12
     *
     * @deprecated since 2.2.21, use {@link #schema()} instead. Marked for removal in future versions.
     * @return items
     */
    @Deprecated
    Schema items() default @Schema;

    /**
     * The schema of the items in the array
     *
     * @return schema
     */
    Schema schema() default @Schema;

    /**
     * Allows to define the properties to be resolved into properties of the schema of type `array` (not the ones of the
     * `items` of such schema which are defined in {@link #schema() schema}.
     *
     * @return arraySchema
     *
     * @since 2.0.2
     */
    Schema arraySchema() default @Schema;

    /**
     * sets the maximum number of items in an array.  Ignored if value is Integer.MIN_VALUE.
     *
     * @return integer representing maximum number of items in array
     **/
    int maxItems() default Integer.MIN_VALUE;

    /**
     * sets the minimum number of items in an array.  Ignored if value is Integer.MAX_VALUE.
     *
     * @return integer representing minimum number of items in array
     **/
    int minItems() default Integer.MAX_VALUE;

    /**
     * determines whether an array of items will be unique
     *
     * @return boolean - whether items in an array are unique or repeating
     **/
    boolean uniqueItems() default false;

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    /**
     * Specifies contains constrictions expressions.
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return contains expression.
     */
    @OpenAPI31
    Schema contains() default @Schema;

    /**
     * Provides max contains related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return max contains
     */
    @OpenAPI31
    int maxContains() default 0;

    /**
     * Provides min contains related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return min contains
     */
    @OpenAPI31
    int minContains() default 0;

    /**
     * Provides unevaluted items to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return unevaluated items
     */
    @OpenAPI31
    Schema unevaluatedItems() default @Schema;

    /**
     * Provides prefix items to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return prefixItems
     */
    @OpenAPI31
    Schema[] prefixItems() default {};
}
