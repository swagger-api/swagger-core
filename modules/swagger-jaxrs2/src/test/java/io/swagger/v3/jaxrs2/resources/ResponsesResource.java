package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.swagger.v3.jaxrs2.resources.model.MultipleBaseBean;
import io.swagger.v3.jaxrs2.resources.model.MultipleSub1Bean;
import io.swagger.v3.jaxrs2.resources.model.MultipleSub2Bean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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

    @GET
    @Path("/oneOf")
    @Operation(summary = "Test inheritance / polymorphism",
            responses = {
                    @ApiResponse(description = "bean answer",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            oneOf = { MultipleSub1Bean.class, MultipleSub2Bean.class }
                                    )
                            )
                    )
            })
    @Produces({ MediaType.APPLICATION_JSON })
    public MultipleBaseBean getOneOf(
            @Parameter(description = "Test inheritance / polymorphism",
                    required = true,
                    example = "1")
            @QueryParam("number") final int beanNumber) {

        return null;
    }

    @GET
    @Path("/anyOf")
    @Operation(summary = "Test inheritance / polymorphism",
            responses = {
                    @ApiResponse(description = "bean answer",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            anyOf = { MultipleSub1Bean.class, MultipleSub2Bean.class }
                                    )
                            )
                    )
            })
    @Produces({ MediaType.APPLICATION_JSON })
    public MultipleBaseBean getAnyOf(
            @Parameter(description = "Test inheritance / polymorphism",
                    required = true,
                    example = "1")
            @QueryParam("number") final int beanNumber) {

        return null;
    }

    @GET
    @Path("/allOf")
    @Operation(summary = "Test inheritance / polymorphism",
            responses = {
                    @ApiResponse(description = "bean answer",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            allOf = { MultipleSub1Bean.class, MultipleSub2Bean.class }
                                    )
                            )
                    )
            })
    @Produces({ MediaType.APPLICATION_JSON })
    public MultipleBaseBean getAllOf(
            @Parameter(description = "Test inheritance / polymorphism",
                    required = true,
                    example = "1")
            @QueryParam("number") final int beanNumber) {

        return null;
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
