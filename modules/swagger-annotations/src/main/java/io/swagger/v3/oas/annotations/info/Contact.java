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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Contact information for the exposed API.
 **/
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface Contact {
  /**
   * The identifying name of the contact person/organization.
   * @return the name of the contact
   **/
  String name() default "";

  /**
   * The URL pointing to the contact information. Must be in the format of a URL.
   * @return the URL of the contact
   **/
  String url() default "";

  /**
   * The email address of the contact person/organization. Must be in the format of an email address.
   * @return the email address of the contact
   **/
  String email() default "";

}
