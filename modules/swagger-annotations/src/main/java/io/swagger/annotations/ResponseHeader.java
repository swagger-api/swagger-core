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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a header that can be provided as part of the response.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseHeader {
    /**
     * Header's name.
     */
    String name() default "";

    /**
     * Long description of the response header.
     */
    String description() default "";

    /**
     * Header's data type.
     */
    Class<?> response() default Void.class;

    /**
     * Declares a container wrapping the response header.
     * <p>
     * Valid values are "List" or "Set". Any other value will be ignored.
     */
    String responseContainer() default "";
}