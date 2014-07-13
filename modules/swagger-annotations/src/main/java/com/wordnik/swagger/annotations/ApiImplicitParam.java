/**
 *  Copyright 2013 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a single parameter in an API Operation.
 * <p/>
 * While {@link com.wordnik.swagger.annotations.ApiParam} is bound to a JAX-RS parameter,
 * method or field, this allows you to manually define a parameter in a fine-tuned manner.
 * This is the only way to define parameters when using Servlets or other non-JAX-RS
 * environments.
 * <p/>
 * This annotation must be used as a value of {@link com.wordnik.swagger.annotations.ApiImplicitParams}
 * in order to be parsed.
 *
 * @see com.wordnik.swagger.annotations.ApiImplicitParams
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiImplicitParam {
    /**
     * Name of the parameter.
     * <p/>
     * For proper Swagger functionality, follow these rules when naming your parameters based on {@link #paramType()}:
     * <ol>
     * <li>If {@code paramType} is "path", the name should be the associated section in the path.</li>
     * <li>If {@code paramType} is "body", the name should be "body".</li>
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
     * <p/>
     * There are three ways to describe the allowable values:
     * <ol>
     * <li>To set a list of values, provide a comma-separated list surrounded by square brackets.
     * For example: {@code [first, second, third]}.</li>
     * <li>To set a range of values, start the value with "range", and surrounding by square
     * brackets include the minimum and maximum values. For example: {@code range[1, 5]}.</li>
     * <li>To set a minimum/maximum value, use the same format for range but use "infinity"
     * or "-infinity" as the second value. For example, {@code range[1, infinity]} means the
     * minimum allowable value of this parameter is 1.</li>
     * </ol>
     */
    String allowableValues() default "";

    /**
     * Specifies if the parameter is required or not.
     * <p/>
     * Path parameters should always be set as required.
     */
    boolean required() default false;

    /**
     * Allows for filtering a parameter from the API documentation.
     *
     * @see com.wordnik.swagger.core.filter.SwaggerSpecFilter
     */
    String access() default "";

    /**
     * Specifies whether the parameter can accept multiple comma-separated values.
     */
    boolean allowMultiple() default false;

    /**
     * The data type of the parameter.
     * <p/>
     * This can be the class name or a primitive.
     */
    String dataType() default "";

    /**
     * The parameter type of the parameter.
     *
     * Valid values are {@code path}, {@code query}, {@code body}, {@code header} or {@code form}.
     */
    String paramType() default "";
}
