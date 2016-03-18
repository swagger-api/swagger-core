package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.Sample;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Api(value = "/basicApiResponse", description = "Basic resource")
@Produces({"application/xml"})
@Path("/")
@ApiResponses({
        @ApiResponse(code = 409, message = "Class level"),
        @ApiResponse(code = 403, message = "Forbidden class level")
})
public class ResourceWithClassLevelApiResourceNoMethodLevelApiResources {

    @GET
    @Path("/{id}")
    public Response getTest(
            @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")
            @DefaultValue("5")
            @PathParam("id") String id,
            @QueryParam("limit") Integer limit
    ) {
        return Response.ok().build();
    }


    @PUT
    @Path("/{id}")
    public Response putTest(
            @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")
            @DefaultValue("5")
            @PathParam("id") String id,
            Sample sample
    ) {
        return Response.ok().build();
    }

}
