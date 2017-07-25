package io.swagger.jaxrs2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.links.Link;
import io.swagger.oas.annotations.links.LinkParameter;

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
            @LinkParameter(
                    name = "Link Parameter",
                    expression = "Link Expression"
            )
    )
    public void getParameters() {
    }
}
