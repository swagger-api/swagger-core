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
 * Describes an operation or typically a HTTP method against a specific path.
 * <p>
 * Operations with equivalent paths are grouped in a single Operation Object.
 * A combination of a HTTP method and a path creates a unique operation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiOperation {
    /**
     * Corresponds to the `summary` field of the operation.
     * <p>
     * Provides a brief description of this operation. Should be 120 characters or less
     * for proper visibility in Swagger-UI.
     */
    String value();

    /**
     * Corresponds to the 'notes' field of the operation.
     * <p>
     * A verbose description of the operation.
     */
    String notes() default "";

    /**
     * A list of tags for API documentation control.
     * <p>
     * Tags can be used for logical grouping of operations by resources or any other qualifier.
     * A non-empty value will override the value received from {@link Api#value()} or {@link Api#tags()}
     * for this operation.
     *
     * @since 1.5.2-M1
     */
    String[] tags() default "";

    /**
     * The response type of the operation.
     * <p>
     * In JAX-RS applications, the return type of the method would automatically be used, unless it is
     * {@code javax.ws.rs.core.Response}. In that case, the operation return type would default to `void`
     * as the actual response type cannot be known.
     * <p>
     * Setting this property would override any automatically-derived data type.
     * <p>
     * If the value used is a class representing a primitive ({@code Integer}, {@code Long}, ...)
     * the corresponding primitive type will be used.
     */
    Class<?> response() default Void.class;

    /**
     * Declares a container wrapping the response.
     * <p>
     * Valid values are "List", "Set" or "Map". Any other value will be ignored.
     */
    String responseContainer() default "";

    /**
     * Specifies a reference to the response type. The specified reference can be either local or remote and
     * will be used as-is, and will override any specified response() class.
     */

    String responseReference() default "";

    /**
     * Corresponds to the `method` field as the HTTP method used.
     * <p>
     * If not stated, in JAX-RS applications, the following JAX-RS annotations would be scanned
     * and used: {@code @GET}, {@code @HEAD}, {@code @POST}, {@code @PUT}, {@code @DELETE} and {@code @OPTIONS}.
     * Note that even though not part of the JAX-RS specification, if you create and use the {@code @PATCH} annotation,
     * it will also be parsed and used. If the httpMethod property is set, it will override the JAX-RS annotation.
     * <p>
     * For Servlets, you must specify the HTTP method manually.
     * <p>
     * Acceptable values are "GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS" and "PATCH".
     */
    String httpMethod() default "";

    /**
     * Not used in 1.5.X, kept for legacy support.
     */
    @Deprecated int position() default 0;

    /**
     * Corresponds to the `operationId` field.
     * <p>
     * The operationId is used by third-party tools to uniquely identify this operation. In Swagger 2.0, this is
     * no longer mandatory and if not provided will remain empty.
     */
    String nickname() default "";

    /**
     * Corresponds to the `produces` field of the operation.
     * <p>
     * Takes in comma-separated values of content types.
     * For example, "application/json, application/xml" would suggest this operation
     * generates JSON and XML output.
     * <p>
     * For JAX-RS resources, this would automatically take the value of the {@code @Produces}
     * annotation if such exists. It can also be used to override the {@code @Produces} values
     * for the Swagger documentation.
     */
    String produces() default "";

    /**
     * Corresponds to the `consumes` field of the operation.
     * <p>
     * Takes in comma-separated values of content types.
     * For example, "application/json, application/xml" would suggest this API Resource
     * accepts JSON and XML input.
     * <p>
     * For JAX-RS resources, this would automatically take the value of the {@code @Consumes}
     * annotation if such exists. It can also be used to override the {@code @Consumes} values
     * for the Swagger documentation.
     */
    String consumes() default "";

    /**
     * Sets specific protocols (schemes) for this operation.
     * <p>
     * Comma-separated values of the available protocols. Possible values: http, https, ws, wss.
     *
     * @return the protocols supported by the operations under the resource.
     */
    String protocols() default "";

    /**
     * Corresponds to the `security` field of the Operation Object.
     * <p>
     * Takes in a list of the authorizations (security requirements) for this operation.
     *
     * @return an array of authorizations required by the server, or a single, empty authorization value if not set.
     * @see Authorization
     */
    Authorization[] authorizations() default @Authorization(value = "");

    /**
     * Hides the operation from the list of operations.
     */
    boolean hidden() default false;

    /**
     * A list of possible headers provided alongside the response.
     *
     * @return a list of response headers.
     */
    ResponseHeader[] responseHeaders() default @ResponseHeader(name = "", response = Void.class);

    /**
     * The HTTP status code of the response.
     * <p>
     * The value should be one of the formal <a target="_blank" href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html">HTTP Status Code Definitions</a>.
     */
    int code() default 200;

    /**
     * @return an optional array of extensions
     */

    Extension[] extensions() default @Extension(properties = @ExtensionProperty(name = "", value = ""));
}
