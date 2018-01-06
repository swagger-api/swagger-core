package io.swagger.v3.jaxrs2.resources.extensions;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.extensions.Extensions;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

/**
 * Created by rafaellopez on 1/4/18.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "the title",
                version = "0.0",
                description = "My API",
                license = @License(name = "Apache 2.0", url = "http://foo.bar"),
                contact = @Contact(url = "http://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
        ))
@Extensions(value = {
        @Extension(name = "x-class", properties = {
                @ExtensionProperty(name = "x-class", value = "Class Name")})})
@Extension(name = "x-class-independent",
        properties = {@ExtensionProperty(name = "class", value = "independent")})
public class ClassExtensionsResource {
}
