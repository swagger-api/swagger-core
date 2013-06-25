package testresources;

import com.wordnik.swagger.core.*;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import javax.xml.bind.annotation.*;

@Path("/javaPathParamTest")
@Api(value = "/javaPathParamTest")
public class JavaPathParamTargetResource {
  @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")
  @PathParam("id") 
  String id;

  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    response = Sample.class,
    position = 0)
  @ApiResponses({
    @ApiResponse(code = 400, message = "Invalid ID", response = NotFoundModel.class),
    @ApiResponse(code = 404, message = "object not found")}
  )
  public Response getTest(
    @ApiParam(value = "a query param", required = true, allowableValues = "a,b,c")@QueryParam("qp") String classParam) {
    Sample out = new Sample();
    out.setName("foo");
    out.setValue("bar");
    return Response.ok().entity(out).build();
  }

  @GET
  @Path("/{id}/details")
  @ApiOperation(value = "Get details by ID",
    notes = "No details provided",
    response = Sample.class,
    position = 0)
  @ApiResponses({
    @ApiResponse(code = 400, message = "Invalid ID", response = NotFoundModel.class),
    @ApiResponse(code = 404, message = "object not found")}
  )
  public Response getDetails() {
    Sample out = new Sample();
    out.setName("foo");
    out.setValue("bar");
    return Response.ok().entity(out).build();
  }
}
