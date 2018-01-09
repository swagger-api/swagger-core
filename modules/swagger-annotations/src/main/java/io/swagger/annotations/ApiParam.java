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
 * Adds additional meta-data for operation parameters.
 * <p>
 * This annotation can be used only in combination of JAX-RS 1.x/2.x annotations.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParam {
    /**
     * The parameter name.
     * <p>
     * The name of the parameter will be derived from the field/method/parameter name,
     * however you can override it.
     * <p>
     * Path parameters must always be named as the path section they represent.
     */
    String name() default "";

    /**
     * A brief description of the parameter.
     */
    String value() default "";

    /**
     * Describes the default value for the parameter.
     * <p>
     * If the parameter is annotated with JAX-RS's {@code @DefaultValue}, that value would
     * be used, but can be overridden by setting this property.
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
     * Path parameters will always be set as required, whether you set this property or not.
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
     * Hides the parameter from the list of parameters.
     */
    boolean hidden() default false;

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
