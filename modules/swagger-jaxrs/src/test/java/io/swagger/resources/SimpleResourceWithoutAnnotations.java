package io.swagger.resources;

import io.swagger.annotations.ApiParam;
import io.swagger.models.Sample;

import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Produces({"application/xml"})
@Path("/")
public class SimpleResourceWithoutAnnotations {
    @GET
    @Path("/{id}")
    public Sample getTest(
            @DefaultValue("5")
            @PathParam("id") String id,
            @QueryParam("limit") Integer limit
    ) throws WebApplicationException {
        Sample out = new Sample();
        out.setName("foo");
        out.setValue("bar");
        return out;
    }

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