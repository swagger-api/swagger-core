package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api(value = "/external/info/", tags = @Tags(value = {
  @Tag(value = "external_info", description = "all external info for this API"),
  @Tag(value = "user_info",
    description = "info for all users",
    externalDocs = @ExternalDocs(value="see here", url = "http://swagger.io"))
}))
@Path("external/info/")
public class TaggedResource {
  @ApiOperation(value="test")
  @GET
  public Response.Status getTest(@ApiParam(value = "test") ArrayList<String> tenantId) {
    return Response.Status.OK;
  }
}