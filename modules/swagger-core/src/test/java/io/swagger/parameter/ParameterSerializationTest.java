package io.swagger.parameter;

import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.parameters.QueryParameter;

import org.testng.annotations.Test;

public class ParameterSerializationTest {

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
}
