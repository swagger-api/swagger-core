/**
 * Copyright 2017 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.v3.oas.annotations.media;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.AccessMode;

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
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#schemaObject">Schema (OpenAPI specification)</a>
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
     * @return whether or not this schema is required
     **/
    boolean required() default false;

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
     * Allows to specify the access mode (AccessMode.READ_ONLY, READ_WRITE)
     *
     * AccessMode.READ_ONLY: value will only be written to during a request but not returned during a response.
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
     * An array of the sub types inheriting from this model.
     */
    Class<?>[] subTypes() default {};

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    enum AccessMode {
        AUTO,
        READ_ONLY,
        WRITE_ONLY,
        READ_WRITE;
    }
}
