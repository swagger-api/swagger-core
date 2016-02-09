package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.NotFoundModel;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Api(value = "/basic", description = "Basic resource")
@Path("/")
public class ResourceWithVoidReturns {
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get object by ID",
            notes = "No details provided")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid ID", response = NotFoundModel.class),
            @ApiResponse(code = 404, message = "object not found")})
    public Response getTest(
            @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]", defaultValue = "2")
            @DefaultValue("1")
            @PathParam("id") String id,
            @QueryParam("limit") Integer limit
    ) throws WebApplicationException {
        return Response.ok().build();
    }
}
