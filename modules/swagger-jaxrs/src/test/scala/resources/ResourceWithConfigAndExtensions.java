package resources;

import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.annotations.Contact;
import com.wordnik.swagger.annotations.ExternalDocs;
import com.wordnik.swagger.annotations.Info;
import com.wordnik.swagger.annotations.License;
import com.wordnik.swagger.jaxrs.Reader;
import com.wordnik.swagger.jaxrs.config.ReaderListener;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.Tag;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import java.util.*;

@SwaggerConfig(
   info = @Info(
       description = "Custom description",
       version = "V1.2.3",
       title = "TheAwesomeApi",
       termsOfService = "do-what-you-want",
       contact = @Contact( name = "Sponge-Bob", email = "sponge-bob@swagger.io", url = "http://swagger.io"),
       license = @License( name = "Apache 2.0", url = "http://www.apache.org"),
       extensions = {
            @Extension( properties = {
                   @ExtensionProperty( name = "test1", value = "value1"),
                   @ExtensionProperty( name = "test2", value = "value2")
            }),
            @Extension( name = "test", properties = {
                   @ExtensionProperty( name = "test1", value = "value1"),
                   @ExtensionProperty( name = "test2", value = "value2")
            })
        }
   ),
        consumes = {"application/json","application/xml"},
        produces = {"application/json","application/xml"},
        schemes = {SwaggerConfig.Scheme.HTTP, SwaggerConfig.Scheme.HTTPS},
        tags = {
                @com.wordnik.swagger.annotations.Tag( name ="mytag", description="my tag"),
                @com.wordnik.swagger.annotations.Tag( name ="anothertag", description="another tag",
                    externalDocs = @ExternalDocs( value = "test", url = "http://swagger.io")),
                @com.wordnik.swagger.annotations.Tag( name ="tagwithextensions", description="my tag",
                        extensions = @Extension( properties = {@ExtensionProperty(name="test", value = "value")}))
        }, externalDocs = @ExternalDocs( value = "test", url = "http://swagger.io")
)
@Api(value = "/external/info/" )
@Path("who/cares")
public class ResourceWithConfigAndExtensions implements ReaderListener {
    @ApiOperation(value="test.", extensions = {
            @Extension( properties = @ExtensionProperty( name = "test", value = "value"))
    })
    @GET
    public void getTest(@ApiParam(value = "test") ArrayList<String> tenantId) {
        return;
    }

    @Override
    public void beforeScan(Reader reader, Swagger swagger) {
        swagger.addTag( new Tag().name( "Tag-added-before-read"));
    }

    @Override
    public void afterScan(Reader reader, Swagger swagger) {
        swagger.addTag( new Tag().name( "Tag-added-after-read"));
    }
}