package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.*;

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