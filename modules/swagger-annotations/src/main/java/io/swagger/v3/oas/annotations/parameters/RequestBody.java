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

package io.swagger.v3.oas.annotations.parameters;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.Content;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * The annotation may be used on a method parameter to define it as the Request Body of the operation, and/or to define
 * additional properties for such request body.
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#requestBodyObject">Request Body (OpenAPI specification)</a>
 * @see io.swagger.v3.oas.annotations.Parameter
 * @see Content
 **/
@Target({METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RequestBody {
    /**
     * A brief description of the request body.
     *
     * @return description of the request body
     **/
    String description() default "";

    /**
     * The content of the request body.
     *
     * @return array of content
     **/
    Content[] content() default {};

    /**
     * Determines if the request body is required in the request. Defaults to false.
     *
     * @return boolean
     **/
    boolean required() default false;

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

}
