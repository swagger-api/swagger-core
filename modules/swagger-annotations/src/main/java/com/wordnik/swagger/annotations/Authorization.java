/**
 *  Copyright 2013 Wordnik, Inc.
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
 * Declares an authorization scheme to be used on a resource or an operation.
 * <p/>
 * The authorization scheme used needs to be defined in the Resource Listing's authorization
 * section.
 * <p/>
 * This annotation is not used directly and will not be parsed by Swagger. It should be used
 * within either {@link com.wordnik.swagger.annotations.Api} or {@link com.wordnik.swagger.annotations.ApiOperation}.
 *
 * @see com.wordnik.swagger.annotations.ApiOperation
 * @see com.wordnik.swagger.annotations.Api
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorization {
    /**
     * The name of the authorization scheme to be used on this resource/operation.
     * <p/>
     * The name must be defined in the Resource Listing's authorization section,
     */
    String value();

    /**
     * The scopes to be used if the authorization scheme is OAuth2.
     *
     * @see com.wordnik.swagger.annotations.AuthorizationScope
     */
    AuthorizationScope[] scopes() default @AuthorizationScope(scope = "", description = "");
}