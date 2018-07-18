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

package io.swagger.v3.oas.annotations;

import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * The annotation may be used at method level or as field of {@link Operation} to add a reference to an external
 * resource for extended documentation of an <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#operationObject">Operation (OpenAPI specification)</a>.
 * <p>It may also be used to add external documentation to {@link io.swagger.v3.oas.annotations.tags.Tag},
 * {@link io.swagger.v3.oas.annotations.headers.Header} or {@link io.swagger.v3.oas.annotations.media.Schema},
 * or as field of {@link OpenAPIDefinition}.</p>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#externalDocumentationObject">External Documentation (OpenAPI specification)</a>
 * @see OpenAPIDefinition
 **/
@Target({METHOD, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExternalDocumentation {

    /**
     * A short description of the target documentation.
     *
     * @return the documentation description
     **/
    String description() default "";

    /**
     * The URL for the target documentation. Value must be in the format of a URL.
     *
     * @return the documentation URL
     **/
    String url() default "";

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

}
