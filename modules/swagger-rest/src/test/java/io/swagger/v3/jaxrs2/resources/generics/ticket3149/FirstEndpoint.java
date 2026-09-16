package io.swagger.v3.jaxrs2.resources.generics.ticket3149;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

public interface FirstEndpoint<C> {

    @POST
    @Path("/")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")})
    default String firstEndpoint(C param) {
        return "firstEndpoint-ok";
    }
}
