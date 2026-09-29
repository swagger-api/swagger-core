package io.swagger.v3.rest.petstore.openapidefintion;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

/**
 * OpenAPIDefinition Example
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Pet Resource Example",
                version = "2.0",
                description = "API Definition",
                summary = "API Summary",
                termsOfService = "Terms of service",
                license = @License(name = "Apache 2.0", url = "http://foo.bar", identifier = "Apache"),
                contact = @Contact(url = "http://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
        )
)
public class OpenAPI31DefinitionResource {
    public void foo() {
    }
}
