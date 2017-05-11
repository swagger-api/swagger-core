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

package io.swagger.oas.annotations.media;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiModel;
import io.swagger.oas.annotations.ExternalDocumentation;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * The Schema Object allows the definition of input and output data types. These types can be objects, but also primitives and arrays. This object is an extended subset of the JSON Schema Specification Wright Draft 00.
 **/


@Target({ ElementType.FIELD,
          ElementType.METHOD,
          ElementType.PARAMETER,
          ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Schema {
  /**
   * provides a java class as implementation for this schema.  When provided, additional information in the Schema annotation will augment the java class after introspection
   **/
  Class<?> implementation() default Void.class;

  /**
   * provides a java class to be used to disallow matching properties.
   **/
  Class<?> not() default Void.class;

  /**
   * provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If more than one match the derived schemas, a validation error will occur
   **/
  Class<?>[] oneOf() default Void.class;

  /**
   * provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If any match, the schema will be considered valid
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
   * provides an optional override for the format.  If a consumer is unaware of the meaning of the format, they shall fall back to using the basic type without format.  For example, if \&quot;type: integer, format: int128\&quot; were used to designate a very large integer, most consumers will not understand how to handle it, and fall back to simply \&quot;type: integer\&quot;
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
   * provides an array examples of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.
   **/
  String[] examples() default "";

  /**
   * provides an example of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.
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
   * provides an override for the basic type of the schema.  Must be a valid type per the OpenAPI Specification
   **/
  String type() default "";

}
