package io.swagger.v3.jaxrs2.resources.generics.ticket3149;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface SecondEndpoint<C> extends OriginalEndpoint<C> {

    @GET
    @Path("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")})
    default String secondEnpoint(C param) {
        return "secondEnpoint-ok";
    }

    @Override
    @GET
    @Path("/original/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")})
    default String originalEndpoint(C param) {
        return "originalEndpoint-ok";
    }


}
