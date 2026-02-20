package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.model.MultipleBaseBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class SchemaPropertiesResource {

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
                                    schemaProperties = {
                                            @SchemaProperty(
                                                    name = "foo",
                                                    schema = @Schema(
                                                            type = "integer",
                                                            maximum = "1"
                                                    )
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            description = "boo",
                            content = @Content(
                                    schema = @Schema(
                                            type = "object",
                                            description = "various properties",
                                            maxProperties = 3
                                    ),
                                    mediaType = "application/json",
                                    schemaProperties = {
                                            @SchemaProperty(
                                                    name = "foo",
                                                    schema = @Schema(
                                                            type = "integer",
                                                            maximum = "1"
                                                    )
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "additionalProperties schema",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "object",
                                            maxProperties = 2
                                    ),
                                    additionalPropertiesSchema = @Schema(
                                            type = "string"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "additionalProperties boolean",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "object",
                                            maxProperties = 2,
                                            additionalProperties = Schema.AdditionalPropertiesValue.FALSE
                                    )
                            )
                    )
            }
    )
    public void getResponses() {
    }

    @GET
    @Path("/one")
    @Produces({MediaType.APPLICATION_JSON})
    public MultipleBaseBean requestBodySchemaPropertyNoSchema(
            @RequestBody(
                    content = @Content(
                            mediaType = "application/yaml",
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "foo",
                                            schema = @Schema(
                                                    type = "string"
                                            )
                                    )
                            }
                    )
            ) Object body) {
        return null;
    }

    @GET
    @Path("/two")
    @Produces({MediaType.APPLICATION_JSON})
    public MultipleBaseBean requestBodySchemaPropertySchema(
            @RequestBody(
                    content = @Content(
                            mediaType = "application/yaml",
                            schema = @Schema(
                                    type = "object",
                                    requiredProperties = {
                                            "foo"
                                    }
                            ),
                            schemaProperties= {
                                    @SchemaProperty(
                                            name = "foo",
                                            schema = @Schema(
                                                    type = "string"
                                            )
                                    )
                            }
                    )
            ) Object body) {
        return null;
    }

    @GET
    @Path("/three")
    @Produces({MediaType.APPLICATION_JSON})
    public MultipleBaseBean requestBodySchemaPropertySchemaArray(
            @RequestBody(
                    content = @Content(
                            mediaType = "application/yaml",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            type = "object",
                                            requiredProperties = {
                                                    "foo"
                                            }
                                    )
                            ),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "foo",
                                            schema = @Schema(
                                                    type = "string"
                                            )
                                    )
                            }
                    )
            ) Object body) {
        return null;
    }
}
