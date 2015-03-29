package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api("/external/info/")
@Path("external/info/")
public class Resource937 {
  @ApiOperation(value="test")
  @GET
  public void getTest(@ApiParam(required=false, defaultValue = "dogs") @DefaultValue("cats") @QueryParam("isRequired") Boolean isRequired) {
    return;
  }
}