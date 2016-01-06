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
 * High-level metadata for a Swagger definition - see
 * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#infoObject
 *
 * @since 1.5.0
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Info {

    /**
     * The title of the API.
     *
     * @return the title of the API
     */
    String title();

    /**
     * The version of your API.
     *
     * @return the version of your API
     */
    String version();

    /**
     * An optional description of the API.
     *
     * @return an optional description of the API
     */
    String description() default "";

    /**
     * An optional terms of service for this API.
     *
     * @return an optional terms of service for this API
     */
    String termsOfService() default "";

    /**
     * Optional contact information for this API.
     *
     * @return optional contact information for this API
     */
    Contact contact() default @Contact(name = "");

    /**
     * Optional license information for this API.
     *
     * @return optional license information for this API
     */
    License license() default @License(name = "");

    /**
     * Optional list of extensions for this API.
     *
     * @return optional list of extensions for this API
     * @see Extension
     */
    Extension[] extensions() default @Extension(properties = @ExtensionProperty(name = "", value = ""));
}
