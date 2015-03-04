package resources;

import models.*;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(value = "/basic", description = "Basic resource")
@Produces({"application/xml"})
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
}