package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api(value = "/external/info/", authorizations = @Authorization("my_auth"))
@Path("external/info/")
public class Resource1041 {
  @ApiOperation(value = "test")
  @GET
  @Path("/path1")
  public void getTest(@ApiParam(value = "test") ArrayList<String> tenantId) {
    return;
  }

  @ApiOperation(value = "test",  authorizations = @Authorization("your_auth"))
  @GET
  @Path("/path2")
  public void getTest2(@ApiParam(value = "test") ArrayList<String> tenantId) {
    return;
  }
}