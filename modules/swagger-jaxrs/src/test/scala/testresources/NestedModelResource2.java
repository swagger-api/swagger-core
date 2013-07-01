package testresources;

import testmodels.*;
import com.wordnik.swagger.core.*;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import javax.xml.bind.annotation.*;

@Path("/family")
@Api(value = "/family", description = "Family Resource")
public class NestedModelResource2 {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Gets a family by id",
    notes = "No details provided",
    response = Window.class)
  @ApiResponses({@ApiResponse(code = 404, message = "object not found")})
  public Response getFamilyById() {
    return Response.ok().build();
  }
}
