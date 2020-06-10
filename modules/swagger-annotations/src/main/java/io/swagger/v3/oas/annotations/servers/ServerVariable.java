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

package io.swagger.v3.oas.annotations.servers;

import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An object representing a Server Variable for server URL template substitution.
 **/
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ServerVariable {
    /**
     * Required.  The name of this variable.
     *
     * @return String name
     **/
    String name();

    /**
     * An array of allowable values for this variable.  This field map to the enum property in the OAS schema.
     *
     * @return String array of allowableValues
     **/
    String[] allowableValues() default "";

    /**
     * Required.  The default value of this variable.
     *
     * @return String defaultValue
     **/
    String defaultValue();

    /**
     * An optional description for the server variable.
     *
     * @return String description
     **/
    String description() default "";

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

}
