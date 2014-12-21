package testresources;

import testmodels.*;
import com.wordnik.swagger.core.*;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import javax.xml.bind.annotation.*;

@Path("/basic")
@Api(value = "/basic", description = "Basic resource")
public class ResourceWithReturnTypes {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Find by ID")
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid ID supplied"),
      @ApiResponse(code = 404, message = "Pet not found") })
  public JavaSample getTest(
    @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")@DefaultValue("1") @QueryParam("id") String id) {
    JavaSample out = new JavaSample();
    out.setName("foo");
    out.setValue("bar");
    return out;
  }
}
