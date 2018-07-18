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

package io.swagger.v3.oas.annotations.responses;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;

import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * The annotation may be used at method level or as field of {@link io.swagger.v3.oas.annotations.Operation} to define one or more responses of the
 * Operation.
 *
 * <p>swagger-jaxrs2 reader engine considers this annotation along with method return type and context as input to
 * resolve the OpenAPI Operation responses.</p>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#responseObject">Response (OpenAPI specification)</a>
 * @see io.swagger.v3.oas.annotations.Operation
 **/
@Target({METHOD, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Repeatable(ApiResponses.class)
public @interface ApiResponse {
    /**
     * A short description of the response.
     *
     * @return description of the response
     **/
    String description() default "";

    /**
     * The HTTP response code, or 'default', for the supplied response. May only have 1 default entry.
     *
     * @return response code
     **/
    String responseCode() default "default";

    /**
     * An array of response headers. Allows additional information to be included with response.
     *
     * @return array of headers
     **/
    Header[] headers() default {};

    /**
     * An array of operation links that can be followed from the response.
     *
     * @return array of links
     **/
    Link[] links() default {};

    /**
     * An array containing descriptions of potential response payloads, for different media types.
     *
     * @return array of content
     **/
    Content[] content() default {};

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    /**
     * A reference to a response defined in components responses.
     *
     * @since 2.0.3
     * @return the reference
     **/
    String ref() default "";

}
