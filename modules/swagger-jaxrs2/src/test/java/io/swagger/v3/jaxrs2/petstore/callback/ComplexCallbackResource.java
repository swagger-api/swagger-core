package io.swagger.v3.jaxrs2.petstore.callback;

import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.callbacks.Callback;
import io.swagger.v3.oas.annotations.callbacks.Callbacks;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

public class ComplexCallbackResource {
    @Callbacks({
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
                    callbackUrlExpression = "http://www.url.com"),
            @Callback(
                    name = "testCallback2",
                    operation = @Operation(
                            operationId = "getAnSpecificReviews",
                            summary = "get a review",
                            method = "get",
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "successful operation",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = User.class)))),
                    callbackUrlExpression = "http://www.url2.com")
    })
    @Operation(
            summary = "Simple get operation",
            operationId = "getWithNoParameters",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "voila!")
            })
    @GET
    @Path("/complexcallback")
    public String simpleGet(@Parameter(description = "idParam", schema = @Schema(implementation = User.class))
                            @QueryParam("id") final String id) {
        return null;
    }
}
