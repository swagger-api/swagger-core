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
 * An ApiModelProperty desecribes a property inside a model class.  The annotations can
 * apply to a method, a property, etc., depending on how the model scanner is configured and
 * used.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelProperty {
  /** Provide a human readable synopsis of this property */
  String value() default "";

  /**
   * Allows overriding the name of the property
   *
   * @return the overridden property name
   */
  String name() default "";

  /**
   * If the values that can be set are restricted, they can be set here. In the form of a comma separated list
   * <code>registered, active, closed</code>.
   *
   * @return the allowable values
   */
  String allowableValues() default "";

  /** 
   * specify an optional access value for filtering in a Filter 
   * implementation.  This
   * allows you to hide certain parameters if a user doesn't have access to them
   */
  String access() default "";

  /** long description of the property */
  String notes() default "";

  /**
   * The dataType. See the documentation for the supported datatypes. If the data type is a custom object, set
   * it's name, or nothing. In case of an enum use 'string' and allowableValues for the enum constants.
   */
  String dataType() default "";

  /**
   * Whether or not the property is required, defaults to false.
   * 
   * @return true if required, false otherwise
   */
  boolean required() default false;

  /**
   * allows explicitly ordering the property in the model.  Since reflection has no guarantee on
   * ordering, you should specify property order to keep models consistent across different VM implementations and versions.
   */
  int position() default 0;
  
  /**
   * Allows a model property to be marked as hidden in the swagger model definition
   */
  boolean hidden() default false;

  /**
   * A sample value for the property
   **/
  String example() default "";

  /**
   * Allows a model property to be designated as read only
   **/
  boolean readOnly() default false;
}
