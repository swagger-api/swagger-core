package resources;

import models.*;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(value = "/basic", description = "Basic resource")
public class ResourceWithVoidReturns {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided")
  @ApiResponses({
    @ApiResponse(code = 400, message = "Invalid ID", response = NotFoundModel.class),
    @ApiResponse(code = 404, message = "object not found")})
  public Response getTest(
      @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]", defaultValue = "2")
      @DefaultValue("1")
      @PathParam("id") String id,
      @QueryParam("limit") Integer limit
      ) throws WebApplicationException {
    return Response.ok().build();
  }
}
