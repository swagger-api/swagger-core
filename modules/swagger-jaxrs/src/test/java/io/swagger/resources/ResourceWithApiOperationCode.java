package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.models.NotFoundModel;
import io.swagger.models.Sample;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Api
@Path("/")
public class ResourceWithApiOperationCode {
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get object by ID",
            notes = "No details provided",
            response = Sample.class,
            position = 0,
            code = 202,
            responseHeaders = {
                    @ResponseHeader(name = "foo", description = "description", response = String.class)
            })
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid ID",
                    response = NotFoundModel.class,
                    responseHeaders = @ResponseHeader(name = "X-Rack-Cache", description = "Explains whether or not a cache was used", response = Boolean.class)),
            @ApiResponse(code = 404, message = "object not found")})
    public Response getTest() {
        return Response.ok().entity("out").build();
    }

    @PUT
    @Path("/{id}")
    @ApiOperation(value = "Get object by ID",
            notes = "No details provided",
            response = Sample.class,
            position = 0,
            responseHeaders = {
                    @ResponseHeader(name = "foo", description = "description", response = String.class)
            })
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized",
                    response = NotFoundModel.class,
                    responseHeaders = @ResponseHeader(name = "X-Rack-Cache", description = "Explains whether or not a cache was used", response = Boolean.class)),
            @ApiResponse(code = 405, message = "Method Not Allowed")})
    public Response putTest() {
        return Response.ok().entity("out").build();
    }
}
