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

import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows configuration of the supported OAuth Flows.
 **/
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OAuthFlows {
    /**
     * Configuration for the OAuth Implicit flow.
     *
     * @return OAuthFlow implicit
     **/
    OAuthFlow implicit() default @OAuthFlow();

    /**
     * Configuration for the OAuth Resource Owner Password flow.
     *
     * @return OAuthFlow password
     **/
    OAuthFlow password() default @OAuthFlow();

    /**
     * Configuration for the OAuth Client Credentials flow.
     *
     * @return OAuthFlow clientCredentials
     **/
    OAuthFlow clientCredentials() default @OAuthFlow();

    /**
     * Configuration for the OAuth Authorization Code flow.
     *
     * @return OAuthFloe authorizationCode
     **/
    OAuthFlow authorizationCode() default @OAuthFlow();

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

}
