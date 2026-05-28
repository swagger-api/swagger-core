package io.swagger.v3.oas.annotations.media;

import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation may be used to define the content/media type  of a parameter, request or response, by defining it as
 * field {@link io.swagger.v3.oas.annotations.Parameter#content()}, {@link io.swagger.v3.oas.annotations.parameters.RequestBody#content()} or {@link io.swagger.v3.oas.annotations.responses.ApiResponse#content()}.
 * <p>If {@link Content#schema()} is defined, swagger-jaxrs2 reader engine will consider it along with
 * JAX-RS annotations, element type and context as input to resolve the annotated element into an OpenAPI schema
 * definition for such element.</p>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#example-object">Example (OpenAPI specification)</a>
 * @see Schema
 * @see io.swagger.v3.oas.annotations.Parameter
 * @see io.swagger.v3.oas.annotations.responses.ApiResponse
 * @see io.swagger.v3.oas.annotations.parameters.RequestBody
 **/
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Content {
    /**
     * The media type that this object applies to.
     *
     * @return the media type value
     **/
    String mediaType() default "";

    /**
     * An array of examples used to show the use of the associated schema.
     *
     * @return the list of examples
     **/
    ExampleObject[] examples() default {};

    /**
     * The schema defining the type used for the content.
     *
     * @return the schema of this media type
     **/
    Schema schema() default @Schema();

    /**
     * The schema properties defined for schema provided in @Schema
     *
     * @since 2.2.0
     * @return the schema properties
     */
    SchemaProperty[] schemaProperties() default {};

    /**
     * The additionalProperties schema defined for schema provided in @Schema
     * If the additionalProperties schema is an array, use additionalPropertiesArraySchema
     *
     * @since 2.2.0
     * @return the additionalProperties schema
     */
    Schema additionalPropertiesSchema() default @Schema();

    /**
     * The additionalProperties array schema defined for schema provided in @Schema
     * If the additionalProperties schema is not an array, use additionalPropertiesSchema
     *
     * @since 2.2.16
     * @return the additionalProperties array schema
     */
    ArraySchema additionalPropertiesArraySchema() default @ArraySchema();

    /**
     * The schema of the array that defines the type used for the content.
     *
     * @return the schema of the array
     */
    ArraySchema array() default @ArraySchema();

    /**
     * An array of encodings
     * The key, being the property name, MUST exist in the schema as a property.
     *
     * @return the array of encodings
     */
    Encoding[] encoding() default {};

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    /**
     * Subschemas to be applied for a given condition.
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return list of dependent schemas.
     */
    @OpenAPI31
    DependentSchema[] dependentSchemas() default {};

    /**
     * Provides the content schema related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return content schema
     */
    @OpenAPI31
    Schema contentSchema() default @Schema();

    /**
     * Provides property names related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return proeprty names
     */
    @OpenAPI31
    Schema propertyNames() default @Schema();

    /**
     * Provides the if sub schema related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return if schema
     */
    @OpenAPI31
    Schema _if() default @Schema();

    /**
     * Provides the then sub schema related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return then schema
     */
    @OpenAPI31
    Schema _then() default @Schema();

    /**
     * Provides the else sub schema related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return else schema
     */
    @OpenAPI31
    Schema _else() default @Schema();

    /**
     * Set schemas to validate according a given condition.
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return not schema to be validated
     **/
    Schema not() default @Schema();

    /**
     * Provides the oneOf sub schemas related to this schema.
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return oneOf sub schemas
     **/
    Schema[] oneOf() default {};

    /**
     * Provides the anyOf sub schemas related to this schema.
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return anyOf sub schemas
     **/
    Schema[] anyOf() default {};

    /**
     * Provides the allOf sub schemas related to this schema..
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return allOf sub schemas
     **/
    Schema[] allOf() default {};

}
