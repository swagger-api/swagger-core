package io.swagger.annotations.test.callbacks;

import io.swagger.annotations.OASOperation;
import io.swagger.annotations.OASParameter;
import io.swagger.annotations.callbacks.OASCallback;
import io.swagger.annotations.media.OASSchema;
import io.swagger.annotations.responses.OASResponse;
import io.swagger.annotations.test.AbstractAnnotationTest;
import org.testng.annotations.Test;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.testng.Assert.assertEquals;

public class CallbackTest extends AbstractAnnotationTest {
    @Test(enabled = false)
    public void testSimpleCallback () {
        String yaml = readIntoYaml(SimpleCallback.class);

        assertEquals(yaml,
            "/test:\n" +
            "  post:\n" +
            "    description: subscribes a client to updates relevant to the requestor's account, as identified by the input token.  The supplied url will be used as the delivery address for response payloads\n" +
            "    \n" +
            "    operationId: subscribe\n" +
            "    parameters:\n" +
            "      - in: header\n" +
            "        name: x-auth-token\n" +
            "        description: the authentication token provided after initially authenticating to the application\n" +
            "        required: true\n" +
            "        schema:\n" +
            "          type: string\n" +
            "      - in: query\n" +
            "        name: url\n" +
            "        description: the URL to call with response data\n" +
            "        required: true\n" +
            "        schema:\n" +
            "          type: string\n" +
            "    responses:\n" +
            "      default:\n" +
            "        description: no description\n" +
            "          '*/*':\n" +
            "            schema:\n" +
            "              type: object\n" +
            "              properties:\n" +
            "                subscriptionId:\n" +
            "                  type: string\n" +
            "    callbacks:\n" +
            "      subscription:\n" +
            "        'http://$request.query.url':\n" +
            "          post:\n" +
            "            description: 'payload data will be sent'\n" +
            "            parameters:\n" +
            "              - in: path\n" +
            "                name: subscriptionId\n" +
            "                required: true\n" +
            "                schema:\n" +
            "                  description: the generated UUID\n" +
            "                  type: string\n" +
            "                  format: uuid\n" +
            "                  readOnly: true\n" +
            "            responses:\n" +
            "              200:\n" +
            "                description: Return this code if the callback was received and processed successfully\n" +
            "              205:\n" +
            "                description: Return this code to unsubscribe from future data updates\n" +
            "              default:\n" +
            "                description: All other response codes will disable this callback subscription");
    }

    static class SimpleCallback {
        @Path("/test")
        @POST
        @OASCallback(
            callbackUrlExpression = "http://$request.query.url",
            name = "subscription",
            operation =
                @OASOperation(
                    method = "post",
                    description = "payload data will be sent ",
                    parameters = {
                        @OASParameter(in = "path", name = "subscriptionId", required = true, schema = @OASSchema(
                            type = "string",
                            format = "uuid",
                            description = "the generated UUID",
                            readOnly = true
                        ))
                    },
                    responses = {
                        @OASResponse(
                            responseCode = "200",
                            description = "Return this code if the callback was received and processed successfully"
                        ),
                        @OASResponse(
                            responseCode = "205",
                            description = "Return this code to unsubscribe from future data updates"
                        ),
                        @OASResponse(
                            responseCode = "default",
                            description = "All other response codes will disable this callback subscription"
                        )
                    }))
        @OASOperation(description = "subscribes a client to updates relevant to the requestor's account, as " +
                "identified by the input token.  The supplied url will be used as the delivery address for response payloads")
        public SubscriptionResponse subscribe (@OASSchema(required = true, description = "the authentication token " +
                "provided after initially authenticating to the application") @HeaderParam("x-auth-token") String token,
                                               @OASSchema(required = true, description = "the URL to call with response " +
                                                       "data") @QueryParam("url") String url) {
            return null;
        }
    }

    static class SubscriptionResponse {
        private String subscriptionUuid;
    }
}
