package resources;

import javax.ws.rs.GET;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * The <code>NoPathSubResource</code> class defines test sub-resource without
 * {@link javax.ws.rs.Path} annotations.
 */
@Api()
public class NoPathSubResource {

  @ApiOperation(value = "Returns greeting")
  @GET
  public String getGreeting() {
    return "Hello!";
  }
}
