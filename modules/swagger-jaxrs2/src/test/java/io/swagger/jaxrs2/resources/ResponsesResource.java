package io.swagger.jaxrs2.resources;


import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.links.Link;
import io.swagger.oas.annotations.links.LinkParameters;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by RafaelLopez on 5/16/17.
 */
public class ResponsesResource {

    @GET
    @Path("/")
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
    @Link(description = "Link Description",
            name = "Link Name",
            operationId = "Operation Id",
            operationRef = "Operation Ref",
            parameters =
            @LinkParameters(
                    name = "Link Parameter",
                    expression = "Link Expression"
            )
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
