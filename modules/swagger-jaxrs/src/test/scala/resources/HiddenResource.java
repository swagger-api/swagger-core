package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api(value = "/external/info/", hidden = true)
@Path("fun")
public class HiddenResource {
  @ApiOperation(value="this", tags={@Tag(value = "tag1", description = "the tag 1")})
  @GET
  @Path("/this")
  public Response getThis(@ApiParam(value = "test") ArrayList<String> tenantId) {
    return Response.ok().build();
  }
}