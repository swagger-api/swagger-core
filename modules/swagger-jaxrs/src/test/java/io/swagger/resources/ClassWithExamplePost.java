package io.swagger.resources;

import io.swagger.annotations.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.ArrayList;

@Api("/external/info/")
@Path("external/info/")
public class ClassWithExamplePost {
    @ApiOperation(value = "test.")
    @POST
    public void postTest(@ApiParam(value = "test",
            examples = @Example(value = {@ExampleProperty(mediaType="fun", value="bar")})) ArrayList<String> tenantId) {
        return;
    }
}
