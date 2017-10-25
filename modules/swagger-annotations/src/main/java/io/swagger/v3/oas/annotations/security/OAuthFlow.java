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

package io.swagger.v3.oas.annotations.security;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Configuration details for a supported OAuth Flow.
 **/
@Target({  })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OAuthFlow {
  /**
   * The authorization URL to be used for this flow. This must be in the form of a URL.  Applies to oauth2 ("implicit", "authorizationCode") type.
   * 
   * @return the authorization url
   **/
  String authorizationUrl() default "";

  /**
   * The token URL to be used for this flow. This must be in the form of a URL.  Applies to oauth2 ("password", "clientCredentials", "authorizationCode") type.
   * 
   * @return the token url
   **/
  String tokenUrl() default "";

  /**
   * The URL to be used for obtaining refresh tokens. This must be in the form of a URL.  Applies to oauth2 type.
   * 
   * @return the refresh url
   **/
  String refreshUrl() default "";

  /**
   * The available scopes for the OAuth2 security scheme.  Applies to oauth2 type.
   * 
   * @return array of scopes
   **/
  OAuthScope[] scopes() default {};

}
