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

import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation may be used to define the content/media type  of a parameter, request or response, by definining it as
 * field {@link io.swagger.v3.oas.annotations.Parameter#content()}, {@link io.swagger.v3.oas.annotations.parameters.RequestBody#content()} or {@link io.swagger.v3.oas.annotations.responses.ApiResponse#content()}.
 * <p>If {@link Content#schema()} is defined, swagger-jaxrs2 reader engine will consider it along with
 * JAX-RS annotations, element type and context as input to resolve the annotated element into an OpenAPI schema
 * definition for such element.</p>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#exampleObject">Example (OpenAPI specification)</a>
 * @see Schema
 * @see io.swagger.v3.oas.annotations.Parameter
 * @see io.swagger.v3.oas.annotations.responses.ApiResponse
 * @see io.swagger.v3.oas.annotations.parameters.RequestBody
 **/
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Content {
    /**
     * The media type that this object applies to.
     *
     * @return the media type value
     **/
    String mediaType() default "";

    /**
     * An array of examples used to show the use of the associated schema.
     *
     * @return the list of examples
     **/
    ExampleObject[] examples() default {};

    /**
     * The schema defining the type used for the content.
     *
     * @return the schema of this media type
     **/
    Schema schema() default @Schema();

    /**
     * The schema of the array that defines the type used for the content.
     *
     * @return the schema of the array
     */
    ArraySchema array() default @ArraySchema();

    /**
     * An array of encodings
     * The key, being the property name, MUST exist in the schema as a property.
     *
     * @return the array of encodings
     */
    Encoding[] encoding() default {};

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

}
