package io.swagger.jaxrs2.resources;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.security.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by RafaelLopez on 5/20/17.
 */
@SecurityScheme(name = "myOauth2Security",
        type = "oauth2",
        in = "header",
        description = "myOauthSecurity Description",
        flows = @OAuthFlows(implicit = @OAuthFlow(authorizationUrl = "http://x.com",
                scopes = @Scopes(
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
