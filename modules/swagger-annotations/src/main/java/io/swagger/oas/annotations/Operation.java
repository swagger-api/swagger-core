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

package io.swagger.oas.annotations;

import io.swagger.oas.annotations.parameters.RequestBody;
import io.swagger.oas.annotations.responses.ApiResponse;
import io.swagger.oas.annotations.servers.Server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Operation Annotation
 *
 * TODO: longer description
 **/


@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Operation {
  /**
   * the HTTP method for this operation
   **/
  String method() default "";

  /**
   * Tags can be used for logical grouping of operations by resources or any other qualifier.
   **/
  String[] tags() default "";

  /**
   * Provides a brief description of this operation. Should be 120 characters or less for proper visibility in Swagger-UI.
   **/
  String summary() default "";

  /**
   * A verbose description of the operation.
   **/
  String description() default "";

  /**
   *
   **/
  ExternalDocumentation externalDocs() default @ExternalDocumentation();

  /**
   * The operationId is used by third-party tools to uniquely identify this operation.
   **/
  String operationId() default "";

  /**
   * An optional array of parameters which will be added to any automatically detected parameters in the method itself
   **/
  Parameter[] parameters() default @Parameter();

  /**
   *
   **/
  RequestBody requestBody() default @RequestBody();

  /**
   *
   **/
  ApiResponse[] responses() default @ApiResponse();

  /**
   * allows an operation to be marked as deprecated.  Alternatively use the @Deprecated annotation
   **/
  boolean deprecated() default false;

  /**
   *
   **/
  Server[] servers() default @Server();

}
