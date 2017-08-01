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

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.oas.annotations.enums.Explode;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.Schema;

/**
 * Describes a single operation parameter
 **/
@Target({ ElementType.PARAMETER,
          ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Parameter {
  /**
   * The name of the parameter.
   * @return the parameter's name
   **/
  String name() default "";

  /**
   * The location of the parameter.  Possible values are "query", "header", "path" or "cookie".  Ignored when empty string.
   * @return the parameter's location
   **/
  String in() default "";

  /**
   * Additional description data to provide on the purpose of the parameter
   * @return the parameter's description
   **/
  String description() default "";

  /**
   * Specifies that the parameter is not optional and must be present.
   * @return whether or not the parameter is required
   **/
  boolean required() default false;

  /**
   * Specifies that a parameter is deprecated and should be transitioned out of usage.
   * @return whether or not the parameter is deprecated
   **/
  boolean deprecated() default false;

  /**
   * When true, allows sending an empty value.  If false, the parameter will be considered \&quot;null\&quot; if no value is present.  This may create validation errors when the parameter is required.
   * @return whether or not the parameter allows empty values
   **/
  boolean allowEmptyValue() default false;

  /**
   * Describes how the parameter value will be serialized depending on the type of the parameter value. Default values (based on value of in): for query - form; for path - simple; for header - simple; for cookie - form.
   * @return the style of the parameter
   **/
  String style() default "";

  /**
   * When this is true, parameter values of type array or object generate separate parameters for each value of the array or key-value pair of the map. For other types of parameters this property has no effect. When style is form, the default value is true. For all other styles, the default value is false.
   *@return whether or not to expand individual array members
   **/
  Explode explode() default Explode.DEFAULT;

  /**
   * Determines whether the parameter value should allow reserved characters, as defined by RFC3986. This property only applies to parameters with an in value of query. The default value is false.
   * @return whether or not the parameter allows reserved characters
   **/
  boolean allowReserved() default false;

  /**
   * The schema defining the type used for the parameter.
   * @return the schema of the parameter
   **/
  Schema schema() default @Schema();

  /**
   * The representation of this parameter, for different media types.
   * @return the content of the parameter
   **/
  Content[] content() default @Content();

  /**
   * Allows this parameter to be marked as hidden
   * @return whether or not this parameter is hidden
   */
  boolean hidden() default false;
  
  /**
   * Provides an array examples of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.
   * @return the list of examples for this parameter
   **/
  String[] examples() default {};

  /**
   * Provides an example of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.
   * @return an example of the parameter
   **/
  String example() default "";
}
