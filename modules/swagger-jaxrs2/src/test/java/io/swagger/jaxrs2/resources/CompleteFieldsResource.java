package io.swagger.jaxrs2.resources;

import io.swagger.oas.annotations.ExternalDocumentation;
import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.enums.ParameterIn;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


public class CompleteFieldsResource {

    @GET
    @Path("/")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description",
            tags = {"Example Tag", "Second Tag"},
            externalDocs =
            @ExternalDocumentation(
                    description = "External documentation description",
                    url = "http://url.com"
            ),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
                            required = true, description = "parameter description",
                            allowEmptyValue = true, allowReserved = true,
                            schema = @Schema(
                                    type = "string",
                                    format = "uuid",
                                    description = "the generated UUID",
                                    readOnly = true)
                    )},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "voila!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponsesResource.SampleResponseSchema.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "default",
                            description = "boo",
                            content = @Content(
                                    mediaType = "*/*",
                                    schema = @Schema(implementation = ResponsesResource.GenericError.class)
                            )
                    )
            }
    )
    public Response getSummaryAndDescription() {
        return Response.ok().entity("ok").build();
    }

}
