package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api(value = "/external/info/", description = "it's an api")
@Path("external/info/{id}")
public class Resource1085 {
  @ApiOperation(value="test")
  @GET
  public Response getTest(@PathParam("id") Long id) {
    return Response.ok().build();
  }
}