package resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("test")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "test")
public class ResourceWithoutScheme {
  @Path("/status")
  @GET
  @ApiOperation(value = "Get status")
  public String getStatus() {
    return "{\"status\":\"OK!\"}";
  }

  @Path("/value")
  @GET
  @ApiOperation(value = "Get value", protocols = "ftp")
  public String getValue() {
    return "{\"value\":\"OK!\"}";
  }
}
