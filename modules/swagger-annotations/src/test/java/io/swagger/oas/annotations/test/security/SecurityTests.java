package io.swagger.oas.annotations.test.security;

import io.swagger.oas.annotations.security.OAuthFlow;
import io.swagger.oas.annotations.security.OAuthFlows;
import io.swagger.oas.annotations.security.Scopes;
import io.swagger.oas.annotations.security.SecurityRequirement;
import io.swagger.oas.annotations.security.SecurityScheme;
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
