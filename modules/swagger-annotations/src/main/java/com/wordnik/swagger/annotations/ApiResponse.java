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
 * An ApiResponse represents a type of response from a server.  This can be used to
 * describe both success codes as well as errors.
 * If your Api has different response classes, you can describe them here by associating
 * a response class with a response code.  Note, Swagger does not allow multiple response
 * types for a single response code.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResponse {
  /** Response code to describe */
  int code();

  /** Human-readable message to accompany the response */
  String message();

  /** Optional response class to describe the payload of the message */
  Class<?> response() default Void.class;

  ResponseHeader[] responseHeaders() default @ResponseHeader(name = "", response = Void.class);
}
