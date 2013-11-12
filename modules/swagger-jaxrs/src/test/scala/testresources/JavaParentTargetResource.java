package testresources;

import com.wordnik.swagger.core.*;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import javax.xml.bind.annotation.*;

abstract class JavaParentTargetResource {
  @ApiParam(value = "sample name data", required = true)
  @PathParam("name") 
  protected String name;
}