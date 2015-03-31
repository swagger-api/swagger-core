package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api(value = "/external/info/", hidden = true)
@Path("fun")
public class HiddenResource {
  @GET
  @ApiOperation(value="this", tags="tag1")
  @Path("/this")
  public Response getThis(@ApiParam(value = "test") ArrayList<String> tenantId) {
    return Response.ok().build();
  }
}