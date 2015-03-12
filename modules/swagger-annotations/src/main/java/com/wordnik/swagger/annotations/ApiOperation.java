/**
 *  Copyright 2015 Reverb Technologies, Inc.
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
 * Describes an operation or typically a HTTP method against a specific path.  Operations
 * with equivalent paths are grouped in an array in the Api Declaration.  See
 * https://github.com/wordnik/swagger-core/wiki/API-Declaration
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiOperation {
  /** Brief description of the operation  */
  String value();

  /** long description of the operation */
  String notes() default "";

  /**
   * A list of tags for API documentation control. 
   * Tags can be used for logical grouping of operations by resources or any other qualifier.
   * 
   * @since 1.5.2
   */
  String[] tags() default "";

  /** default response class from the operation */
  Class<?> response() default Void.class;

  /** if the response class is within a container, specify it here */
  String responseContainer() default "";

  /** the HTTP method, i.e GET, PUT, POST, DELETE, PATCH, OPTIONS */
  String httpMethod() default "";

  /** allow explicit ordering of operations inside the Api Declaration */
  int position() default 0;

  /** the nickname for the operation, to override what is detected by the annotation scanner */
  String nickname() default "";
  
  /** content type produced by this Api */
  String produces() default "";

  /** media type consumed by this Api */
  String consumes() default "";

  /** protocols that this Api requires (i.e. https) */
  String protocols() default "";

  /** authorizations required by this Api */
  //String authorizations() default "";

  /** authorizations required by this Api */
  Authorization[] authorizations() default @Authorization(value = "", type = "");

  /**
   * Allows an operation to be marked as hidden
   */
  boolean hidden() default false;

  ResponseHeader[] responseHeaders() default @ResponseHeader(name = "", response = Void.class);
}
