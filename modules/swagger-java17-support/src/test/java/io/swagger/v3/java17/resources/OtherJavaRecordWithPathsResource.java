package io.swagger.v3.java17.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("sample2")
public record OtherJavaRecordWithPathsResource() {
    @GET
    @Path("/")
    @Operation(operationId = "Operation Id",
            description = "description")
    @SecurityRequirement(name = "security_key",
            scopes = {"write:pets", "read:pets"}
    )
    public void getSecurity() {
    }

    @GET
    @Path("/2")
    @Operation(operationId = "Operation Id 2",
            description = "description 2")
    @SecurityRequirement(name = "security_key2",
            scopes = {"write:pets", "read:pets"}
    )
    public void getSecurity2() {
    }
}
