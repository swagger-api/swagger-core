package io.swagger.v3.jaxrs2.annotations.security;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.testng.annotations.Test;

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

    @Test(enabled = false)
    public void testSecurityRequirement() {
        String openApiYAML = readIntoYaml(SecurityTests.SecurityRequirementOnClass.class);
        int start = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, openApiYAML.length() - 1);
        String expectedYAML = "components:\n" +
                "  securitySchemes:\n" +
                "    myOauth2Security:\n" +
                "      name: \"myOauth2Security\"\n" +
                "      flows:\n" +
                "        implicit:\n" +
                "          authorizationUrl: \"http://url.com/auth\"\n" +
                "          scopes:\n" +
                "            name: \"write:pets\"\n" +
                "            description: \"modify pets in your account\"\n" +
                "        password:\n" +
                "          scopes:\n" +
                "            name: \"\"\n" +
                "            description: \"\"\n" +
                "        clientCredentials:\n" +
                "          scopes:\n" +
                "            name: \"\"\n" +
                "            description: \"\"\n" +
                "        authorizationCode:\n" +
                "          scopes:\n" +
                "            name: \"\"\n" +
                "            description: \"\"";
        assertEquals(openApiYAML, expectedYAML);

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

    @SecurityRequirement(name = "apiKey")
    static class SecurityRequirementOnClass {

    }

    @SecurityScheme(name = "apiKey", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER)
    static class ApiKeySchemeOnClass {

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
