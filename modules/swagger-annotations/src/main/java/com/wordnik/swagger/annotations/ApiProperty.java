/**
 *  Copyright 2012 Wordnik, Inc.
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

/** ApiProperty can be put on a Method to allow swagger to understand the json fields datatype and more. */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiProperty {
	/** Provide a human readable synopsis of this property */
    String value() default "";

	/**
	 * If the values that can be set are restricted, they can be set here. In the form of a comma separated list
	 * <code>registered, active, closed</code>.
	 *
	 * @return the allowable values
	 */
    String allowableValues() default "";
    String access() default "";
	/** Provide any extra information */
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
}
