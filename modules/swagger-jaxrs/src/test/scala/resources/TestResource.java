package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.*;

@Path("test")
@Produces(MediaType.APPLICATION_JSON)
@Api("test")
public class TestResource {
  @Path("/status")
  @GET
  @ApiOperation("Get status")
  public String getStatus() {
    return "{\"status\":\"OK!\"}";
  }

  @Path("/more")
  @ApiOperation("Get more")
  @Produces({MediaType.APPLICATION_XML})
  public TestSubResource getSubResource(
    @ApiParam("a query param") @QueryParam("qp") Integer qp) {
    return new TestSubResource();
  }
}