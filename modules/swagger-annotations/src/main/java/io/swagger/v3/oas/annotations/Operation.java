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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a single API operation on a path.
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Operation {
    /**
     * The HTTP method for this operation.
     * @return the HTTP method of this operation
     **/
    String method() default "";

    /**
     * Tags can be used for logical grouping of operations by resources or any other qualifier.
     * @return the list of tags associated with this operation
     **/
    String[] tags() default {};

    /**
     * Provides a brief description of this operation. Should be 120 characters or less for proper visibility in Swagger-UI.
     * @return a summary of this operation
     **/
    String summary() default "";

    /**
     * A verbose description of the operation.
     * @return a description of this operation
     **/
    String description() default "";

    /**
     * Additional external documentation for this operation.
     * @return additional documentation about this operation
     **/
    ExternalDocumentation externalDocs() default @ExternalDocumentation();

    /**
     * The operationId is used by third-party tools to uniquely identify this operation.
     * @return the ID of this operation
     **/
    String operationId() default "";

    /**
     * An optional array of parameters which will be added to any automatically detected parameters in the method itself.
     * @return the list of parameters for this operation
     **/
    Parameter[] parameters() default {};

    /**
     * The list of possible responses as they are returned from executing this operation.
     * @return the list of responses for this operation
     **/
    ApiResponse[] responses() default {};

    /**
     * Allows an operation to be marked as deprecated.  Alternatively use the @Deprecated annotation
     * @return whether or not this operation is deprecated
     **/
    boolean deprecated() default false;

    /**
     * A declaration of which security mechanisms can be used for this operation.
     * @return the array of security requirements for this Operation
     */
    SecurityRequirement[] security() default {};

    /**
     * An alternative server array to service this operation.
     * @return the list of servers hosting this operation
     **/
    Server[] servers() default {};

    /**
     * The list of optional extensions
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    /**
     * Allows this operation to be marked as hidden
     * @return whether or not this operation is hidden
     */
    boolean hidden() default false;
}
