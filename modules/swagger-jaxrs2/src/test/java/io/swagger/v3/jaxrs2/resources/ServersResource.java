package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@OpenAPIDefinition(
    servers = {
        @Server(
            description = "definition server 1",
            url = "http://definition1",
            variables = {
                @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"}),
                @ServerVariable(name = "var2", description = "var 2", defaultValue = "1", allowableValues = {"1", "2"})
            })
    }
)
@Server(
        description = "class server 1",
        url = "http://class1",
        variables = {
                @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"}),
                @ServerVariable(name = "var2", description = "var 2", defaultValue = "1", allowableValues = {"1", "2"})
        })
@Server(
        description = "class server 2",
        url = "http://class2",
        variables = {
                @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"})
        })
public class ServersResource {

    @GET
    @Path("/")
    @Operation(servers = {
            @Server(
                    description = "operation server 1",
                    url = "http://op1",
                    variables = {
                            @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"})
                    })
    })
    @Server(
            description = "method server 1",
            url = "http://method1",
            variables = {
                    @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"})
            })
    @Server(
            description = "method server 2",
            url = "http://method2"
    )

    public Response getServers() {
        return Response.ok().entity("ok").build();
    }
}
