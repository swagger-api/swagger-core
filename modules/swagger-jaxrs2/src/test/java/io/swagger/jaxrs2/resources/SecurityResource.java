package io.swagger.jaxrs2.resources;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.oas.annotations.enums.SecuritySchemeType;
import io.swagger.oas.annotations.security.*;

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
