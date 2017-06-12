package io.swagger.jaxrs2.resources;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.links.Link;
import io.swagger.oas.annotations.links.LinkParameters;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.security.SecurityRequirement;
import io.swagger.oas.annotations.servers.Server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by RafaelLopez on 5/20/17.
 */
public class LinkParametersResource {

    @GET
    @Path("/")
    @Operation(operationId = "Operation Id",
            description = "description")
    @Link(description = "Link Description",
            name = "Link Name",
            operationId = "Operation Id",
            operationRef = "Operation Ref",
            parameters =
            @LinkParameters(
                    name = "Link Parameter",
                    expression = "Link Expression"
            )
    )
    public void getParameters() {
    }
}
