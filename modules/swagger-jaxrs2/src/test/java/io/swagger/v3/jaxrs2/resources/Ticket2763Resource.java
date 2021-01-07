package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

public class Ticket2763Resource {

    @GET
    @Path("/array")
    @ApiResponse(content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(
                    ref="https://openebench.bsc.es/monitor/tool/tool.json"))))
    public void getArrayResponses() {
    }

    @GET
    @Path("/schema")
    @ApiResponse(content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(ref="https://openebench.bsc.es/monitor/tool/tool.json")))
    public void getSchemaResponses() {
    }
}
