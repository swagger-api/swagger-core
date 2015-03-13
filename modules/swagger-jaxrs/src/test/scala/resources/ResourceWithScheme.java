package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.*;

@Path("test")
@Produces(MediaType.APPLICATION_JSON)
@Api("test")
public class ResourceWithScheme {
  @Path("/status")
  @GET
  @ApiOperation(value = "Get status", protocols = "https")
  public String getStatus() {
    return "{\"status\":\"OK!\"}";
  }
}