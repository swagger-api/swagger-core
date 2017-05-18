package io.swagger.jaxrs2.resources;


import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by RafaelLopez on 5/16/17.
 */
public class ApiExampleResource {
    @GET
    @Path("/")
    @Operation(summary = "Operation Summary", description = "Operation Description")
    public Response getSummaryAndDescription() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/")
    @Operation(deprecated = true)
    public Response deprecatedMethod() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Operation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with no inputs and a complex output object",
            operationId = "getWithPayloadResponse",
            deprecated = true,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "voila!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SampleResponseSchema.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "default",
                            description = "boo",
                            content = @Content(
                                    mediaType = "*/*",
                                    schema = @Schema(implementation = GenericError.class)
                            )
                    )
            }
    )
    public void getResponses() {
    }

    static class SampleResponseSchema {
        @Schema(description = "the user id")
        private String id;
    }

    static class GenericError {
        private int code;
        private String message;
    }




}
