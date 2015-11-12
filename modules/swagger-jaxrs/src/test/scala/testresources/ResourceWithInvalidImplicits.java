package testresources;

import testmodels.*;
import com.wordnik.swagger.core.*;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import javax.xml.bind.annotation.*;

@Path("/basic")
@Api(value = "/basic", description = "Basic resource")
public class ResourceWithInvalidImplicits {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Find by ID")
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid ID supplied"),
      @ApiResponse(code = 404, message = "Pet not found") })
  @ApiImplicitParams({
    @ApiImplicitParam(
      name = "id",
      value = "An implicit param missing a datatype",
      required = true,
      paramType = "query"),
    @ApiImplicitParam(
      name = "name",
      value = "An implicit param missing a param type",
      required = true,
      dataType = "string"),
    @ApiImplicitParam(
      name = "age",
      value = "An implicit param that is OK",
      required = true,
      paramType = "query",
      dataType = "integer")})
  public JavaSample getTest() {
    JavaSample out = new JavaSample();
    out.setName("foo");
    out.setValue("bar");
    return out;
  }
}
