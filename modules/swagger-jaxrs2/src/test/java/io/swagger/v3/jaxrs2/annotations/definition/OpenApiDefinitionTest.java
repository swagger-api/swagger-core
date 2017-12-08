package io.swagger.v3.jaxrs2.annotations.definition;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.testng.annotations.Test;

import java.io.IOException;

public class OpenApiDefinitionTest extends AbstractAnnotationTest {
    @Test
    public void testSimpleInfoGet() throws IOException {

        String expectedYAML = "openapi: 3.0.1\n" +
                "info:\n" +
                "  title: the title\n" +
                "  description: My API\n" +
                "  contact:\n" +
                "    name: Fred\n" +
                "    url: http://gigantic-server.com\n" +
                "    email: Fred@gigagantic-server.com\n" +
                "  license:\n" +
                "    name: Apache 2.0\n" +
                "    url: http://foo.bar\n" +
                "  version: \"0.0\"\n" +
                "externalDocs:\n" +
                "  description: definition docs desc\n" +
                "servers:\n" +
                "- url: http://foo\n" +
                "  description: server 1\n" +
                "  variables:\n" +
                "    var1:\n" +
                "      description: var 1\n" +
                "      enum:\n" +
                "      - \"1\"\n" +
                "      - \"2\"\n" +
                "      default: \"1\"\n" +
                "    var2:\n" +
                "      description: var 2\n" +
                "      enum:\n" +
                "      - \"1\"\n" +
                "      - \"2\"\n" +
                "      default: \"1\"\n" +
                "security:\n" +
                "- req 1:\n" +
                "  - a\n" +
                "  - b\n" +
                "- req 2:\n" +
                "  - b\n" +
                "  - c\n" +
                "tags:\n" +
                "- name: Tag 1\n" +
                "  description: desc 1\n" +
                "  externalDocs:\n" +
                "    description: docs desc\n" +
                "- name: Tag 2\n" +
                "  description: desc 2\n" +
                "  externalDocs:\n" +
                "    description: docs desc 2\n" +
                "- name: Tag 3\n";

        compareAsYaml(OpenApiDefinitionTest.ClassWithAnnotation.class, expectedYAML);
    }

    @OpenAPIDefinition(
            info = @Info(
                    title = "the title",
                    version = "0.0",
                    description = "My API",
                    license = @License(name = "Apache 2.0", url = "http://foo.bar"),
                    contact = @Contact(url = "http://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
            ),
            tags = {
                    @Tag(name = "Tag 1", description = "desc 1", externalDocs = @ExternalDocumentation(description = "docs desc")),
                    @Tag(name = "Tag 2", description = "desc 2", externalDocs = @ExternalDocumentation(description = "docs desc 2")),
                    @Tag(name = "Tag 3")
            },
            externalDocs = @ExternalDocumentation(description = "definition docs desc"),
            security = {
                    @SecurityRequirement(name = "req 1", scopes = {"a", "b"}),
                    @SecurityRequirement(name = "req 2", scopes = {"b", "c"})
            },
            servers = {
                    @Server(
                            description = "server 1",
                            url = "http://foo",
                            variables = {
                                    @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"}),
                                    @ServerVariable(name = "var2", description = "var 2", defaultValue = "1", allowableValues = {"1", "2"})
                            })
            }
    )
    static class ClassWithAnnotation {

        public void foo() {
        }

    }

}