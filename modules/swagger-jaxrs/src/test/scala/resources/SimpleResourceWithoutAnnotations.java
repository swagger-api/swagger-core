package resources;

import io.swagger.annotations.ApiParam;
import models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Produces({"application/xml"})
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
    @ApiParam(value = "sample param data", required = true)Sample sample,
    @HeaderParam(value = "Authorization") String authorization,
    @QueryParam(value = "dateUpdated") java.util.Date dateUpdated,
    @CookieParam(value = "X-your-cookie") String cookieId) {
    return Response.ok().build();
  }
}