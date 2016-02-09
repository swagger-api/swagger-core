package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.EnumHolder;
import io.swagger.models.NotFoundModel;
import io.swagger.models.Sample;
import io.swagger.models.TestEnum;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Api(value = "/basic", description = "Basic resource")
@Produces({"application/xml"})
@Path("/")
public class ResourceWithEnums {
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
            @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")
            @DefaultValue("5")
            @PathParam("id") String id,
            @QueryParam("limit") Integer limit,
            @ApiParam(value = "sample query data", required = true, allowableValues = "a,b,c,d,e")
            @QueryParam("allowable") String allowable
    ) throws WebApplicationException {
        Sample out = new Sample();
        out.setName("foo");
        out.setValue("bar");
        return Response.ok().entity(out).build();
    }

    @GET
    @Path("/checkEnumHandling/{v0}")
    @ApiOperation(value = "Checks enum handling", response = EnumHolder.class)
    public EnumHolder checkEnumHandling(
            @PathParam("v0") TestEnum value0,
            @QueryParam("v1") TestEnum[] value1,
            @QueryParam("v2") Collection<TestEnum> value2,
            @ApiParam(value = "Enum value with allowed values", allowableValues = "A,B,C") @QueryParam("v3") TestEnum value3,
            @ApiParam(value = "Array of strings with allowed values", allowableValues = "D,E,F") @QueryParam("v4") String[] value4,
            @ApiParam(value = "Collection of strings with allowed values", allowableValues = "G,H,I") @QueryParam("v5") Collection<String> value5) {
        return null;
    }
}