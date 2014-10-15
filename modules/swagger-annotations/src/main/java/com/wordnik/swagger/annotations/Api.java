/**
 *  Copyright 2014 Reverb Technologies, Inc.
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
 * describes a top-level api.  Classes with @Api annotations will
 * be included in the Resource Listing: https://github.com/wordnik/swagger-core/wiki/Resource-Listing
 * for details
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {
  /** Short description of the Api */
  String value();

  /** General description of this class */
  String description() default "";

  /** The base path that is prepended to all @Path elements. This may be an override for certain scenarios only */
  String basePath() default "";
  
  /** optional explicit ordering of this Api in the Resource Listing */  
  int position() default 0;

  /** content type produced by this Api */
  String produces() default "";

  /** media type consumed by this Api */
  String consumes() default "";

  /** protocols that this Api requires (i.e. https) */
  String protocols() default "";

  /** authorizations required by this Api */
  // String authorizations() default "";
  /** authorizations required by this Api */
  Authorization[] authorizations() default @Authorization(value = "", type = "");
}
