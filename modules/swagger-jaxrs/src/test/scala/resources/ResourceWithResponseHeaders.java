package resources;

import models.*;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(value = "/basic", description = "Basic resource")
@Produces({"application/xml"})
public class ResourceWithResponseHeaders {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    response = Sample.class,
    position = 0,
    responseHeaders = {
      @ResponseHeader(name = "foo", description = "description", response = String.class)
    })
  @ApiResponses({
    @ApiResponse(code = 400, message = "Invalid ID",
      response = NotFoundModel.class,
      responseHeaders = @ResponseHeader(name = "X-Rack-Cache", description = "Explains whether or not a cache was used", response = Boolean.class)),
    @ApiResponse(code = 404, message = "object not found")})
  public Response getTest(
      @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")
      @DefaultValue("5")
      @PathParam("id") String id,
      @QueryParam("limit") Integer limit
      ) throws WebApplicationException {
    Sample out = new Sample();
    out.setName("foo");
    out.setValue("bar");
    return Response.ok().entity(out).build();
  }
}