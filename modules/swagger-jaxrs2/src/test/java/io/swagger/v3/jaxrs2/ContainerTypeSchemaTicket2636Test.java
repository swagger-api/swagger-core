package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.HashMap;

public class ContainerTypeSchemaTicket2636Test extends AbstractAnnotationTest {

    @Test
    public void testContainerTypeSchemaTicket2636() throws Exception {
        String expectedYaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /path:\n" +
                "    get:\n" +
                "      summary: Op\n" +
                "      description: 'RequestBody contains a Schema class that extends a Map '\n" +
                "      operationId: getWithNoParameters\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/MyModel'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: voila!\n" +
                "components:\n" +
                "  schemas:\n" +
                "    MyModel:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        empty:\n" +
                "          type: boolean\n" +
                "      additionalProperties:\n" +
                "        type: string";
        compareAsYaml(RequestBodyInheritanceModelIssue.class, expectedYaml);
    }

    static class RequestBodyInheritanceModelIssue {
        @Operation(
                summary = "Op",
                description = "RequestBody contains a Schema class that extends a Map ",
                operationId = "getWithNoParameters",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                })
        @GET
        @Path("/path")
        public void simpleGet(@RequestBody(required=true,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyModel.class)))
                                      String data) {
        }
    }

    class MyModel extends HashMap<String, String> {

    }
}
