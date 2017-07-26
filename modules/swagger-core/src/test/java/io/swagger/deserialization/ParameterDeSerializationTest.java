package io.swagger.deserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.IntegerSchema;
import io.swagger.oas.models.media.StringSchema;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ParameterDeSerializationTest {
    private final ObjectMapper m = Json.mapper();

    @Test(description = "it should deserialize a QueryParameter")
    public void deserializeQueryParameter() throws IOException {
        final String json = "{\"in\":\"query\",\"required\":false,\"schema\":{\"type\":\"string\"}}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a QueryParameter with array")
    public void deserializeArrayQueryParameter() throws IOException {
        final String json = "{" +
                "   \"in\":\"query\"," +
                "   \"required\":false," +
                "   \"schema\":{" +
                "     \"type\":\"array\"," +
                "     \"items\":{" +
                "        \"type\":\"string\"" +
                "     }" +
                "   }" +
                "}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a PathParameter")
    public void deserializePathParameter() throws IOException {
        final String json = "{\"in\":\"query\",\"required\":true,\"schema\":{\"type\":\"string\"}}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
        assertTrue(p.getRequired());
    }

    @Test(description = "it should deserialize a PathParameter with string array")
    public void deserializeStringArrayPathParameter() throws IOException {
        final String json = "{\"in\":\"path\",\"required\":true,\"schema\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}}}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a PathParameter with integer array ")
    public void deserializeIntegerArrayPathParameter() throws IOException {
        final String json = "{" +
                "   \"in\":\"path\"," +
                "   \"required\":true," +
                "   \"schema\":{" +
                "   \"type\":\"array\"," +
                "   \"items\":{" +
                "      \"type\":\"integer\"," +
                "      \"format\":\"int32\"" +
                "   }}" +
                "}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a HeaderParameter")
    public void deserializeHeaderParameter() throws IOException {
        final String json = "{\"in\":\"header\",\"required\":true,\"schema\":{\"type\":\"string\"}}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a string array HeaderParameter")
    public void deserializeStringArrayHeaderParameter() throws IOException {
        final String json = "{\"in\":\"header\",\"required\":true,\"schema\":{\"type\":\"string\"}}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a BodyParameter")
    public void deserializeBodyParameter() throws IOException {

        final String json =
                "{\"content\":{" +
                "  \"*/*\":{" +
                "    \"schema\":{" +
                "      \"type\":\"string\"}}}}";
        final RequestBody p = m.readValue(json, RequestBody.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a read only parameter")
    public void deserializeReadOnlyParameter() throws IOException {
        final String json =
                "{\"in\":\"path\"," +
                "\"content\":{" +
                        "  \"*/*\":{" +
                        "    \"schema\":{" +
                        "      \"type\":\"string\"," +
                        "      \"readOnly\":true}}}}";
        final Parameter p = m.readValue(json, Parameter.class);
        assertTrue(p.getContent().get("*/*").getSchema().getReadOnly());
    }

    @Test(description = "it should deserialize an array BodyParameter")
    public void deserializeArrayBodyParameter() throws IOException {
        final String json = "{\"content\":{\"*/*\":{\"schema\":{\"type\":\"array\",\"items\":{\"$ref\":\"#/definitions/Cat\"}}}}}";
        final RequestBody p = m.readValue(json, RequestBody.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }


    @Test(description = "it should deserialize a path parameter with enum")
    public void deserializeEnumPathParameter() throws IOException {
        final String json = "{" +
                "   \"in\":\"path\"," +
                "   \"required\":true," +
                "   \"schema\":{" +
                "     \"type\":\"array\"," +
                "     \"items\":{" +
                "        \"type\":\"string\"," +
                "        \"enum\":[\"a\",\"b\",\"c\"]" +
                "     }" +
                "}}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);

        ArraySchema as = (ArraySchema)p.getSchema();
        assertEquals(((StringSchema)as.getItems()).getEnum(), Arrays.asList("a", "b", "c"));
    }

    @Test(description = "it should deserialize a number path parameter with enum")
    public void deserializeNumberEnumPathParameter() throws IOException {
        final String json = "{" +
                "   \"in\":\"path\"," +
                "   \"required\":true," +
                "   \"schema\":{" +
                "     \"type\":\"array\"," +
                "     \"items\":{" +
                "        \"type\":\"integer\"," +
                "        \"format\":\"int32\"," +
                "        \"enum\":[1,2,3]" +
                "     }" +
                "   }" +
                "}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);

        assertEquals(((IntegerSchema)((ArraySchema) p.getSchema()).getItems()).getEnum(), Arrays.asList(1,2,3));
    }

    @Test(description = "should serialize correctly typed numeric enums")
    public void testIssue1765() throws Exception {
        String yaml =
                "openapi: '3.0.0-rc1'\n" +
                "paths:\n" +
                "  /test:\n" +
                "    get:\n" +
                "      parameters:\n" +
                "      - name: \"days\"\n" +
                "        in: \"path\"\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: \"integer\"\n" +
                "          format: \"int32\"\n" +
                "          enum:\n" +
                "          - 1\n" +
                "          - 2\n" +
                "          - 3\n" +
                "          - 4\n" +
                "          - 5\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: great";

        OpenAPI swagger = Yaml.mapper().readValue(yaml, OpenAPI.class);
        SerializationMatchers.assertEqualsToYaml(swagger, yaml);
    }
}
