package io.swagger.v3.java17.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/v17")
public class TestControllerWithRecordResource {

    @POST
    @Operation(
            operationId = "opsRecordID",
            responses = @ApiResponse(description = "Successful operation",
                    content = @Content(mediaType = "application/json",schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = JavaRecordResource.class))
            )
    )
    @Consumes({"application/json", "application/xml"})
    public void postRecord(){}

}

