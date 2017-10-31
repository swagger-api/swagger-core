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

package io.swagger.v3.oas.annotations.callbacks;

import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This object represents a callback URL that will be invoked.
 **/
@Target({ ElementType.FIELD,
          ElementType.METHOD,
          ElementType.PARAMETER,
          ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Callbacks.class)
@Inherited
public @interface Callback {
  /**
   * The friendly name used to refer to this callback
   * @return the name of the callback
   **/
  String name() default "";

/**
 * An absolute URL which defines the destination which will be called with the supplied operation definition.  
 * @return the callback URL
 */
  String callbackUrlExpression() default "";

  /**
   * The array of operations that will be called out-of band 
   * @return the callback operations
   **/
  Operation[] operation() default {};

}
