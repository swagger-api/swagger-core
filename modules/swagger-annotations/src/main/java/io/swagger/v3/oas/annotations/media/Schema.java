package io.swagger.v3.oas.annotations.media;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
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
 * The annotation may be used to define a Schema for a set of elements of the OpenAPI spec, and/or to define additional
 * properties for the schema. It is applicable e.g. to parameters, schema classes (aka "models"), properties of such
 * models, request and response content, header.
 *
 * <p>swagger-core resolver and swagger-jaxrs2 reader engine consider this annotation along with JAX-RS annotations,
 * element type and context as input to resolve the annotated element into an OpenAPI schema definition for such element.</p>
 * <p>The annotation may be used also to override partly (e.g. the name) or fully (e.g providing a completely different
 * representation) the schema of an element; for example if a specific class is provided as value of {@link Schema#implementation()},
 * it will override the element type</p>
 *
 * <p>The annotation {@link ArraySchema} shall be used for array elements; {@link ArraySchema} and {@link Schema} cannot
 * coexist</p>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#schema-object">Schema (OpenAPI specification)</a>
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.1.1/versions/3.1.1.md#schema-object">Schema (OpenAPI specification)</a>
 * @see ArraySchema
 **/
@Target({FIELD, METHOD, PARAMETER, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Schema {
    /**
     * Provides a java class as implementation for this schema.  When provided, additional information in the Schema annotation (except for type information) will augment the java class after introspection.
     *
     * @return a class that implements this schema
     **/
    Class<?> implementation() default Void.class;

    /**
     * Provides a java class to be used to disallow matching properties.
     *
     * @return a class with disallowed properties
     **/
    Class<?> not() default Void.class;

    /**
     * Provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If more than one match the derived schemas, a validation error will occur.
     *
     * @return the list of possible classes for a single match
     **/
    Class<?>[] oneOf() default {};

    /**
     * Provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If any match, the schema will be considered valid.
     *
     * @return the list of possible class matches
     **/
    Class<?>[] anyOf() default {};

    /**
     * Provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If all match, the schema will be considered valid
     *
     * @return the list of classes to match
     **/
    Class<?>[] allOf() default {};

    /**
     * The name of the schema or property.
     *
     * @return the name of the schema
     **/
    String name() default "";

    /**
     * A title to explain the purpose of the schema.
     *
     * @return the title of the schema
     **/
    String title() default "";

    /**
     * Constrains a value such that when divided by the multipleOf, the remainder must be an integer.  Ignored if the value is 0.
     *
     * @return the multiplier constraint of the schema
     **/
    double multipleOf() default 0;

    /**
     * Sets the maximum numeric value for a property.  Ignored if the value is an empty string.
     *
     * @return the maximum value for this schema
     **/
    String maximum() default "";

    /**
     * if true, makes the maximum value exclusive, or a less-than criteria.
     *
     * @return the exclusive maximum value for this schema
     **/
    boolean exclusiveMaximum() default false;

    /**
     * Sets the minimum numeric value for a property.  Ignored if the value is an empty string or not a number.
     *
     * @return the minimum value for this schema
     **/
    String minimum() default "";

    /**
     * If true, makes the minimum value exclusive, or a greater-than criteria.
     *
     * @return the exclusive minimum value for this schema
     **/
    boolean exclusiveMinimum() default false;

    /**
     * Sets the maximum length of a string value.  Ignored if the value is negative.
     *
     * @return the maximum length of this schema
     **/
    int maxLength() default Integer.MAX_VALUE;

    /**
     * Sets the minimum length of a string value.  Ignored if the value is negative.
     *
     * @return the minimum length of this schema
     **/
    int minLength() default 0;

    /**
     * A pattern that the value must satisfy. Ignored if the value is an empty string.
     *
     * @return the pattern of this schema
     **/
    String pattern() default "";

    /**
     * Constrains the number of arbitrary properties when additionalProperties is defined.  Ignored if value is 0.
     *
     * @return the maximum number of properties for this schema
     **/
    int maxProperties() default 0;

    /**
     * Constrains the number of arbitrary properties when additionalProperties is defined.  Ignored if value is 0.
     *
     * @return the minimum number of properties for this schema
     **/
    int minProperties() default 0;

    /**
     * Allows multiple properties in an object to be marked as required.
     *
     * @return the list of required schema properties
     **/
    String[] requiredProperties() default {};

    /**
     * Mandates that the annotated item is required or not.
     *
     * @deprecated since 2.2.5, replaced by {@link #requiredMode()}
     *
     * @return whether this schema is required
     **/
    @Deprecated
    boolean required() default false;

    /**
     * Allows to specify the required mode (RequiredMode.AUTO, REQUIRED, NOT_REQUIRED)
     *
     * RequiredMode.AUTO: will let the library decide based on its heuristics.
     * RequiredMode.REQUIRED: will force the item to be considered as required regardless of heuristics.
     * RequiredMode.NOT_REQUIRED: will force the item to be considered as not required regardless of heuristics.
     *
     * @since 2.2.5
     * @return the requiredMode for this schema (property)
     *
     */
    RequiredMode requiredMode() default RequiredMode.AUTO;

    /**
     * A description of the schema.
     *
     * @return the schema's description
     **/
    String description() default "";

    /**
     * Provides an optional override for the format.  If a consumer is unaware of the meaning of the format, they shall fall back to using the basic type without format.  For example, if \&quot;type: integer, format: int128\&quot; were used to designate a very large integer, most consumers will not understand how to handle it, and fall back to simply \&quot;type: integer\&quot;
     *
     * @return the schema's format
     **/
    String format() default "";

    /**
     * References a schema definition in an external OpenAPI document.
     *
     * @return a reference to this schema
     **/
    String ref() default "";

    /**
     * If true, designates a value as possibly null.
     *
     * @return whether or not this schema is nullable
     **/
    boolean nullable() default false;

    /**
     * Sets whether the value should only be read during a response but not read to during a request.
     *
     * @deprecated As of 2.0.0, replaced by {@link #accessMode()}
     *
     * @return whether or not this schema is read only
     *
     **/
    @Deprecated
    boolean readOnly() default false;

    /**
     * Sets whether a value should only be written to during a request but not returned during a response.
     *
     * @deprecated As of 2.0.0, replaced by {@link #accessMode()}
     *
     * @return whether or not this schema is write only
     **/
    @Deprecated
    boolean writeOnly() default false;

    /**
     * Allows to specify the access mode (AccessMode.READ_ONLY, WRITE_ONLY, READ_WRITE)
     *
     * AccessMode.READ_ONLY: value will not be written to during a request but may be returned during a response.
     * AccessMode.WRITE_ONLY: value will only be written to during a request but not returned during a response.
     * AccessMode.READ_WRITE: value will be written to during a request and returned during a response.
     *
     * @return the accessMode for this schema (property)
     *
     */
    AccessMode accessMode() default AccessMode.AUTO;

    /**
     * Provides an example of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.
     *
     * @return an example of this schema
     **/
    String example() default "";

    /**
     * Additional external documentation for this schema.
     *
     * @return additional schema documentation
     **/
    ExternalDocumentation externalDocs() default @ExternalDocumentation();

    /**
     * Specifies that a schema is deprecated and should be transitioned out of usage.
     *
     * @return whether or not this schema is deprecated
     **/
    boolean deprecated() default false;

    /**
     * Provides an override for the basic type of the schema.  Must be a valid type per the OpenAPI Specification.
     *
     * @return the type of this schema
     **/
    String type() default "";

    /**
     * Provides a list of allowable values.  This field map to the enum property in the OAS schema.
     *
     * @return a list of allowed schema values
     */
    String[] allowableValues() default {};

    /**
     * Provides a default value.
     *
     * @return the default value of this schema
     */
    String defaultValue() default "";

    /**
     * Provides a discriminator property value.
     *
     * @return the discriminator property
     */
    String discriminatorProperty() default "";

    /**
     * Provides discriminator mapping values.
     *
     * @return the discriminator mappings
     */
    DiscriminatorMapping[] discriminatorMapping() default {};

    /**
     * Allows schema to be marked as hidden.
     *
     * @return whether or not this schema is hidden
     */
    boolean hidden() default false;

    /**
     * Allows enums to be resolved as a reference to a scheme added to components section.
     *
     * @since 2.1.0
     * @return whether or not this must be resolved as a reference
     */
    boolean enumAsRef() default false;

    /**
     * An array of the sub types inheriting from this model.
     */
    Class<?>[] subTypes() default {};

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    /**
     * List of optional items positionally defines before normal items.
     * @return optional array of items
     */
    Class<?>[] prefixItems() default {};

    /**
     * List of schema types
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return array of types
     */
    @OpenAPI31
    String[] types() default {};

    /**
     * @since 2.2.12 / OpenAPI 3.1
     *
     * OAS 3.1 version of `exclusiveMaximum`, accepting a numeric value
     *
     * @return the exclusive maximum value for this schema
     **/
    @OpenAPI31
    int exclusiveMaximumValue() default 0;

    /**
     * Provides an exclusive minimum for a expressing exclusive range.
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return an exclusive minimum.
     */
    @OpenAPI31
    int exclusiveMinimumValue() default 0;

    /**
     * Specifies contains constrictions expressions.
     * @return contains expression.
     */
    @OpenAPI31
    Class<?> contains() default Void.class;

    /**
     * Provides the $id related to this schema.
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return the $id of schema
     */
    @OpenAPI31
    String $id() default "";

    /**
     * Provides Json Schema dialect where the schema is valid.
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return json schema dialect
     */
    @OpenAPI31
    String $schema() default "";

    /**
     * Provides the $anchor related to schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return $anchor schema
     */
    @OpenAPI31
    String $anchor() default "";

    /**
     * Provides the $vocabulary related to schema
     *
     * @since 2.2.14 / OpenAPI 3.1
     * @return $vocabulary schema
     */
    @OpenAPI31
    String $vocabulary() default "";

    /**
     * Provides the $dynamicAnchor related to schema
     *
     * @since 2.2.14 / OpenAPI 3.1
     * @return $dynamicAnchor schema
     */
    @OpenAPI31
    String $dynamicAnchor() default "";

    /**
     * Provides the $dynamicRef related to schema
     *
     * @since 2.2.32 / OpenAPI 3.1
     * @return $dynamicRef schema
     */
    @OpenAPI31
    String $dynamicRef() default "";

    /**
     * Provides the content encoding related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return content encoding
     */
    @OpenAPI31
    String contentEncoding() default "";

    /**
     * Provides the content media type related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return content media type
     */
    @OpenAPI31
    String contentMediaType() default "";

    /**
     * Provides the content schema related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return content schema
     */
    @OpenAPI31
    Class<?> contentSchema() default Void.class;

    /**
     * Provides property names related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return property names
     */
    @OpenAPI31
    Class<?> propertyNames() default Void.class;

    /**
     * Provides max contains related to this schema
     * @return max contains
     */
    @OpenAPI31
    int maxContains() default Integer.MAX_VALUE;

    /**
     * Provides min contains related to this schema
     * @return min contains
     */
    @OpenAPI31
    int minContains() default 0;

    /**
     * Provides a list of additional items
     * @return additional items
     */
    Class<?> additionalItems() default Void.class;

    /**
     * Provides a list of unevaluated items
     * @return unevaluated items
     */
    Class<?> unevaluatedItems() default Void.class;

    /**
     * Provides the if sub schema related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return if sub schema
     */
    @OpenAPI31
    Class<?> _if() default Void.class;

    /**
     * Provides the else sub schema related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return else sub schema
     */
    @OpenAPI31
    Class<?> _else() default Void.class;

    /**
     * Provides the then sub schema related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return then sub schema
     */
    @OpenAPI31
    Class<?> then() default Void.class;

    /**
     * Provides $comment related to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return $comment related to schema
     */
    @OpenAPI31
    String $comment() default "";

    /**
     * Provides a list of examples related to this schema
     * @return list of examples
     */
    Class<?>[] exampleClasses() default {};

    /**
     * Allows to specify the additionalProperties value
     *
     * AdditionalPropertiesValue.TRUE: set to TRUE
     * AdditionalPropertiesValue.FALSE: set to FALSE
     * AdditionalPropertiesValue.USE_ADDITIONAL_PROPERTIES_ANNOTATION: resolve from @Content.additionalPropertiesSchema
     * or @Schema.additionalPropertiesSchema
     *
     * @since 2.2.0
     * @return the accessMode for this schema (property)
     *
     */
    AdditionalPropertiesValue additionalProperties() default AdditionalPropertiesValue.USE_ADDITIONAL_PROPERTIES_ANNOTATION;

    enum AccessMode {
        AUTO,
        READ_ONLY,
        WRITE_ONLY,
        READ_WRITE;
    }

    enum AdditionalPropertiesValue {
        TRUE,
        FALSE,
        USE_ADDITIONAL_PROPERTIES_ANNOTATION;
    }

    enum RequiredMode {
        AUTO,
        REQUIRED,
        NOT_REQUIRED;
    }

    enum SchemaResolution {
        AUTO,
        DEFAULT,
        INLINE,
        ALL_OF,
        ALL_OF_REF;
    }

    /**
     * Allows to specify the dependentRequired value
     **
     * @since 2.2.12 / OpenAPI 3.1
     * @return the list of DependentRequire annotations
     *
     */
    @OpenAPI31
    DependentRequired[] dependentRequiredMap() default {};

    /**
     * Allows to specify the dependentSchemas value providing a Class to be resolved into a Schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return the list of dependentSchemas annotations
     *
     */
    @OpenAPI31
    StringToClassMapItem[] dependentSchemas() default {};

    /**
     * Provides pattern properties to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return pattern properties
     */
    @OpenAPI31
    StringToClassMapItem[] patternProperties() default {};

    /**
     * Provides properties related to this schema
     *
     * @return schema properties
     */
    StringToClassMapItem[] properties() default {};

    /**
     * Provides unevaluated properties to this schema
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return unevaluated properties
     */
    @OpenAPI31
    Class<?> unevaluatedProperties() default Void.class;
    Class<?> additionalPropertiesSchema() default Void.class;

    /**
     * Provides an array of examples of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.
     *
     * @return an array of examples of this schema
     **/
    @OpenAPI31
    String[] examples() default {};

    /**
     * Provides value restricted to this schema.
     *
     * @since 2.2.12 / OpenAPI 3.1
     * @return const value
     */
    @OpenAPI31
    String _const() default "";

    /**
     * Allows to specify the schema resolution mode for object schemas
     *
     * SchemaResolution.DEFAULT: bundled into components/schemas, $ref with no siblings
     * SchemaResolution.INLINE: inline schema, no $ref
     * SchemaResolution.ALL_OF: bundled into components/schemas, $ref and any context annotation resolution into allOf
     * SchemaResolution.ALL_OF_REF: bundled into components/schemas, $ref into allOf, context annotation resolution into root
     *
     * @return the schema resolution mode for this schema
     *
     */
    SchemaResolution schemaResolution() default SchemaResolution.AUTO;
}
