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

package io.swagger.v3.oas.annotations.info;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation provides metadata about the API, and maps to the Info object in OpenAPI Specification 3.
 **/
@Target({ ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Info {
  /**
   * The title of the application.
   * @return the application's title
   **/
  String title() default "";

  /**
   * A short description of the application. CommonMark syntax can be used for rich text representation.
   * @return the application's description
   **/
  String description() default "";

  /**
   * A URL to the Terms of Service for the API. Must be in the format of a URL.
   * @return the application's terms of service
   **/
  String termsOfService() default "";

  /**
   * The contact information for the exposed API.
   * @return a contact for the application
   **/
  Contact contact() default @Contact();

  /**
   * The license information for the exposed API.
   * @return the license of the application
   **/
  License license() default @License();

  /**
   * The version of the API definition.
   * @return the application's version
   **/
  String version() default "";

}
