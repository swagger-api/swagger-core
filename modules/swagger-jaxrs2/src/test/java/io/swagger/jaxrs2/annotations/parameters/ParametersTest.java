package io.swagger.jaxrs2.annotations.parameters;

import io.swagger.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.enums.Explode;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static org.testng.Assert.assertEquals;

/**
 * Created by rafaellopez on 7/10/17.
 */
public class ParametersTest extends AbstractAnnotationTest {

    @Test
    public void testSimpleCallback() {
        String openApiYAML = readIntoYaml(ParametersTest.SimpleOperations.class);
        int start = openApiYAML.indexOf("/test:");
        int end = openApiYAML.length() - 1;
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = "/test:\n" +
                "    post:\n" +
                "      description: subscribes a client to updates relevant to the requestor's account, as identified by the input token.  The supplied url will be used as the delivery address for response payloads\n" +
                "      operationId: subscribe\n" +
                "      parameters:\n" +
                "      - name: subscriptionId\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        style: SIMPLE\n" +
                "      - name: formId\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        style: FORM\n" +
                "      - name: explodeFalse\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        style: FORM\n" +
                "        explode: false\n" +
                "      - name: explodeTrue\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        style: FORM\n" +
                "        explode: true\n" +
                "      - name: explodeAvoiding\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: int\n" +
                "          description: the generated id\n" +
                "          format: id\n" +
                "          readOnly: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: no description\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SubscriptionResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        subscriptionId:\n" +
                "          type: string\n" +
                "    int:\n" +
                "      type: int\n" +
                "      description: the generated id\n" +
                "      format: id\n" +
                "      readOnly: true";
        System.out.println(expectedYAML);
        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleOperations {
        @Path("/test")
        @POST
        @Operation(
                operationId = "subscribe",
                description = "subscribes a client to updates relevant to the requestor's account, as " +
                        "identified by the input token.  The supplied url will be used as the delivery address for response payloads",
                parameters = {
                        @Parameter(in = "path", name = "subscriptionId", required = true,
                                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class)),
                        @Parameter(in = "query", name = "formId", required = true,
                                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class)),
                        @Parameter(in = "query", name = "explodeFalse", required = true, explode = Explode.FALSE,
                                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class)),
                        @Parameter(in = "query", name = "explodeTrue", required = true, explode = Explode.TRUE,
                                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class)),
                        @Parameter(in = "query", name = "explodeAvoiding", required = true, explode = Explode.TRUE,
                                schema = @Schema(
                                        type = "int",
                                        format = "id",
                                        description = "the generated id",
                                        readOnly = true
                                ))
                },
                responses = {
                        @ApiResponse(
                                responseCode = "default",
                                description = "no description", content = @Content(
                                mediaType = "*/*",
                                schema =
                                @Schema(
                                        implementation = ParametersTest.SubscriptionResponse.class)
                        ))
                })
        public ParametersTest.SubscriptionResponse subscribe() {
            return null;
        }
    }

    static class SubscriptionResponse {
        public String subscriptionId;
    }
}
