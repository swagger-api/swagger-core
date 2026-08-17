package io.swagger.v3.oas.annotations;

import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * The annotation may be used on a method parameter to define it as a parameter for the operation, and/or to define
 * additional properties for the Parameter. It can also be used independently in {@link Operation#parameters()} or at
 * method level to add a parameter to the operation, even if not bound to any method parameter.
 *
 * <p>swagger-jaxrs2 reader engine considers this annotation along with JAX-RS annotations, parameter type and context
 * as input to resolve a method parameter into an OpenAPI Operation parameter.</p>
 *
 * <p>For method parameters bound to the request body, see {@link io.swagger.v3.oas.annotations.parameters.RequestBody}</p>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#parameter-object">Parameter (OpenAPI specification)</a>
 * @see io.swagger.v3.oas.annotations.parameters.RequestBody
 * @see Operation
 * @see Schema
 **/
@Target({PARAMETER, METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Parameters.class)
@Inherited
public @interface Parameter {
    /**
     * The name of the parameter.
     *
     * @return the parameter's name
     **/
    String name() default "";

    /**
     * The location of the parameter.  Possible values are "query", "header", "path" or "cookie".  Ignored when empty string.
     *
     * @return the parameter's location
     **/
    ParameterIn in() default ParameterIn.DEFAULT;

    /**
     * Additional description data to provide on the purpose of the parameter
     *
     * @return the parameter's description
     **/
    String description() default "";

    /**
     * Determines whether this parameter is mandatory. If the parameter location is "path", this property is required and its value must be true. Otherwise, the property may be included and its default value is false.
     *
     * @return whether or not the parameter is required
     **/
    boolean required() default false;

    /**
     * Specifies that a parameter is deprecated and should be transitioned out of usage.
     *
     * @return whether or not the parameter is deprecated
     **/
    boolean deprecated() default false;

    /**
     * When true, allows sending an empty value.  If false, the parameter will be considered \&quot;null\&quot; if no value is present.  This may create validation errors when the parameter is required.
     *
     * @return whether or not the parameter allows empty values
     **/
    boolean allowEmptyValue() default false;

    /**
     * Describes how the parameter value will be serialized depending on the type of the parameter value. Default values (based on value of in): for query - form; for path - simple; for header - simple; for cookie - form.  Ignored if the properties content or array are specified.
     *
     * @return the style of the parameter
     **/
    ParameterStyle style() default ParameterStyle.DEFAULT;

    /**
     * When this is true, parameter values of type array or object generate separate parameters for each value of the array or key-value pair of the map. For other types of parameters this property has no effect. When style is form, the default value is true. For all other styles, the default value is false.  Ignored if the properties content or array are specified.
     *
     * @return whether or not to expand individual array members
     **/
    Explode explode() default Explode.DEFAULT;

    /**
     * Determines whether the parameter value should allow reserved characters, as defined by RFC3986. This property only applies to parameters with an in value of query. The default value is false.  Ignored if the properties content or array are specified.
     *
     * @return whether or not the parameter allows reserved characters
     **/
    boolean allowReserved() default false;

    /**
     * The schema defining the type used for the parameter.  Ignored if the properties content or array are specified.
     *
     * @return the schema of the parameter
     **/
    Schema schema() default @Schema();

    /**
     * The schema of the array that defines this parameter.  Ignored if the property content is specified.
     *
     * @return the schema of the array
     */
    ArraySchema array() default @ArraySchema();

    /**
     * The representation of this parameter, for different media types.
     *
     * @return the content of the parameter
     **/
    Content[] content() default {};

    /**
     * Allows this parameter to be marked as hidden
     *
     * @return whether or not this parameter is hidden
     */
    boolean hidden() default false;

    /**
     * An array of examples  of the schema used to show the use of the associated schema.
     *
     * @return array of examples of the parameter
     **/
    ExampleObject[] examples() default {};

    /**
     * Provides an example of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.  Ignored if the properties examples, content or array are specified.
     *
     * @return an example of the parameter
     **/
    String example() default "";

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    /**
     * A reference to a parameter defined in components parameter.
     *
     * @since 2.0.3
     * @return the reference
     **/
    String ref() default "";

    Class<?>[] validationGroups() default {};
}
