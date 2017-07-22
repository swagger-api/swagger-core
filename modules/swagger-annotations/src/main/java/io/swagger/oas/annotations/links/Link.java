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

package io.swagger.oas.annotations.links;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.oas.annotations.servers.Server;


/**
 * The Link object represents a possible design-time link for a response. The presence of a link does not guarantee the caller's ability to successfully invoke it, rather it provides a known relationship and traversal mechanism between responses and other operations.
 **/
@Target({ ElementType.FIELD,
          ElementType.METHOD,
          ElementType.PARAMETER,
          ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Link {
  /**
   * The name of this link.
   **/
  String name() default "";

  /**
   * A relative or absolute reference to an OAS operation. This field is mutually exclusive of the operationId field, and must point to an Operation Object. Relative operationRef values may be used to locate an existing Operation Object in the OpenAPI definition.
   **/
  String operationRef() default "";

  /**
   * The name of an existing, resolvable OAS operation, as defined with a unique operationId. This field is mutually exclusive of the operationRef field.
   **/
  String operationId() default "";

  /**
   * Array of parameters to pass to an operation as specified with operationId or identified via operationRef.
   **/
  LinkParameter[] parameters() default {};

  /**
   * A description of the link. CommonMark syntax may be used for rich text representation.
   **/
  String description() default "";
  
  /**
   * A literal value or {expression} to use as a request body when calling the target operation.
   **/
  String requestBody() default "";
  
  /**
   * An alternative server  to service this operation.
   **/
  Server server() default @Server;

}
