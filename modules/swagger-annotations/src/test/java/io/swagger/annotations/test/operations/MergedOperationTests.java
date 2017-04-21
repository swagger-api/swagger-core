package io.swagger.annotations.test.operations;

import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;

import static org.testng.Assert.assertEquals;

public class MergedOperationTests {
    public String readIntoYaml(Class<?> cls) {
        // TODO: we will scan the ClassWithTitle and write as YAML but for now, stubbing it out to show the
        // expected test behavior
        return "nope!";
    }

    @Test(enabled = false, description = "shows a response when no annotation is present")
    public void testUnannotatedMethod() {
        String yaml = readIntoYaml(UnannotatedMethodClass.class);

        assertEquals(yaml,
            "get:\n" +
            "  operationId: getSimpleResponse\n" +
            "  parameters: []\n" +
            "  responses:\n" +
            "    default:\n" +
            "      content:\n" +
            "        */*:\n" +
            "          schema:\n" +
            "            type: object\n" +
            "            properties:\n" +
            "              id:\n" +
            "                type: string\n");
    }

    static class UnannotatedMethodClass {
        @GET
        public SimpleResponse getSimpleResponse() {
            return null;
        }
    }

    static class SimpleResponse {
        private String id;
    }

    @Test(enabled = false, description = "")
    public void testAnnotatedParameters() {
        String yaml = readIntoYaml(MethodWithParameters.class);

        assertEquals(yaml,
            "get:\n" +
            "  operationId: getSimpleResponse\n" +
            "  parameters:\n" +
            "    - in: query\n" +
            "      name: id\n" +
            "      schema:\n" +
            "        type: string\n" +
            "    - in: header\n" +
            "      name: x-authorized-by\n" +
            "      style: simple\n" +
            "      schema:\n" +
            "        type: array\n" +
            "        items:\n" +
            "          type: string\n" +
            "\n" +
            "  responses:\n" +
            "    default:\n" +
            "      content:\n" +
            "        */*:\n" +
            "          schema:\n" +
            "            type: object\n" +
            "            properties:\n" +
            "              id:\n" +
            "                type: string");
    }

    static class MethodWithParameters {
        @GET
        public SimpleResponse getSimpleResponseWithParameters(
                @QueryParam("id") String id,
                @HeaderParam("x-authorized-by") String[] auth) {
            return null;
        }
    }
}
