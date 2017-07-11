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

import io.swagger.oas.annotations.enums.Explode;
import io.swagger.oas.annotations.media.ArraySchema;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.parameters.Parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 *
 * 
 **/


@Target({ ElementType.PARAMETER,
          ElementType.METHOD })
@Repeatable(Parameters.class)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Parameter {
  /**
   * the name of the parameter
   **/
  String name() default "";

  /**
   * the location of the parameter.  Ignored when empty string
   **/
  String in() default "";

  /**
   * Additional description data to provide on the purpose of the parameter
   **/
  String description() default "";

  /**
   * Specifies that the parameter is not optional and must be present
   **/
  boolean required() default false;

  /**
   * 
   **/
  boolean deprecated() default false;

  /**
   * When true, allows sending an empty value.  If false, the parameter will be considered \&quot;null\&quot; if no value is present.  This may create validation errors when the parameter is required.
   **/
  boolean allowEmptyValue() default false;

  /**
   * 
   **/
  String style() default "";

  /**
   * 
   **/
  Explode explode() default Explode.DEFAULT;

  /**
   * 
   **/
  boolean allowReserved() default false;

  /**
   * 
   **/
  Schema schema() default @Schema();


  ArraySchema array() default @ArraySchema();

  /**
   * 
   **/
  Content[] content() default @Content();

  /**
   * allows parameter to be marked as hidden
   */
  boolean hidden() default false;
}
