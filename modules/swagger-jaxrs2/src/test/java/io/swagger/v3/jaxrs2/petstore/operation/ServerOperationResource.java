package io.swagger.v3.jaxrs2.petstore.operation;

import io.swagger.v3.jaxrs2.resources.model.Pet;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.servers.Server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Resource With a Hidden Operation
 */
@OpenAPIDefinition(
        servers = {
                @Server(description = "server 1", url = "http://foo")
        }
)
public class ServerOperationResource {
    @Path("/serversoperation")
    @GET
    @Operation(operationId = "Pets", description = "Pets Example",
            servers = {
                    @Server(description = "server 2", url = "http://foo2")
            }
    )
    public Pet getPet() {
        return new Pet();
    }
}
