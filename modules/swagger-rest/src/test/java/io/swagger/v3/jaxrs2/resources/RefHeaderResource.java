package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Class with Links
 */
public class RefHeaderResource {
    @Operation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with no inputs and a complex output",
            operationId = "getWithPayloadResponse",
            deprecated = true,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "voila!",
                            headers = {@Header(
                                    name = "Rate-Limit-Limit",
                                    description = "The number of allowed requests in the current period",
                                    ref = "Header",
                                    schema = @Schema(type = "integer"))})
            })
    @GET
    @Path("/path")
    public void simpleGet() {
    }

}
