package io.swagger.v3.jaxrs2.annotations.security;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.resources.SecurityResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class SecurityTest extends AbstractAnnotationTest {
    @Test
    public void testSecuritySheme() {
        String openApiYAML = readIntoYaml(SecurityTest.OAuth2SchemeOnClass.class);
        int start = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, openApiYAML.length() - 1);
        String expectedYAML = "components:\n" +
                "  securitySchemes:\n" +
                "    myOauth2Security:\n" +
                "      type: oauth2\n" +
                "      in: header\n" +
                "      flows:\n" +
                "        implicit:\n" +
                "          authorizationUrl: http://url.com/auth\n" +
                "          scopes:\n" +
                "            write:pets: modify pets in your account";
        assertEquals(extractedYAML, expectedYAML);

    }

    @Test
    public void testSecurityRequirement() throws IOException {
        String expectedYAML = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /2:\n" +
                "    get:\n" +
                "      description: description 2\n" +
                "      operationId: Operation Id 2\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "      security:\n" +
                "      - security_key:\n" +
                "        - write:pets\n" +
                "        - read:pets\n" +
                "      - myOauth2Security:\n" +
                "        - write:pets\n" +
                "      - security_key2:\n" +
                "        - write:pets\n" +
                "        - read:pets\n" +
                "  /:\n" +
                "    get:\n" +
                "      description: description\n" +
                "      operationId: Operation Id\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "      security:\n" +
                "      - security_key:\n" +
                "        - write:pets\n" +
                "        - read:pets\n" +
                "      - myOauth2Security:\n" +
                "        - write:pets\n" +
                "components:\n" +
                "  securitySchemes:\n" +
                "    myOauth2Security:\n" +
                "      type: oauth2\n" +
                "      description: myOauthSecurity Description\n" +
                "      in: header\n" +
                "      flows:\n" +
                "        implicit:\n" +
                "          authorizationUrl: http://x.com\n" +
                "          scopes:\n" +
                "            write:pets: modify pets in your account";
        compareAsYaml(SecurityResource.class, expectedYAML);

    }

    @Test
    public void testMultipleSecurityShemes() {
        String openApiYAML = readIntoYaml(SecurityTest.MultipleSchemesOnClass.class);
        int start = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, openApiYAML.length() - 1);
        String expectedYAML = "components:\n" +
                "  securitySchemes:\n" +
                "    apiKey:\n" +
                "      type: apiKey\n" +
                "      name: API_KEY\n" +
                "      in: header\n" +
                "    myOauth2Security:\n" +
                "      type: oauth2\n" +
                "      in: header\n" +
                "      flows:\n" +
                "        implicit:\n" +
                "          authorizationUrl: http://url.com/auth\n" +
                "          scopes:\n" +
                "            write:pets: modify pets in your account";
        assertEquals(extractedYAML, expectedYAML);

    }

    @Test
    public void testTicket2767() {
        String openApiYAML = readIntoYaml(SecurityTest.Ticket2767.class);
        String expectedYAML = "openapi: 3.0.1\n" +
                "info:\n" +
                "  title: Test\n" +
                "  version: 1.0-SNAPSHOT\n" +
                "security:\n" +
                "- basicAuth: []\n" +
                "components:\n" +
                "  securitySchemes:\n" +
                "    basicAuth:\n" +
                "      type: http\n" +
                "      scheme: basic\n";
        assertEquals(openApiYAML, expectedYAML);

    }

    @Test
    public void testTicket2767_2() {
        String openApiYAML = readIntoYaml(SecurityTest.Ticket2767_2.class);
        String expectedYAML = "openapi: 3.0.1\n" +
                "info:\n" +
                "  title: Test\n" +
                "  version: 1.0-SNAPSHOT\n" +
                "security:\n" +
                "- api_key: []\n" +
                "components:\n" +
                "  securitySchemes:\n" +
                "    api_key:\n" +
                "      type: apiKey\n" +
                "      name: API_KEY\n";
        assertEquals(openApiYAML, expectedYAML);

    }

    @SecurityScheme(name = "myOauth2Security",
            type = SecuritySchemeType.OAUTH2,
            in = SecuritySchemeIn.HEADER,
            flows = @OAuthFlows(
                    implicit = @OAuthFlow(authorizationUrl = "http://url.com/auth",
                            scopes = @OAuthScope(name = "write:pets", description = "modify pets in your account"))))
    static class OAuth2SchemeOnClass {

    }

    @SecurityScheme(name = "myOauth2Security",
            type = SecuritySchemeType.OAUTH2,
            in = SecuritySchemeIn.HEADER,
            flows = @OAuthFlows(
                    implicit = @OAuthFlow(authorizationUrl = "http://url.com/auth",
                            scopes = @OAuthScope(name = "write:pets", description = "modify pets in your account"))))
    @SecurityScheme(name = "apiKey", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, paramName = "API_KEY")
    static class MultipleSchemesOnClass {

    }


    @OpenAPIDefinition(
            security = {@SecurityRequirement(name = "basicAuth")},
            info = @Info( title = "Test", description = "", version = "1.0-SNAPSHOT"))
    @SecurityScheme(name="basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
    static class Ticket2767 {

    }

    @OpenAPIDefinition(
            security = {@SecurityRequirement(name = "api_key")},
            info = @Info( title = "Test", description = "", version = "1.0-SNAPSHOT"))
    @SecurityScheme(name="api_key", type = SecuritySchemeType.APIKEY, paramName = "API_KEY")
    static class Ticket2767_2 {

    }
}
