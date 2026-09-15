package io.swagger.v3.jaxrs2.petstore.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.PUT;
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
@SecurityRequirement(name = "security_key",
        scopes = {"write:pets", "read:pets"}
)
@SecurityRequirement(name = "myOauth2Security",
        scopes = {"write:pets"}
)
@Path("/security")
public class SecurityResource {
    @GET
    @Operation(operationId = "Operation Id",
            description = "description")
    @SecurityRequirement(name = "security_key",
            scopes = {"write:pets", "read:pets"}
    )
    public void getSecurity() {
    }

    @PATCH
    @Operation(operationId = "Operation Id 2",
            description = "description 2")
    @SecurityRequirement(name = "security_key2",
            scopes = {"write:pets", "read:pets"}
    )
    public void getSecurity2() {
    }

    @PUT
    @Operation(operationId = "Operation Id 3",
            description = "description 3", security =
    @SecurityRequirement(name = "security_key3",
            scopes = {"write:pets", "read:pets"}
    ))
    public void setSecurity(String security) {
    }
}
