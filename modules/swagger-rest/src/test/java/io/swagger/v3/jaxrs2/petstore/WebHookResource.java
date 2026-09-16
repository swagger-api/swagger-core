package io.swagger.v3.jaxrs2.petstore;

import io.swagger.v3.jaxrs2.petstore.parameter.Parameters31Resource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.Webhook;
import io.swagger.v3.oas.annotations.Webhooks;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Webhooks({
        @Webhook(
                name = "webhook1",
                operation = @Operation(operationId = "subscribe",
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
                                                types = {"string", "number"},
                                                exclusiveMaximumValue = 100,
                                                $schema = "parameter $schema",
                                                $anchor = "parameter $anchor",
                                                _if = Parameters31Resource.SubscriptionResponse.class,
                                                _else = Parameters31Resource.SubscriptionResponse.class,
                                                then = Parameters31Resource.SubscriptionResponse.class
                                        )
                                )
                        }
                )
        ),
        @Webhook(
                name = "webhook2",
                operation = @Operation(
                        operationId = "getAllReviews",
                        summary = "get all the reviews",
                        method = "get",
                        responses = @ApiResponse(
                                responseCode = "200",
                                description = "successful operation",
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                $id = "http://yourdomain.com/schemas/myschema.json",
                                                $comment = "random comment",
                                                types = { "integer", "number" },
                                                dependentSchemas = {
                                                        @StringToClassMapItem(
                                                                key = "pet",
                                                                value = Object.class
                                                        )
                                                },
                                                patternProperties = {
                                                        @StringToClassMapItem(
                                                                key = "user",
                                                                value = Object.class
                                                        )
                                                },
                                                format = "int32"
                                        )
                                )
                        )
                )
        )

})

public class WebHookResource {
}
