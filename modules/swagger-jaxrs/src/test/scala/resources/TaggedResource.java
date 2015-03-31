package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api(value = "/external/info/", tags = {"external_info","user_info"})
@Path("external/info/")
public class TaggedResource {
  @ApiOperation(value="test")
  @GET
  public Response.Status getTest(@ApiParam(value = "test") ArrayList<String> tenantId) {
    return Response.Status.OK;
  }
}