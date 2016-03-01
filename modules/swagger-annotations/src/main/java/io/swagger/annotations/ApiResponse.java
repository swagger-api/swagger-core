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
 * Describes a possible response of an operation.
 * <p>
 * This can be used to describe possible success and error codes from your REST API call.
 * You may or may not use this to describe the return type of the operation (normally a
 * successful code), but the successful response should be described as well using the
 * {@link ApiOperation}.
 * <p>
 * This annotation can be applied at method or class level; class level annotations will
 * be parsed only if an @ApiResponse annotation with the same code is not defined at method
 * level or in thrown Exception
 * <p>
 * If your API has uses a different response class for these responses, you can describe them
 * here by associating a response class with a response code.
 * Note, Swagger does not allow multiple response types for a single response code.
 * <p>
 * This annotation is not used directly and will not be parsed by Swagger. It should be used
 * within the {@link ApiResponses}.
 *
 * @see ApiOperation
 * @see ApiResponses
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResponse {
    /**
     * The HTTP status code of the response.
     * <p>
     * The value should be one of the formal <a target="_blank" href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html">HTTP Status Code Definitions</a>.
     */
    int code();

    /**
     * Human-readable message to accompany the response.
     */
    String message();

    /**
     * Optional response class to describe the payload of the message.
     * <p>
     * Corresponds to the `schema` field of the response message object.
     */
    Class<?> response() default Void.class;

    /**
     * Specifies a reference to the response type. The specified reference can be either local or remote and
     * will be used as-is, and will override any specified response() class.
     */

    String reference() default "";

    /**
     * A list of possible headers provided alongside the response.
     *
     * @return a list of response headers.
     */
    ResponseHeader[] responseHeaders() default @ResponseHeader(name = "", response = Void.class);

    /**
     * Declares a container wrapping the response.
     * <p>
     * Valid values are "List", "Set" or "Map". Any other value will be ignored.
     */
    String responseContainer() default "";
}
