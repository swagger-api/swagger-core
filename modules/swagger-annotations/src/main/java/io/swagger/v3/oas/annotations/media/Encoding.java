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

package io.swagger.v3.oas.annotations.media;

import io.swagger.v3.oas.annotations.headers.Header;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *
 * Single encoding definition to be applied to single Schema Object
 **/

@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Encoding {

    /**
     * The name of this encoding object instance.
     * This property is a key in encoding map of MediaType object and
     * MUST exist in a schema as a property.
     * 
     * @return name of the encoding
     **/
    String name() default "";

    /**
     * The Content-Type for encoding a specific property.
     * 
     * @return content type of the encoding
     **/
    String contentType() default "";

    /**
     * Describes how a specific property value will be serialized depending on its type
     * 
     * @return style of the encoding
     **/
    String style() default "";

    /**
     *
     * When this is true, property values of type array or object generate separate parameters for each value of the array,
     * or key-value-pair of the map.
     * 
     *@return boolean
     **/
    boolean explode() default false;

    /**
     *
     * Determines whether the parameter value SHOULD allow reserved characters,
     * as defined by RFC3986 to be included without percent-encoding.
     * 
     *@return boolean
     **/
    boolean allowReserved() default false;

    /**
     * An array of header objects
     * 
     *@return array of headers
     */
    Header[] headers() default {};

}
