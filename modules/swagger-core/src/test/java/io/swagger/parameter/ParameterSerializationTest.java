package io.swagger.parameter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.ArrayModel;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ParameterSerializationTest {
    private final ObjectMapper m = Json.mapper();

    @Test(description = "it should serialize a QueryParameter")
    public void serializeQueryParameter() {
        final QueryParameter p = new QueryParameter().property(new StringProperty());
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"string\"}";
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
        final QueryParameter p = new QueryParameter()
                .type(ArrayProperty.TYPE)
                .items(new StringProperty())
                .collectionFormat("multi");
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
        final PathParameter p = new PathParameter().property(new StringProperty());
        final String json = "{\"in\":\"path\",\"required\":true,\"type\":\"string\"}";
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
        PathParameter p = new PathParameter()
                .type(ArrayProperty.TYPE)
                .items(new StringProperty())
                .collectionFormat("multi");
        final String json = "{" +
                "   \"in\":\"path\"," +
                "   \"required\":true," +
                "   \"type\":\"array\"," +
                "   \"items\":{" +
                "      \"type\":\"string\"" +
                "   }," +
                "   \"collectionFormat\":\"multi\"" +
                "}";
        SerializationMatchers.assertEqualsToJson(p, json);

        final String yaml = "---\n" +
                "in: \"path\"\n" +
                "required: true\n" +
                "type: \"array\"\n" +
                "items:\n" +
                "  type: \"string\"\n" +
                "collectionFormat: \"multi\"";
        SerializationMatchers.assertEqualsToYaml(p, yaml);
    }

    @Test(description = "it should deserialize a PathParameter with string array")
    public void deserializeStringArrayPathParameter() throws IOException {
        final String json = "{" +
                "   \"in\":\"path\"," +
                "   \"required\":true," +
                "   \"type\":\"array\"," +
                "   \"items\":{" +
                "      \"type\":\"string\"" +
                "   }," +
                "   \"collectionFormat\":\"multi\"" +
                "}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize a PathParameter with integer array")
    public void serializeIntegerArrayPathParameter() {
        final PathParameter p = new PathParameter()
                .type(ArrayProperty.TYPE)
                .items(new IntegerProperty())
                .collectionFormat("multi");
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
        final HeaderParameter p = new HeaderParameter().property(new StringProperty());
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
        final HeaderParameter p = new HeaderParameter()
                .type(ArrayProperty.TYPE)
                .property(new StringProperty())
                .collectionFormat("multi");
        final String json = "{\"in\":\"header\",\"required\":false,\"type\":\"string\",\"collectionFormat\":\"multi\"}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a string array HeaderParameter")
    public void deserializeStringArrayHeaderParameter() throws IOException {
        final String json = "{\"in\":\"header\",\"required\":true,\"type\":\"string\",\"collectionFormat\":\"multi\"}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize a BodyParameter")
    public void serializeBodyParameter() {
        final ModelImpl model = new ModelImpl()
                .name("Cat")
                .property("name", new StringProperty());
        final BodyParameter p = new BodyParameter().schema(model);
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

    @Test(description = "it should serialize a BodyParameter to yaml")
    public void serializeBodyParameterToYaml() {
        final ModelImpl model = new ModelImpl()
                .name("Cat")
                .property("name", new StringProperty());
        final BodyParameter p = new BodyParameter().schema(model);
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

    @Test(description = "it should deserialize a read only parameter")
    public void deserializeReadOnlyParameter() throws IOException {
        final String json =
                "{" +
                "   \"in\":\"path\"," +
                "   \"required\":false," +
                "   \"type\":\"string\"," +
                "   \"readOnly\":\"true\"" +
                "}";
        final Parameter p = m.readValue(json, Parameter.class);
        assertTrue(p.isReadOnly());
    }

    @Test(description = "it should serialize a ref BodyParameter")
    public void serializeRefBodyParameter() {
        final RefModel model = new RefModel("Cat");
        final BodyParameter p = new BodyParameter().schema(model);
        final String json = "{\"in\":\"body\",\"required\":false,\"schema\":{\"$ref\":\"#/definitions/Cat\"}}";
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize a ref BodyParameter")
    public void deserializeRefBodyParameter() throws IOException {
        final String json = "{\"in\":\"body\",\"required\":false,\"schema\":{\"$ref\":\"#/definitions/Cat\"}}";
        final Parameter p = m.readValue(json, Parameter.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize an array BodyParameter")
    public void serializeArrayBodyParameter() {
        final ArrayModel model = new ArrayModel().items(new RefProperty("Cat"));
        final BodyParameter p = new BodyParameter().schema(model);
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

    @Test(description = "it should serialize a path parameter with enum")
    public void serializeEnumPathParameter() {
        PathParameter p = new PathParameter()
                .items(new StringProperty())
                ._enum(Arrays.asList("a", "b", "c"));
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

        assertEquals(((PathParameter) p).getEnum(), Arrays.asList("a", "b", "c"));
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

        assertEquals(((PathParameter) p).getEnumValue(), Arrays.asList(1,2,3));
    }

    @Test(description = "should serialize correctly typed numeric enums")
    public void testIssue1765() throws Exception {
        String yaml =
                "swagger: '2.0'\n" +
                        "paths:\n" +
                        "  /test:\n" +
                        "    get:\n" +
                        "      parameters:\n" +
                        "      - name: \"days\"\n" +
                        "        in: \"path\"\n" +
                        "        required: true\n" +
                        "        type: \"integer\"\n" +
                        "        format: \"int32\"\n" +
                        "        enum:\n" +
                        "        - 1\n" +
                        "        - 2\n" +
                        "        - 3\n" +
                        "        - 4\n" +
                        "        - 5\n" +
                        "      responses:\n" +
                        "        default:\n" +
                        "          description: great";

        Swagger swagger = Yaml.mapper().readValue(yaml, Swagger.class);
        SerializationMatchers.assertEqualsToYaml(swagger, yaml);
    }
    @Test(description = "should serialize string value")
    public void testStringValue() {
        final QueryParameter param = new QueryParameter();
        param.setDefaultValue("false");
        param.setType("string");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"string\",\"default\":\"false\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize boolean value")
    public void testBooleanValue() {
        final QueryParameter param = new QueryParameter();
        param.setDefaultValue("false");
        param.setType("boolean");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"boolean\",\"default\":false}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize long value")
    public void testLongValue() {
        final QueryParameter param = new QueryParameter();
        param.setDefaultValue("1234");
        param.setType("integer");
        param.setFormat("1nt64");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"integer\",\"default\":1234,\"format\":\"1nt64\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize double value")
    public void testDoubleValue() {
        final QueryParameter param = new QueryParameter();
        param.setDefaultValue("12.34");
        param.setType("number");
        param.setFormat("double");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"number\",\"default\":12.34,\"format\":\"double\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize double value")
    public void testFloatValue() {
        final QueryParameter param = new QueryParameter();
        param.setDefaultValue("12.34");
        param.setType("number");
        param.setFormat("float");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"number\",\"default\":12.34,\"format\":\"float\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize incorrect boolean value as string")
    public void testIncorrectBoolean() {
        final QueryParameter param = new QueryParameter();
        param.setDefaultValue("test");
        param.setType("boolean");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"boolean\",\"default\":\"test\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize incorrect long value as string")
    public void testIncorrectLong() {
        final QueryParameter param = new QueryParameter();
        param.setDefaultValue("test");
        param.setType("integer");
        param.setFormat("1nt64");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"integer\",\"default\":\"test\",\"format\":\"1nt64\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should serialize incorrect double value as string")
    public void testIncorrectDouble() {
        final QueryParameter param = new QueryParameter();
        param.setDefaultValue("test");
        param.setType("number");
        param.setFormat("double");
        final String json = "{\"in\":\"query\",\"required\":false,\"type\":\"number\",\"default\":\"test\",\"format\":\"double\"}";
        SerializationMatchers.assertEqualsToJson(param, json);
    }

    @Test(description = "should mark a parameter as readOnly")
    public void testReadOnlyParameter() throws Exception {
        final QueryParameter qp = new QueryParameter().readOnly(true);
        final String json = "{\"in\":\"query\",\"required\":false,\"readOnly\":true}";
        SerializationMatchers.assertEqualsToJson(qp, json);
    }

    @Test(description = "should mark a parameter as to allow empty value")
    public void testAllowEmptyValueParameter() throws Exception {
        final QueryParameter qp = new QueryParameter().allowEmptyValue(true);
        final String json = "{\"in\":\"query\",\"required\":false,\"allowEmptyValue\":true}";
        SerializationMatchers.assertEqualsToJson(qp, json);
    }
}
