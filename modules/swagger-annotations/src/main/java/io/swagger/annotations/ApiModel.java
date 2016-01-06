/**
 * Copyright 2016 SmartBear Software
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

package io.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides additional information about Swagger models.
 * <p>
 * Classes will be introspected automatically as they are used as types in operations,
 * but you may want to manipulate the structure of the models.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ApiModel {
    /**
     * Provide an alternative name for the model.
     * <p>
     * By default, the class name is used.
     */
    String value() default "";

    /**
     * Provide a longer description of the class.
     */
    String description() default "";

    /**
     * Provide a superclass for the model to allow describing inheritance.
     */
    Class<?> parent() default Void.class;

    /**
     * Supports model inheritance and polymorphism.
     * <p>
     * This is the name of the field used as a discriminator. Based on this field,
     * it would be possible to assert which sub type needs to be used.
     */
    String discriminator() default "";

    /**
     * An array of the sub types inheriting from this model.
     */
    Class<?>[] subTypes() default {};

    /**
     * Specifies a reference to the corresponding type definition, overrides any other metadata specified
     */

    String reference() default "";
}
