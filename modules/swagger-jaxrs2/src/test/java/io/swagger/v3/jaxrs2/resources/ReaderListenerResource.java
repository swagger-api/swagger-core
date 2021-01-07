package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.ReaderListener;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.integration.api.OpenApiReader;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

public class ReaderListenerResource implements ReaderListener {

    @GET
    @Path("/")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response getSummaryAndDescription(@QueryParam("subscriptionId") @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            schema = @Schema(
                    type = "string",
                    format = "uuid",
                    description = "the generated UUID",
                    accessMode = Schema.AccessMode.READ_ONLY)
    ) String subscriptionId,
                                             @QueryParam("description") String description) {
        return Response.ok().entity("ok").build();
    }

    @Override
    public void beforeScan(OpenApiReader reader, OpenAPI openAPI) {
        openAPI.addTagsItem(new Tag().name("Tag-added-before-read"));
    }

    @Override
    public void afterScan(OpenApiReader reader, OpenAPI openAPI) {
        openAPI.addTagsItem(new Tag().name("Tag-added-after-read"));
    }
}
