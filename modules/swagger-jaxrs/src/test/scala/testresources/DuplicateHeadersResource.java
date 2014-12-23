package testresources;

import testmodels.*;
import com.wordnik.swagger.core.*;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import javax.xml.bind.annotation.*;

@Path("/basic")
@Api(value = "/basic", description = "Basic resource")
public class DuplicateHeadersResource {
  @ApiParam(value = "NOT this one!")
  @HeaderParam("auth_token")
  String header;

  @GET
  @Path("/{id}")
  @ApiOperation(value = "Find by ID")
  public JavaSample getTest(
    @ApiParam(value = "This one!")@HeaderParam("auth_token") String duplicate) {
    JavaSample out = new JavaSample();
    out.setName("foo");
    out.setValue("bar");
    return out;
  }
}
