package resources;

import models.*;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api(value = "/basic", description = "Basic resource")
public class ResourceWithMapReturnValue {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided"/*,
    response = String.class,
    responseContainer = "Map"*/)
  @ApiResponses({
    @ApiResponse(code = 400, message = "Invalid ID", response = NotFoundModel.class),
    @ApiResponse(code = 404, message = "object not found")})
  public Map<String, Integer> getTest(
      @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")
      @DefaultValue("5")
      @PathParam("id") String id,
      @QueryParam("limit") Integer limit
      ) throws WebApplicationException {
    return new HashMap<String, Integer>();
  }
}
