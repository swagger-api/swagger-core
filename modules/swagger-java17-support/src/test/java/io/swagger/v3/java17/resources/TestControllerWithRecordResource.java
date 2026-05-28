package io.swagger.v3.java17.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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

