package resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import models.*;

import javax.ws.rs.*;

import java.util.*;

@Api(value = "/basic", description = "Basic resource")
public class ResourceWithBeanParams {
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
      @BeanParam Pagination pagination
      ) throws WebApplicationException {
    return new HashMap<String, Integer>();
  }
}
