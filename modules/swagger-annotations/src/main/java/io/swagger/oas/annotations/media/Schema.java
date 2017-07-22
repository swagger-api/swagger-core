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

import io.swagger.oas.annotations.ExternalDocumentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
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
   * Provides a java class as implementation for this schema.  When provided, additional information in the Schema annotation will augment the java class after introspection.
   **/
  Class<?> implementation() default Void.class;

  /**
   * Provides a java class to be used to disallow matching properties.
   **/
  Class<?> not() default Void.class;

  /**
   * Provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If more than one match the derived schemas, a validation error will occur.
   **/
  Class<?>[] oneOf() default {};

  /**
   * Provides an array of java class implementations which can be used to describe multiple acceptable schemas.  If any match, the schema will be considered valid.
   **/
  Class<?>[] anyOf() default {};

  /**
   * The name of the schema or property.
   **/
  String name() default "";

  /**
   * A title to explain the purpose of the schema.
   **/
  String title() default "";

  /**
   * Constrains a value such that when divided by the multipleOf, the remainder must be an integer.  Ignored if the value is 0.
   **/
  int multipleOf() default 0;

  /**
   * Sets the maximum numeric value for a property.  Ignored if the value is an empty string.
   **/
  String maximum() default "";

  /**
   * if true, makes the maximum value exclusive, or a less-than criteria.
   **/
  boolean exclusiveMaximum() default false;

  /**
   * Sets the minimum numeric value for a property.  Ignored if the value is an empty string.
   **/
  String minimum() default "";

  /**
   * If true, makes the minimum value exclusive, or a greater-than criteria.
   **/
  boolean exclusiveMinimum() default false;

  /**
   * Sets the maximum length of a string value.  Ignored if the value is Integer.MIN_VALUE.
   **/
  int maxLength() default Integer.MIN_VALUE;

  /**
   * Sets the minimum length of a string value.  Ignored if the value is Integer.MAX_VALUE.
   **/
  int minLength() default Integer.MAX_VALUE;

  /**
   * A pattern that the value must satisfy. Ignored if the value is an empty string.
   **/
  String pattern() default "";

  /**
   * Sets the maximum number of items in an array.  Ignored if value is Integer.MIN_VALUE.
   **/
  int maxItems() default Integer.MIN_VALUE;

  /**
   * Sets the minimum number of items in an array.  Ignored if value is Integer.MAX_VALUE.
   **/
  int minItems() default Integer.MAX_VALUE;

  /**
   * Determines whether an array of items will be unique.
   **/
  boolean uniqueItems() default false;

  /**
   * Constrains the number of arbitrary properties when additionalProperties is defined.  Ignored if value is 0.
   **/
  int maxProperties() default 0;

  /**
   * Constrains the number of arbitrary properties when additionalProperties is defined.  Ignored if value is 0.
   **/
  int minProperties() default 0;

  /**
   * Allows multiple properties in an object to be marked as required.
   **/
  String[] requiredProperties() default {};

  /**
   * Mandates that the annotated item is required or not.
   **/
  boolean required() default false;

  /**
   * A description of the schema.
   **/
  String description() default "";

  /**
   * Provides an optional override for the format.  If a consumer is unaware of the meaning of the format, they shall fall back to using the basic type without format.  For example, if \&quot;type: integer, format: int128\&quot; were used to designate a very large integer, most consumers will not understand how to handle it, and fall back to simply \&quot;type: integer\&quot;
   **/
  String format() default "";

  /**
   * References a schema definition in an external OpenAPI document.
   **/
  String ref() default "";

  /**
   * If true, designates a value as possibly null.
   **/
  boolean nullable() default false;

  /**
   * Sets whether the value should only be read during a response but not written to during a request.
   **/
  boolean readOnly() default false;

  /**
   * Sets whether a value should only be written to during a request but not returned during a response.
   **/
  boolean writeOnly() default false;

  /**
   * Provides an example of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.
   **/
  String example() default "";

  /**
   * Provides an array examples of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.
   **/
  ExampleObject[] examples() default {};
  
  /**
   * Additional external documentation for this schema.
   **/
  ExternalDocumentation externalDocs() default @ExternalDocumentation();

  /**
   * Specifies that a schema is deprecated and should be transitioned out of usage.
   **/
  boolean deprecated() default false;

  /**
   * Provides an override for the basic type of the schema.  Must be a valid type per the OpenAPI Specification.
   **/
  String type() default "";

  /**
   * Provides a list of allowable values.  This field map to the enum property in the OAS schema.
   */
  String[] allowableValues() default {};

  /**
   * Provides a default value.
   */
  String defaultValue() default "";

  /**
   * Allows schema to be marked as hidden.
   */
  boolean hidden() default false;
}
