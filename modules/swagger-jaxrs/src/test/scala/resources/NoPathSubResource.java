package resources;

import javax.ws.rs.GET;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

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
