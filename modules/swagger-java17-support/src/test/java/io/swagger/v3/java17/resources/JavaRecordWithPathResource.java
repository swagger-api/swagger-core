package io.swagger.v3.java17.resources;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("sample")
public record JavaRecordWithPathResource() {

    @POST
    @Path("/1")
    @Operation(description = "description 1", operationId = "id 1")
    public void postExample(){

    }

    @POST
    @Path("/2")
    @Operation(description = "description 2", operationId = "id 2")
    public void postExample2(){

    }
}
