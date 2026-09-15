package io.swagger.v3.jaxrs2.petstore.link;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.DependentSchema;
import io.swagger.v3.oas.annotations.media.DependentSchemas;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Class with Links
 */
public class LinksAndContent31Resource {
    @Path("/links")
    @Operation(operationId = "getUserWithAddress",
            responses = {
                    @ApiResponse(description = "test description",
                            content = @Content(
                                    mediaType = "*/*",
                                    schema = @Schema(
                                            types = { "object" }
                                    ),
                                    dependentSchemas = {
                                            @DependentSchema(
                                                    name = "value",
                                                    schema = @Schema(
                                                            types = {
                                                                    "string",
                                                                    "number"
                                                            }
                                                    )
                                            )
                                    }
                            ),
                            links = {
                                    @Link(
                                            name = "address",
                                            operationId = "getAddress",
                                            parameters = @LinkParameter(
                                                    name = "userId",
                                                    expression = "$request.query.userId")),
                                    @Link(
                                            name = "user",
                                            operationId = "getUser",
                                            operationRef = "#/components/links/MyLink",
                                            parameters = @LinkParameter(
                                                    name = "userId",
                                                    expression = "$request.query.userId"),
                                            extensions = @Extension(
                                                    name = "x-one",
                                                    properties = {
                                                            @ExtensionProperty(
                                                                    name = "x-sample-extension",
                                                                    value = "true"
                                                            )
                                                    }
                                            )

                                    )
                            })}
    )
    @GET
    public String getUser(@QueryParam("userId")final String userId) {
        return null;
    }

}
