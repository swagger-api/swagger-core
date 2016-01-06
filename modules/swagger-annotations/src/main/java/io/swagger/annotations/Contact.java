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
 * Contact metadata available within the info section of a Swagger definition - see
 * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#contactObject
 *
 * @since 1.5.0
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Contact {

    /**
     * The name of the contact.
     *
     * @return the name of the contact
     */
    String name();

    /**
     * Optional URL associated with this contact.
     *
     * @return an optional URL associated with this contact
     */
    String url() default "";

    /**
     * Optional email for this contact.
     *
     * @return an optional email for this contact
     */
    String email() default "";
}
