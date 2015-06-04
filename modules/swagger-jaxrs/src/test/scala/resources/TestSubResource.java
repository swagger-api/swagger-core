package resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Api
@Produces(MediaType.APPLICATION_JSON)
public class TestSubResource {
  @Path("/otherStatus")
  @GET
  @ApiOperation("Get the other status!")
  public String otherStatus() {
    return "{\"a\":\"Still Ok!\"}";
  }
}