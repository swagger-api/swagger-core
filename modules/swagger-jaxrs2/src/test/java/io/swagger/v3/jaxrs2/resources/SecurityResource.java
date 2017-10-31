package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


@SecurityScheme(name = "myOauth2Security",
        type = SecuritySchemeType.OAUTH2,
        in = SecuritySchemeIn.HEADER,
        description = "myOauthSecurity Description",
        flows = @OAuthFlows(implicit = @OAuthFlow(authorizationUrl = "http://x.com",
                scopes = @OAuthScope(
                        name = "write:pets",
                        description = "modify pets in your account"))
        )
)
public class SecurityResource {

    @GET
    @Path("/")
    @Operation(operationId = "Operation Id",
            description = "description")
    @SecurityRequirement(name = "security_key",
            scopes = {"write:pets", "read:pets"}
    )
    public void getSecurity() {
    }
}
