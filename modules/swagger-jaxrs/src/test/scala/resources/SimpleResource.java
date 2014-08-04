package resources;

import models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.*;

@Api(value = "/basic", description = "Basic resource")
@Produces({"application/xml"})
public class SimpleResource {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    response = Sample.class,
    position = 0)
  @ApiResponses({
    @ApiResponse(code = 400, message = "Invalid ID", response = NotFoundModel.class),
    @ApiResponse(code = 404, message = "object not found")})
  public Response getTest(
      @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")
      @DefaultValue("1")
      @PathParam("id") String id,
      @QueryParam("limit") Integer limit
      ) throws WebApplicationException {
    Sample out = new Sample();
    out.setName("foo");
    out.setValue("bar");
    return Response.ok().entity(out).build();
  }

  @PUT
  @Path("/{id}")
  @ApiOperation(value = "Update by ID",
    notes = "No details provided",
    position = 1)
  @ApiResponses({
    @ApiResponse(code = 400, message = "Invalid ID"),
    @ApiResponse(code = 404, message = "object not found")})
  public Response updateTest(
    @ApiParam(value = "sample param data", required = true)Sample sample,
    @HeaderParam(value = "Authorization") String authorization,
    @QueryParam(value = "dateUpdated") java.util.Date dateUpdated,
    @CookieParam(value = "X-your-cookie") String cookieId) {
    return Response.ok().build();
  }
}