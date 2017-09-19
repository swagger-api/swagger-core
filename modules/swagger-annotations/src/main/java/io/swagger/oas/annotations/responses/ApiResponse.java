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

package io.swagger.oas.annotations.responses;

import io.swagger.oas.annotations.headers.Header;
import io.swagger.oas.annotations.links.Link;
import io.swagger.oas.annotations.media.Content;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a single response from an API Operation, including design-time, static links to operations based on the response.
 **/
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ApiResponse {
    /**
     * A short description of the response. 
     **/
    String description() default "";

    /**
     * The HTTP response code, or 'default', for the supplied response. May only have 1 default entry.  
     **/
    String responseCode() default "default";

    /**
     * An array of response headers. Allows additional information to be included with response.
     **/
    Header[] headers() default {};

    /**
     * An array of operation links that can be followed from the response.
     **/
    Link[] links() default {};

    // TODO #2312 as array, according to spec
    /**
     * An array containing descriptions of potential response payloads, for different media types.
     **/
    Content[] content() default {};

}
