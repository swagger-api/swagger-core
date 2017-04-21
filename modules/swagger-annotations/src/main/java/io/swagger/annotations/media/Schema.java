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

package io.swagger.annotations.media;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ExternalDocumentation;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * 
 **/


@Target({ ElementType.FIELD,
          ElementType.TYPE,
          ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Schema {
  /**
   * 
   **/
  Class<?> implementation() default Void.class;

  /**
   * 
   **/
  Class<?> not() default Void.class;

  /**
   * 
   **/
  Class<?>[] oneOf() default Void.class;

  /**
   * 
   **/
  Class<?>[] anyOf() default Void.class;

  /**
   * the name of the schema or property
   **/
  String name() default "";

  /**
   * a title to explain the purpose of the schema
   **/
  String title() default "";

  /**
   * constrains a value such that when divided by the multipleOf, the remainder must be an integer.
   **/
  int multipleOf() default 0;

  /**
   * sets the minimum numeric value for a property.  Ignored if the value is an empty string.
   **/
  String maximum() default "";

  /**
   * if true, makes the maximum value exclusive, or a less-than criteria
   **/
  boolean exclusiveMaximum() default false;

  /**
   * if true, makes the maximum value exclusive, or a greater-than criteria
   **/
  String minimum() default "";

  /**
   * 
   **/
  boolean exclusiveMinimum() default false;

  /**
   * sets the maximum length of a string value.  Ignored if the value is Integer.MIN_VALUE.
   **/
  int maxLength() default Integer.MIN_VALUE;

  /**
   * sets the minimum length of a string value.  Ignored if the value is Integer.MAX_VALUE.
   **/
  int minLength() default Integer.MAX_VALUE;

  /**
   * a pattern that the value must satisfy
   **/
  String pattern() default "";

  /**
   * sets the maximum number of items in an array.  Ignored if value is Integer.MIN_VALUE.
   **/
  int maxItems() default Integer.MIN_VALUE;

  /**
   * sets the minimum number of items in an array.  Ignored if value is Integer.MAX_VALUE.
   **/
  int minItems() default Integer.MAX_VALUE;

  /**
   * determines whether an array of items will be unique
   **/
  boolean uniqueItems() default false;

  /**
   * constrains the number of arbitrary properties when additionalProperties is defined.  Ignored if value is 0.
   **/
  int maxProperties() default 0;

  /**
   * constrains the number of arbitrary properties when additionalProperties is defined.  Ignored if value is 0.
   **/
  int minProperties() default 0;

  /**
   * Allows multiple properties in an object to be marked as required
   **/
  String[] requiredProperties() default "";

  /**
   * Mandates that the annotated item is required or not
   **/
  boolean required() default false;

  /**
   * a description of the schema
   **/
  String description() default "";

  /**
   * 
   **/
  String format() default "";

  /**
   * 
   **/
  String ref() default "";

  /**
   * if true, designates a value as possibly null
   **/
  boolean nulable() default false;

  /**
   * sets whether the value can be modified by a consumer
   **/
  boolean readOnly() default false;

  /**
   * sets whether a value can only be written to by a consumer
   **/
  boolean writeOnly() default false;

  /**
   * 
   **/
  String[] examples() default "";

  /**
   * 
   **/
  String example() default "";

  /**
   * 
   **/
  ExternalDocumentation externalDocs() default @ExternalDocumentation();

  /**
   * 
   **/
  boolean deprecated() default false;

  /**
   * 
   **/
  String type() default "";

}
