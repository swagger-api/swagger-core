package io.swagger.v3.jaxrs2.annotations.security;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.resources.SecurityResource;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class SecurityTests extends AbstractAnnotationTest {
    @Test
    public void testSecuritySheme() {
        String openApiYAML = readIntoYaml(SecurityTests.OAuth2SchemeOnClass.class);
        int start = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, openApiYAML.length() - 1);
        String expectedYAML = "components:\n" +
                "  securitySchemes:\n" +
                "    myOauth2Security:\n" +
                "      type: oauth2\n" +
                "      name: myOauth2Security\n" +
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
                "      name: myOauth2Security\n" +
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
        String openApiYAML = readIntoYaml(SecurityTests.MultipleSchemesOnClass.class);
        int start = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, openApiYAML.length() - 1);
        String expectedYAML = "components:\n" +
                "  securitySchemes:\n" +
                "    apiKey:\n" +
                "      type: apiKey\n" +
                "      name: apiKey\n" +
                "      in: header\n" +
                "    myOauth2Security:\n" +
                "      type: oauth2\n" +
                "      name: myOauth2Security\n" +
                "      in: header\n" +
                "      flows:\n" +
                "        implicit:\n" +
                "          authorizationUrl: http://url.com/auth\n" +
                "          scopes:\n" +
                "            write:pets: modify pets in your account";
        assertEquals(extractedYAML, expectedYAML);

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
    @SecurityScheme(name = "apiKey", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER)
    static class MultipleSchemesOnClass {

    }
}
