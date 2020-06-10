package org.my.project.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.ArrayList;

@Path("/packageB")
public class ResourceInPackageB {
    @Operation(operationId = "test.")
    @GET
    public void getTest(@Parameter(name = "test") ArrayList<String> tenantId) {
        return;
    }
}