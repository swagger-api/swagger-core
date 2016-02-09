package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.NotFoundModel;
import io.swagger.models.Sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Api(value = "/basic", description = "Basic resource")
@Produces({"application/xml"})
@Path("/")
public class ResourceWithRanges {
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get object by ID",
            httpMethod = "GET",
            notes = "No details provided",
            response = Sample.class,
            position = 0)
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid ID", response = NotFoundModel.class),
            @ApiResponse(code = 404, message = "object not found")})
    public Response getTest(
            @ApiParam(value = "sample param data", defaultValue = "5", allowableValues = "range[0,10]")
            @PathParam("id") Integer id,
            @ApiParam(value = "sample positive infinity data", allowableValues = "range[0, infinity]")
            @PathParam("minValue") Double minValue,
            @ApiParam(value = "sample negative infinity data", allowableValues = "range[-infinity, 100]")
            @PathParam("maxValue") Integer maxValue,
            @ApiParam(value = "sample array data", allowMultiple = true, allowableValues = "range(0, 5)")
            @PathParam("values") Integer values
    ) throws WebApplicationException {
        Sample out = new Sample();
        out.setName("foo");
        out.setValue("bar");
        return Response.ok().entity(out).build();
    }
}
