package io.swagger.parameter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.OpenAPI;
import io.swagger.models.media.ArraySchema;
import io.swagger.models.media.Content;
import io.swagger.models.media.IntegerSchema;
import io.swagger.models.media.MediaType;
import io.swagger.models.media.NumberSchema;
import io.swagger.models.media.Schema;
import io.swagger.models.media.StringSchema;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.parameters.RequestBody;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ParameterSerializationTest {
    private final ObjectMapper m = Json.mapper();

    @Test(description = "it should serialize a QueryParameter")
    public void serializeQueryParameter() {
        final Parameter p = new QueryParameter()
                .schema(new StringSchema());
        final String json = "{\"in\":\"query\",\"schema\":{\"type\":\"string\"}}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a QueryParameter")
    public void deserializeQueryParameter() throws IOException {
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"string\"}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize a QueryParameter with array")
    public void serializeArrayQueryParameter() {
        final Parameter p = new QueryParameter()
                .schema(new ArraySchema()
                    .items(new StringSchema()));
        final String json = "{" +
                "   \"in\":\"query\"," +
                "   \"required\":false," +
                "   \"type\":\"array\"," +
                "   \"items\":{" +
                "      \"type\":\"string\"" +
                "   }," +
                "   \"collectionFormat\":\"multi\"" +
                "}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a QueryParameter with array")
    public void deserializeArrayQueryParameter() throws IOException {
        final String json = "{" +
                "   \"in\":\"query\"," +
                "   \"required\":false," +
                "   \"type\":\"array\"," +
                "   \"items\":{" +
                "      \"type\":\"string\"" +
                "   }," +
                "   \"collectionFormat\":\"multi\"" +
                "}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize a PathParameter")
    public void serializePathParameter() {
        final Parameter p = new PathParameter().schema(new StringSchema());
        final String json = "{\"in\":\"path\",\"schema\":{\"type\":\"string\"}}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a PathParameter")
    public void deserializePathParameter() throws IOException {
        final String json = "{\"in\":\"query\",\"required\":true,\"type\":\"string\"}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
        assertTrue(p.getRequired());
    }

    @Test(description = "it should serialize a PathParameter with string array")
    public void serializeStringArrayPathParameter() {
        Parameter p = new PathParameter()
                .schema(new ArraySchema()
                    .items(new StringSchema()));
        final String json = "{\"in\":\"path\",\"schema\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}}}";
        SerializationMatchers.assertEqualsToJson(p, json);

        final String yaml = "---\n" +
                "in: \"path\"\n" +
                "schema:\n" +
                "  type: \"array\"\n" +
                "  items:\n" +
                "    type: \"string\"";
        SerializationMatchers.assertEqualsToYaml(p, yaml);
    }

    @Test(description = "it should deserialize a PathParameter with string array")
    public void deserializeStringArrayPathParameter() throws IOException {
        final String json = "{\"in\":\"path\",\"schema\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}}}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize a PathParameter with integer array")
    public void serializeIntegerArrayPathParameter() {
        final Parameter p = new PathParameter()
                .schema(new ArraySchema().items(new IntegerSchema()));
        final String json = "{" +
                "   \"in\":\"path\"," +
                "   \"required\":true," +
                "   \"type\":\"array\"," +
                "   \"items\":{" +
                "      \"type\":\"integer\"," +
                "      \"format\":\"int32\"" +
                "   }," +
                "   \"collectionFormat\":\"multi\"" +
                "}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a PathParameter with integer array ")
    public void deserializeIntegerArrayPathParameter() throws IOException {
        final String json = "{" +
                "   \"in\":\"path\"," +
                "   \"required\":true," +
                "   \"type\":\"array\"," +
                "   \"items\":{" +
                "      \"type\":\"integer\"," +
                "      \"format\":\"int32\"" +
                "   }," +
                "   \"collectionFormat\":\"multi\"" +
                "}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should it should serialize a HeaderParameter")
    public void serializeHeaderParameter() {
        final Parameter p = new HeaderParameter()
                .schema(new StringSchema());
        final String json = "{\"in\":\"header\",\"required\":false,\"type\":\"string\"}";
        SerializationMatchers.assertEqualsToJson(p, json);
        final String yaml = "---\n" +
                "in: \"header\"\n" +
                "required: false\n" +
                "type: \"string\"";
        SerializationMatchers.assertEqualsToYaml(p, yaml);
    }

    @Test(description = "it should deserialize a HeaderParameter")
    public void deserializeHeaderParameter() throws IOException {
        final String json = "{\"in\":\"header\",\"required\":true,\"type\":\"string\"}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize a string array HeaderParameter")
    public void serializeStringArrayHeaderParameter() {
        final Parameter p = new HeaderParameter()
                .schema(new ArraySchema()
                        .items(new StringSchema()));
        final String json = "{\"in\":\"header\",\"schema\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}}}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a string array HeaderParameter")
    public void deserializeStringArrayHeaderParameter() throws IOException {
        final String json = "{\"in\":\"header\",\"required\":true,\"type\":\"string\",\"collectionFormat\":\"multi\"}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(enabled = false, description = "it should serialize a BodyParameter")
    public void serializeBodyParameter() {
        final Schema model = new Schema()
                .title("Cat")
                .addProperties("name", new StringSchema());
        final RequestBody p = new RequestBody()
                .content(new Content().addMediaType("*/*",
                        new MediaType().schema(model)));

        final String json = "{" +
                "   \"in\":\"body\"," +
                "   \"required\":false," +
                "   \"schema\":{" +
                "      \"properties\":{" +
                "         \"name\":{" +
                "            \"type\":\"string\"" +
                "         }" +
                "      }" +
                "   }" +
                "}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(enabled = false, description = "it should serialize a BodyParameter to yaml")
    public void serializeBodyParameterToYaml() {
        final Schema model = new Schema()
                .title("Cat")
                .addProperties("name", new StringSchema());
        final RequestBody p = new RequestBody()
                .content(new Content().addMediaType("*/*",
                        new MediaType().schema(model)));
        final String yaml = "---\n" +
                "in: \"body\"\n" +
                "required: false\n" +
                "schema:\n" +
                "  properties:\n" +
                "    name:\n" +
                "      type: \"string\"";
        SerializationMatchers.assertEqualsToYaml(p, yaml);
    }

    @Test(description = "it should deserialize a BodyParameter")
    public void deserializeBodyParameter() throws IOException {
        final String json = "{" +
                "   \"in\":\"body\"," +
                "   \"required\":false," +
                "   \"schema\":{" +
                "      \"properties\":{" +
                "         \"name\":{" +
                "            \"type\":\"string\"" +
                "         }" +
                "      }" +
                "   }" +
                "}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(enabled = false, description = "it should deserialize a read only parameter")
    public void deserializeReadOnlyParameter() throws IOException {
        final String json =
                "{" +
                "   \"in\":\"path\"," +
                "   \"required\":false," +
                "   \"type\":\"string\"," +
                "   \"readOnly\":\"true\"" +
                "}";
        final Parameter p = m.readValue(json, Parameter.class);
        assertTrue(p.getSchema().getReadOnly());
    }

    @Test(description = "it should serialize a ref BodyParameter")
    public void serializeRefBodyParameter() {
        final Schema model = new Schema().ref("#/definitions/Cat");
        final RequestBody p = new RequestBody()
                .content(new Content().addMediaType("*/*",
                        new MediaType().schema(model)));

        final String json = "{\"content\":{\"*/*\":{\"schema\":{\"ref\":\"#/definitions/Cat\"}}}}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a ref BodyParameter")
    public void deserializeRefBodyParameter() throws IOException {
        final String json = "{\"in\":\"body\",\"required\":false,\"schema\":{\"$ref\":\"#/definitions/Cat\"}}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(enabled = false, description = "it should serialize an array BodyParameter")
    public void serializeArrayBodyParameter() {
        final Schema model = new ArraySchema().items(new Schema().ref("#/definitions/Cat"));
        final RequestBody p = new RequestBody()
                .content(new Content().addMediaType("*/*",
                        new MediaType().schema(model)));
        final String json = "{" +
                "   \"in\":\"body\"," +
                "   \"required\":false," +
                "   \"schema\":{" +
                "      \"type\":\"array\"," +
                "      \"items\":{" +
                "         \"$ref\":\"#/definitions/Cat\"" +
                "      }" +
                "   }" +
                "}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize an array BodyParameter")
    public void deserializeArrayBodyParameter() throws IOException {
        final String json = "{" +
                "   \"in\":\"body\"," +
                "   \"required\":false," +
                "   \"schema\":{" +
                "      \"type\":\"array\"," +
                "      \"items\":{" +
                "         \"$ref\":\"#/definitions/Cat\"" +
                "      }" +
                "   }" +
                "}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(enabled = false, description = "it should serialize a path parameter with enum")
    public void serializeEnumPathParameter() {
        List<Object> values = new ArrayList<>();
        values.add("a");
        values.add("b");
        values.add("c");
        Parameter p = new PathParameter()
                .schema(new StringSchema()._enum(values));
        final String json = "{" +
                "   \"in\":\"path\"," +
                "   \"required\":true," +
                "   \"items\":{" +
                "      \"type\":\"string\"" +
                "   }," +
                "   \"enum\":[\"a\",\"b\",\"c\"]" +
                "}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a path parameter with enum")
    public void deserializeEnumPathParameter() throws IOException {
        final String json = "{" +
                "   \"in\":\"path\"," +
                "   \"required\":true," +
                "   \"items\":{" +
                "      \"type\":\"string\"" +
                "   }," +
                "   \"enum\":[\"a\",\"b\",\"c\"]" +
                "}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);

        assertEquals(((PathParameter) p).getSchema().getEnum(), Arrays.asList("a", "b", "c"));
    }

    @Test(description = "it should deserialize a number path parameter with enum")
    public void deserializeNumberEnumPathParameter() throws IOException {
        final String json = "{" +
                "   \"in\":\"path\"," +
                "   \"required\":true," +
                "   \"items\":{" +
                "      \"type\":\"integer\"" +
                "   }," +
                "   \"enum\":[1,2,3]" +
                "}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);

        assertEquals(((PathParameter) p).getSchema().getEnum(), Arrays.asList(1,2,3));
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

    @Test(enabled = false, description = "should serialize string value")
    public void testStringValue() {
        final QueryParameter param = new QueryParameter();
        Schema schema = new Schema()
                .type("string");

//        param.setDefaultValue("false");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"string\",\"default\":\"false\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(enabled = false, description = "should serialize boolean value")
    public void testBooleanValue() {
        final Parameter param = new QueryParameter()
                .schema(new Schema().type("boolean"));
//        param.setDefaultValue("false");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"boolean\",\"default\":false}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(enabled = false, description = "should serialize long value")
    public void testLongValue() {
        final QueryParameter param = new QueryParameter();
        param.setSchema(new IntegerSchema().format("int64"));
        //setDefaultValue("1234");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"integer\",\"default\":1234,\"format\":\"1nt64\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize double value")
    public void testDoubleValue() {
        final QueryParameter param = new QueryParameter();
        param.setSchema(new NumberSchema()._default(new BigDecimal("12.34")).format("double"));

        final String json = "{\"in\":\"query\",\"schema\":{\"type\":\"number\",\"format\":\"double\",\"default\":12.34}}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(enabled = false, description = "should serialize double value")
    public void testFloatValue() {
        final QueryParameter param = new QueryParameter();
        param.setSchema(new NumberSchema()
            .format("float"));
//        param.setDefaultValue("12.34");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"number\",\"default\":12.34,\"format\":\"float\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(enabled = false, description = "should serialize incorrect boolean value as string")
    public void testIncorrectBoolean() {
        final QueryParameter param = new QueryParameter();
        param.setSchema(new Schema().type("boolean"));
//        param.setDefaultValue("test");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"boolean\",\"default\":\"test\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(enabled = false, description = "should serialize incorrect long value as string")
    public void testIncorrectLong() {
        final QueryParameter param = new QueryParameter();
        param.setSchema(new IntegerSchema().format("int64"));
//        param.setDefaultValue("test");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"integer\",\"default\":\"test\",\"format\":\"1nt64\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(enabled = false, description = "should serialize incorrect double value as string")
    public void testIncorrectDouble() {
        final QueryParameter param = new QueryParameter();
        param.setSchema(new NumberSchema().format("double"));
//        param.setDefaultValue("test");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"number\",\"default\":\"test\",\"format\":\"double\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(enabled = false, description = "should mark a parameter as readOnly")
    public void testReadOnlyParameter() throws Exception {
        final QueryParameter qp = new QueryParameter();
        qp.setSchema(new StringSchema().readOnly(true));
        final String json = "{\"in\":\"query\",\"required\":false,\"readOnly\":true}";
        SerializationMatchers.assertEqualsToJson(qp, json);
    }

    @Test(description = "should mark a parameter as to allow empty value")
    public void testAllowEmptyValueParameter() throws Exception {
        final Parameter qp = new QueryParameter().allowEmptyValue(true);
        final String json = "{\"in\":\"query\",\"allowEmptyValue\":true}";
        SerializationMatchers.assertEqualsToJson(qp, json);
    }
}
