package resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import models.Tag;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Api
public class ResourceWithImplicitParams {

  @POST
  @Path("/testString")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "sort", paramType = "query", dataType = "string", required = false, value = "Comma-delimited list of fields to sort by."),
          @ApiImplicitParam(name = "type", paramType = "path", dataType = "string", allowableValues = "one,two,three"),
          @ApiImplicitParam(name = "size", paramType = "header", dataType = "int", allowableValues = "range[1,infinity]"),
          @ApiImplicitParam(name = "width", paramType = "form", dataType = "int", allowableValues = "range[infinity,1]"),
          @ApiImplicitParam(name = "width", paramType = "formData", dataType = "int", allowableValues = "range[infinity,1]"),
          @ApiImplicitParam(name = "height", paramType = "query", dataType = "int", allowableValues = "range[3,4]"),
          @ApiImplicitParam(name = "width", paramType = "unknown")
  })
  @ApiOperation("Test operation with implicit parameters")
  public void testString() {
  }
}
