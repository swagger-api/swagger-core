package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.Sample;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Api(value = "/basic", description = "Basic resource")
@Produces({"application/xml"})
@Path("/")
public class ResourceWithValidation {

    @GET
    @Path("/swagger-and-303")
    @ApiOperation(value = "Get",
            httpMethod = "GET")
    public Response getTestSwaggerAnd303(
            @ApiParam(value = "sample param data", required = false, allowableValues = "range[7, infinity]")
            @QueryParam("id") @NotNull @Min(5) Integer id) throws WebApplicationException {
        Sample out = new Sample();
        out.setName("foo");
        out.setValue("bar");
        return Response.ok().entity(out).build();
    }

    @GET
    @Path("/swagger")
    @ApiOperation(value = "Get",
            httpMethod = "GET")
    public Response getTestSwagger(
            @ApiParam(value = "sample param data", required = true, allowableValues = "range[7, infinity]")
            @QueryParam("id") Integer id) throws WebApplicationException {
        Sample out = new Sample();
        out.setName("foo");
        out.setValue("bar");
        return Response.ok().entity(out).build();
    }

    @GET
    @Path("/303")
    @ApiOperation(value = "Get",
            httpMethod = "GET")
    public Response getTest303(
            @ApiParam(value = "sample param data")
            @QueryParam("id") @NotNull @Min(10) Integer id) throws WebApplicationException {
        Sample out = new Sample();
        out.setName("foo");
        out.setValue("bar");
        return Response.ok().entity(out).build();
    }
}
