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
 * A wrapper to allow a list of multiple {@link com.wordnik.swagger.annotations.ApiResponse} objects.
 * <p/>
 * If you need to describe a single {@link com.wordnik.swagger.annotations.ApiResponse}, you still
 * must use this annotation and wrap the {@code @ApiResponse} in an array.
 *
 * @see com.wordnik.swagger.annotations.ApiResponse
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResponses {
    /**
     * A list of {@link com.wordnik.swagger.annotations.ApiResponse}s provided by the API operation.
     */
    ApiResponse[] value();
}
