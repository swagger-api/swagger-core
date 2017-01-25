/**
 * Copyright 2016 SmartBear Software
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

package io.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a single parameter in an API Operation.
 * <p>
 * While {@link ApiParam} is bound to a JAX-RS parameter,
 * method or field, this allows you to manually define a parameter in a fine-tuned manner.
 * This is the only way to define parameters when using Servlets or other non-JAX-RS
 * environments.
 * <p>
 * This annotation must be used as a value of {@link ApiImplicitParams}
 * in order to be parsed.
 *
 * @see ApiImplicitParams
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiImplicitParam {
    /**
     * Name of the parameter.
     * <p>
     * For proper Swagger functionality, follow these rules when naming your parameters based on {@link #paramType()}:
     * <ol>
     * <li>If {@code paramType} is "path", the name should be the associated section in the path.</li>
     * <li>For all other cases, the name should be the parameter name as your application expects to accept.</li>
     * </ol>
     *
     * @see #paramType()
     */
    String name() default "";

    /**
     * A brief description of the parameter.
     */
    String value() default "";

    /**
     * Describes the default value for the parameter.
     */
    String defaultValue() default "";

    /**
     * Limits the acceptable values for this parameter.
     * <p>
     * There are three ways to describe the allowable values:
     * <ol>
     * <li>To set a list of values, provide a comma-separated list.
     * For example: {@code first, second, third}.</li>
     * <li>To set a range of values, start the value with "range", and surrounding by square
     * brackets include the minimum and maximum values, or round brackets for exclusive minimum and maximum values.
     * For example: {@code range[1, 5]}, {@code range(1, 5)}, {@code range[1, 5)}.</li>
     * <li>To set a minimum/maximum value, use the same format for range but use "infinity"
     * or "-infinity" as the second value. For example, {@code range[1, infinity]} means the
     * minimum allowable value of this parameter is 1.</li>
     * </ol>
     */
    String allowableValues() default "";

    /**
     * Specifies if the parameter is required or not.
     * <p>
     * Path parameters should always be set as required.
     */
    boolean required() default false;

    /**
     * Allows for filtering a parameter from the API documentation.
     * <p>
     * See io.swagger.core.filter.SwaggerSpecFilter for further details.
     */
    String access() default "";

    /**
     * Specifies whether the parameter can accept multiple values by having multiple occurrences.
     */
    boolean allowMultiple() default false;

    /**
     * The data type of the parameter.
     * <p>
     * This can be the class name or a primitive.
     */
    String dataType() default "";

    /**
     * The class of the parameter.
     * <p>
     * Overrides {@code dataType} if provided.
     */
    Class<?> dataTypeClass() default Void.class;

    /**
     * The parameter type of the parameter.
     * <p>
     * Valid values are {@code path}, {@code query}, {@code body},
     * {@code header} or {@code form}.
     */
    String paramType() default "";

    /**
     * a single example for non-body type parameters
     *
     * @since 1.5.4
     *
     * @return
     */
    String example() default "";

    /**
     * Examples for the parameter.  Applies only to BodyParameters
     *
     * @since 1.5.4
     *
     * @return
     */
    Example examples() default @Example(value = @ExampleProperty(mediaType = "", value = ""));

    /**
     * Adds the ability to override the detected type
     *
     * @since 1.5.11
     *
     * @return
     */
    String type() default "";

    /**
     * Adds the ability to provide a custom format
     *
     * @since 1.5.11
     *
     * @return
     */
    String format() default "";

    /**
     * Adds the ability to set a format as empty
     *
     * @since 1.5.11
     *
     * @return
     */
    boolean allowEmptyValue() default false;

    /**
     * adds ability to be designated as read only.
     *
     * @since 1.5.11
     *
     */
    boolean readOnly() default false;

    /**
     * adds ability to override collectionFormat with `array` types
     *
     * @since 1.5.11
     *
     */
    String collectionFormat() default "";
}
