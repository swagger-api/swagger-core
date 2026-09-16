package io.swagger.v3.jaxrs2.petstore.parameter;

import io.swagger.v3.jaxrs2.resources.model.Pet;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Resource with some Parameters examples
 */
public class Parameters31Resource {
    @Path("/parameters")
    @POST
    @Operation(
            operationId = "subscribe",
            description = "subscribes a client to updates relevant to the requestor's account, as " +
                    "identified by the input token.  The supplied url will be used as the delivery address for response payloads",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
                            required = true,
                            schema = @Schema(
                                    $id = "http://yourdomain.com/schemas/myschema.json",
                                    $comment = "schema $comment",
                                    type = "string"
                            ),
                            style = ParameterStyle.SIMPLE),
                    @Parameter(in = ParameterIn.QUERY, name = "explodeFalse",
                            required = true,
                            explode = Explode.FALSE,
                            schema = @Schema(
                                    types = { "string", "number" },
                                    exclusiveMaximumValue = 100,
                                    $schema = "parameter $schema",
                                    $anchor = "parameter $anchor",
                                    _if = Parameters31Resource.SubscriptionResponse.class,
                                    _else = Parameters31Resource.SubscriptionResponse.class,
                                    then = Parameters31Resource.SubscriptionResponse.class
                            )),
                    @Parameter(in = ParameterIn.QUERY, name = "explodeAvoiding", required = true, explode = Explode.TRUE,
                            schema = @Schema(
                                    type = "int",
                                    format = "id",
                                    exclusiveMaximumValue = 1000,
                                    exclusiveMinimumValue = 10,
                                    description = "the generated id",
                                    accessMode = Schema.AccessMode.READ_ONLY
                            )),
                    @Parameter(in = ParameterIn.QUERY, name = "arrayParameter", required = true, explode = Explode.TRUE,
                            array = @ArraySchema(maxItems = 10, minItems = 1,
                                    schema = @Schema(implementation = Parameters31Resource.SubscriptionResponse.class),
                                    uniqueItems = true
                            )
                            ,
                            schema = @Schema(
                                    type = "int",
                                    format = "id",
                                    description = "the generated id",
                                    accessMode = Schema.AccessMode.READ_ONLY),
                            content = @Content(schema = @Schema(type = "number",
                                    description = "the generated id",
                                    accessMode = Schema.AccessMode.READ_ONLY))
                    ),
                    @Parameter(in = ParameterIn.QUERY, name = "arrayParameterImplementation", required = true, explode = Explode.TRUE,
                            array = @ArraySchema(
                                    maxItems = 10,
                                    minItems = 1,
                                    maxContains = 50,
                                    minContains = 1,
                                    contains = @Schema(implementation = Parameters31Resource.SubscriptionResponse.class),
                                    unevaluatedItems = @Schema(implementation = Parameters31Resource.SubscriptionResponse.class),
                                    schema = @Schema(implementation = Parameters31Resource.SubscriptionResponse.class),
                                    uniqueItems = true
                            )
                    ),
                    @Parameter(in = ParameterIn.QUERY, name = "arrayParameterImplementation2", required = true, explode = Explode.TRUE,
                            schema = @Schema(implementation = Parameters31Resource.SubscriptionResponse.class))

            },
            responses = {
                    @ApiResponse(
                            description = "test description", content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(
                                    $id = "http://yourdomain.com/schemas/myschema.json",
                                    dependentSchemas = {
                                            @StringToClassMapItem(
                                                    key = "pet",
                                                    value = Pet.class
                                            )
                                    },
                                    patternProperties = {
                                            @StringToClassMapItem(
                                                    key = "user",
                                                    value = User.class
                                            )
                                    },
                                    properties = {
                                            @StringToClassMapItem(
                                                    key = "extraObject",
                                                    value = Object.class
                                            )
                                    }
                            )
                    ))
            })
    @Consumes({"application/json", "application/xml"})
    public Parameters31Resource.SubscriptionResponse subscribe(@Parameter(description = "idParam")
                                                             @QueryParam("id") final String id) {
        return null;
    }

    public static class SubscriptionResponse {
        public String subscriptionId;
    }
}
