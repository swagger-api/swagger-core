package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

public class SchemaAdditionalPropertiesResource {

    static class Pet {
        public String foo;
    }
    @GET
    @Path("/fromtResponseType")
    public Map<String, List<Pet>> fromtResponseType() {
        return null;
    }

    @GET
    @Path("/schemaNotImpl")
    @Operation(
            operationId = "schemaNotImpl",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "voila!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "object"
                                    ),
                                    additionalPropertiesSchema = @Schema(
                                            ref = "#/components/schemas/Pet"
                                    )
                            )
                    )
            }
    )
    public Response schemaNotImpl() {
        return null;
    }

    @GET
    @Path("/schemaImpl")
    @Operation(
            operationId = "schemaImpl",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "voila!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "object",
                                            additionalPropertiesSchema = Pet.class
                                    )
                            )
                    )
            }
    )
    public Response schemaImpl() {
        return null;
    }

    @GET
    @Path("/arraySchemaImpl")
    @Operation(
            operationId = "arraySchemaImpl",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "voila!",
                            content = @Content(
                                    mediaType = "application/json",
                                    additionalPropertiesArraySchema = @ArraySchema(
                                            schema = @Schema(
                                                        implementation = Pet.class
                                                    )
                                    ),
                                    schema = @Schema(
                                            type = "object"
                                    )
                            )
                    )
            }
    )
    public Response arraySchemaImpl() {
        return null;
    }

}
