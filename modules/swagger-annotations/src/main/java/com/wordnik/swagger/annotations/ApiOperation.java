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
 * Describes an operation or typically a HTTP method against a specific path.
 * <p/>
 * Operations with equivalent paths are grouped in an array in the Api Declaration.
 * A combination of a HTTP method and a path creates a unique operation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiOperation {
    /**
     * Corresponds to the `summary` field of the operation.
     * <p/>
     * Provides a brief description of this operation. Should be 120 characters or less
     * for proper visibility in Swagger-UI.
     */
    String value();

    /**
     * Corresponds to the 'notes' field of the operation.
     * <p/>
     * A verbose description of the operation.
     */
    String notes() default "";

    /**
     * The response type of the operation.
     * <p/>
     * In JAX-RS applications, the return type of the method would automatically be used, unless it is
     * {@code javax.ws.rs.core.Response}. In that case, the operation return type would default to `void`
     * as the actual response type cannot be known.
     * <p/>
     * Setting this property would override any automatically-derived data type.
     * <p/>
     * If the value used is a class representing a primitive ({@code Integer}, {@code Long}, ...)
     * the corresponding primitive type will be used.
     */
    Class<?> response() default Void.class;

    /**
     * Notes whether the response type is a list of values.
     * <p/>
     * Valid values are "List", "Array" and "Set". "List" and "Array" are regular lists (no
     * difference between them), and "Set" means the list contains unique values only.
     * <p/>
     * Any other value will be ignored.
     */
    String responseContainer() default "";

    /**
     * Currently not implemented in readers, reserved for future use.
     */
    String tags() default "";

    /**
     * Corresponds to the `method` field as the HTTP method used.
     * <p/>
     * If not stated, in JAX-RS applications, the following JAX-RS annotations would be scanned
     * and used: {@code @GET}, {@code @HEAD}, {@code @POST}, {@code @PUT}, {@code @DELETE} and {@code @OPTIONS}.
     * Note that even though not part of the JAX-RS specification, if you create and use the {@code @PATCH} annotation,
     * it will also be parsed and used. If the httpMethod property is set, it will override the JAX-RS annotation.
     * <p/>
     * For Servlets, you must specify the HTTP method manually.
     * <p/>
     * Acceptable values are "GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS" and "PATCH".
     */
    String httpMethod() default "";

    /**
     * Optional explicit ordering of this API resource in the Resource Listing.
     */
    int position() default 0;

    /**
     * Corresponds to the `nickname` field.
     * <p/>
     * The nickname field is used by third-party tools to uniquely identify this operation. In JAX-RS environemnt, this
     * would default to the method name, but can be overridden.
     * <p/>
     * For Servlets, you must specify this field.
     */
    String nickname() default "";

    /**
     * Corresponds to the `produces` field of the operation.
     * <p/>
     * Takes in comma-separated values of content types.
     * For example, "application/json, application/xml" would suggest this API Resource
     * generates JSON and XML output.
     * <p/>
     * For JAX-RS resources, this would automatically take the value of the {@code @Produces}
     * annotation if such exists. It can also be used to override the {@code @Produces} values
     * for the Swagger documentation.
     */
    String produces() default "";

    /**
     * Corresponds to the `consumes` field of the operation.
     * <p/>
     * Takes in comma-separated values of content types.
     * For example, "application/json, application/xml" would suggest this API Resource
     * accepts JSON and XML input.
     * <p/>
     * For JAX-RS resources, this would automatically take the value of the {@code @Consumes}
     * annotation if such exists. It can also be used to override the {@code @Consumes} values
     * for the Swagger documentation.
     */
    String consumes() default "";

    /**
     * This property is currently not in use.
     */
    String protocols() default "";

    /**
     * Corresponds to the `authorizations` field of the operation.
     * <p/>
     * Takes in a list of the required authorizations for this operation.
     *
     * @see Authorization
     */
    Authorization[] authorizations() default @Authorization("");

    /**
     * Hides the operation from the list of operations.
     */
    boolean hidden() default false;
}
