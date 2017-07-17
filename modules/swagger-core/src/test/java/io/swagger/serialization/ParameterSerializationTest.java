package io.swagger.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.media.IntegerSchema;
import io.swagger.oas.models.media.MediaType;
import io.swagger.oas.models.media.NumberSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import io.swagger.oas.models.parameters.HeaderParameter;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.parameters.PathParameter;
import io.swagger.oas.models.parameters.QueryParameter;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Test(description = "it should serialize a QueryParameter with array")
    public void serializeArrayQueryParameter() {
        final Parameter p = new QueryParameter()
                .schema(new ArraySchema()
                    .items(new StringSchema()));
        final String json = "{" +
                "   \"in\":\"query\"," +
                "   \"schema\":{" +
                "     \"type\":\"array\"," +
                "     \"items\":{" +
                "       \"type\":\"string\"" +
                "   }}" +
                "}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize a PathParameter")
    public void serializePathParameter() {
        final Parameter p = new PathParameter().schema(new StringSchema());
        final String json = "{\"in\":\"path\",\"required\":true,\"schema\":{\"type\":\"string\"}}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize a PathParameter with string array")
    public void serializeStringArrayPathParameter() {
        Parameter p = new PathParameter()
                .schema(new ArraySchema()
                    .items(new StringSchema()));
        final String json = "{\"in\":\"path\",\"required\":true,\"schema\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}}}";
        SerializationMatchers.assertEqualsToJson(p, json);

        final String yaml = "---\n" +
                "in: \"path\"\n" +
                "required: true\n" +
                "schema:\n" +
                "  type: \"array\"\n" +
                "  items:\n" +
                "    type: \"string\"";
        SerializationMatchers.assertEqualsToYaml(p, yaml);
    }

    @Test(description = "it should serialize a PathParameter with integer array")
    public void serializeIntegerArrayPathParameter() {
        final Parameter p = new PathParameter()
                .schema(new ArraySchema().items(new IntegerSchema()));
        final String json = "{\"in\":\"path\",\"required\":true,\"schema\":{\"type\":\"array\",\"items\":{\"type\":\"integer\",\"format\":\"int32\"}}}\n";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should it should serialize a HeaderParameter")
    public void serializeHeaderParameter() {
        final Parameter p = new HeaderParameter()
                .schema(new StringSchema());
        final String json = "{\"in\":\"header\",\"schema\":{\"type\":\"string\"}}";
        SerializationMatchers.assertEqualsToJson(p, json);
        final String yaml = "---\n" +
                "in: \"header\"\n" +
                "schema:\n" +
                "  type: \"string\"";
        SerializationMatchers.assertEqualsToYaml(p, yaml);
    }

    @Test(description = "it should serialize a string array HeaderParameter")
    public void serializeStringArrayHeaderParameter() {
        final Parameter p = new HeaderParameter()
                .schema(new ArraySchema()
                        .items(new StringSchema()));
        final String json = "{\"in\":\"header\",\"schema\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}}}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize a BodyParameter")
    public void serializeBodyParameter() {
        final Schema model = new Schema()
                .title("Cat")
                .addProperties("name", new StringSchema());
        final RequestBody p = new RequestBody()
                .content(new Content().addMediaType("*/*",
                        new MediaType().schema(model)));

        final String json = "{\"content\":{\"*/*\":{\"schema\":{\"title\":\"Cat\",\"properties\":{\"name\":{\"type\":\"string\"}}}}}}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize a BodyParameter to yaml")
    public void serializeBodyParameterToYaml() {
        final Schema model = new Schema()
                .title("Cat")
                .addProperties("name", new StringSchema());
        final RequestBody p = new RequestBody()
                .content(new Content().addMediaType("*/*",
                        new MediaType().schema(model)));
        final String yaml = "---\n" +
                "content:\n" +
                "  '*/*':\n" +
                "    schema:\n" +
                "      title: Cat\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string";
        SerializationMatchers.assertEqualsToYaml(p, yaml);
    }

    @Test(description = "it should serialize a ref BodyParameter")
    public void serializeRefBodyParameter() {
        final Schema model = new Schema().$ref("#/definitions/Cat");
        final RequestBody p = new RequestBody()
                .content(new Content().addMediaType("*/*",
                        new MediaType().schema(model)));

        final String json = "{\"content\":{\"*/*\":{\"schema\":{\"$ref\":\"#/definitions/Cat\"}}}}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize an array BodyParameter")
    public void serializeArrayBodyParameter() {
        final Schema model = new ArraySchema().items(new Schema().$ref("#/definitions/Cat"));
        final RequestBody p = new RequestBody()
                .content(new Content().addMediaType("*/*",
                        new MediaType().schema(model)));
        final String json = "{\"content\":{\"*/*\":{\"schema\":{\"type\":\"array\",\"items\":{\"$ref\":\"#/definitions/Cat\"}}}}}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize a path parameter with enum")
    public void serializeEnumPathParameter() {
        List<String> values = new ArrayList<>();
        values.add("a");
        values.add("b");
        values.add("c");
        Parameter p = new PathParameter()
                .schema(new StringSchema()._enum(values));
        final String json = "{" +
                "   \"in\":\"path\"," +
                "   \"required\":true," +
                "   \"schema\":{" +
                "      \"type\":\"string\"," +
                "       \"enum\":[\"a\",\"b\",\"c\"]" +
                "   }" +
                "}";
        SerializationMatchers.assertEqualsToJson(p, json);
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

    @Test(description = "should serialize string value")
    public void testStringValue() {
        final QueryParameter param = (QueryParameter) new QueryParameter().required(false);
        Schema schema = new Schema()
                .type("string");
        schema.setDefault("false");

        param.setSchema(schema);
        final String json = "{" +
                "   \"in\":\"query\"," +
                "   \"required\":false," +
                "   \"schema\":{" +
                "      \"type\":\"string\"," +
                "       \"default\":\"false\"" +
                "   }" +
                "}";

        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize boolean value")
    public void testBooleanValue() {
        final QueryParameter param = (QueryParameter) new QueryParameter().required(false);
        Schema schema = new Schema()
                .type("boolean");
        schema.setDefault("false");

        param.setSchema(schema);
        final String json = "{" +
                "   \"in\":\"query\"," +
                "   \"required\":false," +
                "   \"schema\":{" +
                "      \"type\":\"boolean\"," +
                "       \"default\":\"false\"" +
                "   }" +
                "}";

        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize long value")
    public void testLongValue() {

        final QueryParameter param = (QueryParameter) new QueryParameter().required(false);
        Schema schema = new IntegerSchema().format("int64");
        schema.setDefault("1234");

        param.setSchema(schema);
        final String json = "{" +
                "   \"in\":\"query\"," +
                "   \"required\":false," +
                "   \"schema\":{" +
                "      \"type\":\"integer\"," +
                "       \"default\":1234," +
                "       \"format\":\"int64\"" +
                "   }" +
                "}";

        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize double value")
    public void testDoubleValue() {
        final QueryParameter param = new QueryParameter();
        param.setSchema(new NumberSchema()._default(new BigDecimal("12.34")).format("double"));

        final String json = "{\"in\":\"query\",\"schema\":{\"type\":\"number\",\"format\":\"double\",\"default\":12.34}}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize float value")
    public void testFloatValue() {

        final QueryParameter param = new QueryParameter();
        param.setSchema(new NumberSchema()._default(new BigDecimal("12.34")).format("float"));

        final String json = "{\"in\":\"query\",\"schema\":{\"type\":\"number\",\"format\":\"float\",\"default\":12.34}}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize incorrect boolean value as string")
    public void testIncorrectBoolean() {
        final QueryParameter param = (QueryParameter) new QueryParameter().required(false);
        Schema schema = new Schema()
                .type("boolean");
        schema.setDefault("test");

        param.setSchema(schema);
        final String json = "{" +
                "   \"in\":\"query\"," +
                "   \"required\":false," +
                "   \"schema\":{" +
                "      \"type\":\"boolean\"," +
                "       \"default\":\"test\"" +
                "   }" +
                "}";

        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should not serialize incorrect long value")
    public void testIncorrectLong() {
        final QueryParameter param = (QueryParameter) new QueryParameter().required(false);
        Schema schema = new IntegerSchema().format("int64");
        schema.setDefault("test");

        param.setSchema(schema);
        final String json = "{" +
                "   \"in\":\"query\"," +
                "   \"required\":false," +
                "   \"schema\":{" +
                "      \"type\":\"integer\"," +
                "       \"format\":\"int64\"" +
                "   }" +
                "}";

        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should not serialize incorrect double value")
    public void testIncorrectDouble() {
        final QueryParameter param = (QueryParameter) new QueryParameter().required(false);
        Schema schema = new NumberSchema().format("double");
        schema.setDefault("test");

        param.setSchema(schema);
        final String json = "{" +
                "   \"in\":\"query\"," +
                "   \"required\":false," +
                "   \"schema\":{" +
                "      \"type\":\"number\"," +
                "       \"format\":\"double\"" +
                "   }" +
                "}";

        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should mark a parameter as readOnly")
    public void testReadOnlyParameter() throws Exception {
        final QueryParameter qp = new QueryParameter();
        qp.setSchema(new StringSchema().readOnly(true));
        final String json = "{" +
                "   \"in\":\"query\"," +
                "   \"schema\":{" +
                "      \"type\":\"string\"," +
                "      \"readOnly\":true" +
                "   }" +
                "}";

        SerializationMatchers.assertEqualsToJson(qp, json);
    }

    @Test(description = "should mark a parameter as to allow empty value")
    public void testAllowEmptyValueParameter() throws Exception {
        final Parameter qp = new QueryParameter().allowEmptyValue(true);
        final String json = "{\"in\":\"query\",\"allowEmptyValue\":true}";
        SerializationMatchers.assertEqualsToJson(qp, json);
    }
}
