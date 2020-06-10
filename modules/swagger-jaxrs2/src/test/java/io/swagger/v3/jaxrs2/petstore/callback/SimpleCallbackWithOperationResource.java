package io.swagger.v3.jaxrs2.petstore.callback;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.callbacks.Callback;
import io.swagger.v3.oas.annotations.callbacks.Callbacks;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class SimpleCallbackWithOperationResource {
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
                    callbackUrlExpression = "http://www.url.com")
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
    @Path("/simplecallback")
    public String simpleGet() {
        return null;
    }
}
