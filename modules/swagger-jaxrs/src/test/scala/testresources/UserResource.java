package testresources;

import testmodels.*;
import com.wordnik.swagger.core.*;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import javax.xml.bind.annotation.*;

@Path("/nested")
@Api(value = "/nested", description = "nested model resource")
public class UserResource {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Gets a family by id",
    notes = "No details provided",
    response = User.class)
  @ApiResponses({@ApiResponse(code = 404, message = "object not found")})
  public Response getUserById() {
    return Response.ok().build();
  }
}
