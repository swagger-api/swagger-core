/**
 *  Copyright 2015 SmartBear Software
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
 * High-level metadata for a Swagger definition - see
 * https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md#infoObject
 */

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Info {

    /**
     * @return the title of the API
     */

    String title();

    /**
     * @return the version of your API
     */

    String version();

    /**
     * @return an optional description of the API
     */

    String description() default "";

    /**
     * @return an optional terms of service for this API
     */

    String termsOfService() default "";

    /**
     * @return optional contact information for this API
     */

    Contact contact() default @Contact( name = "");

    /**
     * @return optional license information for this API
     */

    License license() default @License( name = "" );

    /**
     * @see Extension
     * @return optional list of extensions for this API
     */


    Extension[] extensions() default @Extension( properties = @ExtensionProperty(name="",value=""));
}
