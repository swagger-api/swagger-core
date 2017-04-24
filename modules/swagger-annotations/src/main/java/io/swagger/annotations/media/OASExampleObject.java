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


/**
 * 
 *
 * 
 **/


@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OASExampleObject {
  /**
   * a unique name to identify this particular example
   **/
  String name() default "";

  /**
   * a brief summary of the purpose or context of the example
   **/
  String summary() default "";

  /**
   * a string representation of the example.  If the media type associated with the example allows parsing into an object, it may be converted from a string
   **/
  String value() default "";

  /**
   * a URL to point to an external document to be used as an example.  This is mutually exclusive with the value property
   **/
  String externalValue() default "";

}
