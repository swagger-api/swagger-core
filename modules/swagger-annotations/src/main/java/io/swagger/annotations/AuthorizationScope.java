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
 * Describes an OAuth2 authorization scope.
 * <p>
 * Used to define an authorization scope that is used by an operation for
 * a defined authorization scheme.
 * <p>
 * This annotation is not used directly and will not be parsed by Swagger. It should be used
 * within the {@link Authorization}.
 *
 * @see Authorization
 * @see ApiOperation
 * @see Api
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizationScope {
    /**
     * The scope of the OAuth2 Authorization scheme to be used.
     * <p>
     * The scope should be previously declared in the Swagger Object's securityDefinition section.
     */
    String scope();

    /**
     * Not used in 1.5.X, kept for legacy support.
     */
    String description();
}
