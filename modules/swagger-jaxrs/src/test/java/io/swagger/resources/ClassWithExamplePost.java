package io.swagger.resources;

import io.swagger.annotations.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;

@Api("/external/info/")
@Path("/")
public class ClassWithExamplePost {
    @ApiOperation(value = "test")
    @POST
    @Path("external/info")
    public void postTest(@ApiParam(value = "test",
            examples = @Example(value = {
                    @ExampleProperty(mediaType="application/json", value="[\"a\",\"b\"]")
            })) ArrayList<String> tenantId) {
        return;
    }

    @ApiOperation(value = "test")
    @POST
    @Path("external/info2")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "body",
                    name = "myPody",
                    dataType = "[Ljava.lang.String;",
                    examples = @Example(value = {
                    @ExampleProperty(mediaType="application/json", value="[\"a\",\"b\"]")}))
    })
    public void implicitPostTest() {
        return;
    }

    @ApiOperation(value = "test")
    @GET
    @Path("external/info")
    public void queryExample(@ApiParam(value = "test",
            example = "a,b,c") @QueryParam("tenantId") ArrayList<String> tenantId) {
        return;
    }

    @ApiOperation(value = "test")
    @GET
    @Path("external/info2")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query",
                    name = "myId",
                    dataType = "java.lang.Long",
                    example = "77") })
    public void implicitQueryExample() {
        return;
    }
}
