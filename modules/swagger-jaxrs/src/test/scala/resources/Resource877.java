package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api(value = "/external/info/", description = "it's an api")
@Path("external/info/")
public class Resource877 {
  @ApiOperation(value="test.")
  @GET
  public Response.Status getTest(@ApiParam(value = "test") ArrayList<String> tenantId) {
    return Response.Status.OK;
  }
}