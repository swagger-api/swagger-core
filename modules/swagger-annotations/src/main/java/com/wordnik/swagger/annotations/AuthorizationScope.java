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
 * Describes an OAuth2 authorization scope.
 * <p/>
 * Used to declare an authorization scope that is used by a resource or an operation for
 * a defined authorization scheme.
 * <p/>
 * This annotation is not used directly and will not be parsed by Swagger. It should be used
 * within the {@link com.wordnik.swagger.annotations.Authorization}.
 *
 * @see com.wordnik.swagger.annotations.Authorization
 * @see com.wordnik.swagger.annotations.ApiOperation
 * @see com.wordnik.swagger.annotations.Api
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizationScope {
    /**
     * The scope of the OAuth2 Authorization scheme to be used.
     * <p/>
     * The scope should be previously defined in the Resource Listing's authorization section.
     */
    String scope();

    /**
     * A brief description of the scope.
     */
    String description();
}
