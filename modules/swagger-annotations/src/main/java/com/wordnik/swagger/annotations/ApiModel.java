/**
 *  Copyright 2013 Wordnik, Inc.
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
 * A bean class used in the REST-api.
 * Suppose you have an interface
 * <code>@PUT @ApiOperation(...) void foo(FooBean fooBean)</code>, there is
 * no direct way to see what fields <code>FooBean</code> would have. This
 * annotation is meant to give a description of <code>FooBean</code> and
 * then have the fields of it be annotated with
 * <code>@ApiModelProperty</code>.
 *
 * @author Heiko W. Rupp
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModel {
  /** Provide a synopsis of this class */
  String value() default "";
  /** Provide a longer description of the class */
  String description() default "";
  /** Provide a superclass for the model to allow describing inheritence */
  Class<?> parent() default Void.class;
  /** for models with a base class, a discriminator can be provided for polymorphic use cases */
  String discriminator() default "";
  Class<?>[] subTypes() default {};
}
