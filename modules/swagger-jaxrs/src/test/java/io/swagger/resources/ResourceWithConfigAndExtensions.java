package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.annotations.ExternalDocs;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.ReaderListener;
import io.swagger.models.Swagger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.ArrayList;

@SwaggerDefinition(
        info = @Info(
                description = "Custom description",
                version = "V1.2.3",
                title = "TheAwesomeApi",
                termsOfService = "do-what-you-want",
                contact = @Contact(name = "Sponge-Bob", email = "sponge-bob@swagger.io", url = "http://swagger.io"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org"),
                extensions = {
                        @Extension(properties = {
                                @ExtensionProperty(name = "test1", value = "value1"),
                                @ExtensionProperty(name = "test2", value = "value2")
                        }),
                        @Extension(name = "test", properties = {
                                @ExtensionProperty(name = "test1", value = "value1"),
                                @ExtensionProperty(name = "test2", value = "value2")
                        })
                }
        ),
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        tags = {
                @Tag(name = "mytag", description = "my tag"),
                @Tag(name = "anothertag", description = "another tag",
                        externalDocs = @ExternalDocs(value = "test", url = "http://swagger.io")),
                @Tag(name = "tagwithextensions", description = "my tag",
                        extensions = @Extension(properties = {@ExtensionProperty(name = "test", value = "value")}))
        }, externalDocs = @ExternalDocs(value = "test", url = "http://swagger.io")
)
@Api(value = "/external/info/")
@Path("who/cares")
public class ResourceWithConfigAndExtensions implements ReaderListener {
    @ApiOperation(value = "test.", tags = {"tagwithextensions", "mytag", "testingtag"}, extensions = {
            @Extension(properties = @ExtensionProperty(name = "test", value = "value"))
    })
    @GET
    public void getTest(@ApiParam(value = "test") ArrayList<String> tenantId) {
        return;
    }

    @Override
    public void beforeScan(Reader reader, Swagger swagger) {
        swagger.addTag(new io.swagger.models.Tag().name("Tag-added-before-read"));
    }

    @Override
    public void afterScan(Reader reader, Swagger swagger) {
        swagger.addTag(new io.swagger.models.Tag().name("Tag-added-after-read"));
    }
}