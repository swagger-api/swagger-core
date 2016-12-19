package io.swagger.resources;

import io.swagger.annotations.ApiParam;
import io.swagger.models.Sample;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Produces({"application/xml"})
@Path("/")
public class SimpleResourceInheritanceSubWithoutAnnotations extends SimpleResourceInheritanceBaseWithoutAnnotations<Sample> {

    @GET
    @Path("/{id}/value")
    @Produces({"text/plain"})
    public Response getStringValue() throws WebApplicationException {
        return Response.ok().entity("ok").build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTest(
            @ApiParam(value = "sample param data", required = true) Sample sample,
            @HeaderParam(value = "Authorization") String authorization,
            @QueryParam(value = "dateUpdated") java.util.Date dateUpdated,
            @CookieParam(value = "X-your-cookie") String cookieId) {
        return Response.ok().build();
    }
}