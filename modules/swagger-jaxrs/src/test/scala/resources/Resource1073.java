package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api("/external/info/")
@Path("external/info/")
public class Resource1073 {
  @ApiOperation(value="test", hidden = true)
  @GET
  public void getTest(@ApiParam(value = "test") ArrayList<String> tenantId) {
    return;
  }
}