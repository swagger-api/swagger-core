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
 * Marks a class as a Swagger resource.
 * <p/>
 * The resource affects both the root document of Swagger, the Resource
 * Listing, and the API Declaration of that specific resource.
 * <p/>
 * Swagger will only include and introspect only classes that are annotated
 * with {@code @Api} and will ignore other resources (JAX-RS endpoints, Servlets and
 * so on).
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {
    /**
     * The 'path' that is going to be used to host the API Declaration of the
     * resource.
     * <p/>
     * For JAX-RS resources, this would normally have the same value as the {@code @Path}
     * on the resource, but can be any other value as well. It will serve as the path
     * where the documentation is hosted.
     * <p/>
     * For Servlets, this path has to be the path serving the Servlet.
     * <p/>
     * If the value isn't preceded with a slash, one would be added to it.
     */
    String value();

    /**
     * Corresponds to the `description` field of the Resource Listing API operation.
     * <p/>
     * This should be a short description of the resource.
     */
    String description() default "";

    /**
     * Corresponds to the `basePath` field of the API Declaration.
     * <p/>
     * The `basePath` is derived automatically by Swagger. This property allows
     * overriding the default value if needed.
     */
    String basePath() default "";

    /**
     * Optional explicit ordering of this API resource in the Resource Listing.
     */
    int position() default 0;

    /**
     * Corresponds to the `produces` field of the API Declaration.
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
     * Corresponds to the `consumes` field of the API Declaration.
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
     * Corresponds to the `authorizations` field of the API Declaration.
     * <p/>
     * Takes in a list of the required authorizations for this API Resource.
     * This may be overridden by specific operations.
     *
     * @see Authorization
     */
    Authorization[] authorizations() default @Authorization("");
}
