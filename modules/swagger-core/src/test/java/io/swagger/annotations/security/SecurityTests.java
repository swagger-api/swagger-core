package io.swagger.annotations.security;

import io.swagger.oas.annotations.security.*;
import org.testng.annotations.Test;

public class SecurityTests {
    @Test
    public void testSecurityRequirement() {

    }

    @SecurityRequirement(name = "apiKey")
    static class SecurityRequirementOnClass {

    }

    @SecurityScheme(name = "apiKey", type = "apiKey", in = "header")
    static class ApiKeySchemeOnClass {

    }

    @SecurityScheme(name = "myOauth2Security",
            type = "oauth2",
            in = "header",
            flows = @OAuthFlows(implicit = @OAuthFlow(authorizationUrl = "", scopes = @Scopes(name = "write:pets", description = "modify pets in your account"))))
    static class OAuth2SchemeOnClass {

    }
}
