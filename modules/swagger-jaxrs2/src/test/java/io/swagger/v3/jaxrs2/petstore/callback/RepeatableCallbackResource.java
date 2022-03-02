/**
 * Copyright 2021 SmartBear Software
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

package io.swagger.v3.jaxrs2.petstore.callback;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.callbacks.Callback;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class RepeatableCallbackResource {
    @Callback(name = "testCallback", operation =
    @Operation(), callbackUrlExpression = "http://$requests.query.url")
    @Callback(
            name = "testCallback1",
            operation = @Operation(
                    operationId = "getAllReviews",
                    summary = "get all the reviews",
                    method = "get",
                    responses = @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "integer",
                                            format = "int32")))),
            callbackUrlExpression = "http://www.url.com")
    @Callback(name = "testCallback2", operation =
    @Operation(),
            callbackUrlExpression = "http://$request.query.url")
    @Operation(
            summary = "Simple get operation",
            operationId = "getWithNoParameters",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "voila!")
            })
    @GET
    @Path("/repeatablecallback")
    public String simpleGet() {
        return null;
    }
}