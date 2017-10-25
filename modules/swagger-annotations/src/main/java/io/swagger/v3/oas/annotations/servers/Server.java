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

package io.swagger.v3.oas.annotations.servers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An object representing a Server.
 **/
@Target({ ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Servers.class)
@Inherited
public @interface Server {
  /**
   * Required. A URL to the target host. 
   * This URL supports Server Variables and may be relative, to indicate that the host location is relative to the location where the 
   * OpenAPI definition is being served. Variable substitutions will be made when a variable is named in {brackets}.
   * 
   * @return String url
   **/
  String url() default "";

  /**
   * An optional string describing the host designated by the URL. CommonMark syntax MAY be used for rich text representation.
   * 
   * @return String description
   **/
  String description() default "";

  /**
   * An array of variables used for substitution in the server's URL template.
   * 
   * @return array of ServerVariables
   **/
  ServerVariable[] variables() default {};

}
