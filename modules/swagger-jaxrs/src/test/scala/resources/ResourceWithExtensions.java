package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import java.util.*;

@SwaggerConfig(
   info = @Info( extensions = {
           @Extension( properties = {
                   @ExtensionProperty( name = "test1", value = "value1"),
                   @ExtensionProperty( name = "test2", value = "value2")
           }),
           @Extension( name = "test", properties = {
                   @ExtensionProperty( name = "test1", value = "value1"),
                   @ExtensionProperty( name = "test2", value = "value2")
           })
   })
)
@Api(value = "/external/info/" )
@Path("who/cares")
public class ResourceWithExtensions {
    @ApiOperation(value="test.", extensions = {
            @Extension( properties = @ExtensionProperty( name = "test", value = "value"))
    })
    @GET
    public void getTest(@ApiParam(value = "test") ArrayList<String> tenantId) {
        return;
    }
}