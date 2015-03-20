package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api(value = "/external/info/")
public class RegexPathParamResource {
  @GET
  @ApiOperation(value="this", tags="tag1")
  @Path("/{report_type:[aA-zZ]+}")
  public Response getThis(@ApiParam(value = "test") @PathParam("report_type") String param) {
    return Response.ok().build();
  }
}
