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

import io.swagger.v3.oas.annotations.media.Content;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a single request body.
 **/
@Target({ ElementType.PARAMETER })
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

}
