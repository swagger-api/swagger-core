package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;

import static io.swagger.v3.core.util.TestUtils.normalizeLineEnds;
import static org.testng.Assert.assertEquals;

import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

public class NumericFormatTest {
    @Test
    public void testFormatOfInteger() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithIntegerFields.class);
        assertEquals(models.size(), 1);

        String json1 = Json.pretty(models);
        String json2 = 
                "{\n" +
                        "  \"ModelWithIntegerFields\" : {\n" +
                        "    \"type\" : \"object\",\n" +
                        "    \"properties\" : {\n" +
                        "      \"id\" : {\n" +
                        "        \"minimum\" : 3,\n" +
                        "        \"type\" : \"integer\",\n" +
                        "        \"format\" : \"int32\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";
        JSONObject jsonObj1 = new JSONObject(json1);
        JSONObject jsonObj2 = new JSONObject(json2);

        JSONAssert.assertEquals(jsonObj1, jsonObj2, true);
    }

    @Test
    public void testFormatOfDecimal() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithDecimalFields.class);
        assertEquals(models.size(), 1);

        String json1 = Json.pretty(models);
        String json2 = 
                "{\n" +
                        "  \"ModelWithDecimalFields\" : {\n" +
                        "    \"type\" : \"object\",\n" +
                        "    \"properties\" : {\n" +
                        "      \"id\" : {\n" +
                        "        \"minimum\" : 3.3,\n" +
                        "        \"exclusiveMinimum\" : false,\n" +
                        "        \"type\" : \"number\",\n" +
                        "        \"format\" : \"double\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";
        JSONObject jsonObj1 = new JSONObject(json1);
        JSONObject jsonObj2 = new JSONObject(json2);

        JSONAssert.assertEquals(jsonObj1, jsonObj2, true);
    }

    @Test
    public void testFormatOfBigDecimal() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithoutScientificFields.class);
        assertEquals(models.size(), 1);

        String json1 = Json.pretty(models);

        String json2 = 
                "{\n" +
                        "  \"ModelWithoutScientificFields\" : {\n" +
                        "    \"type\" : \"object\",\n" +
                        "    \"properties\" : {\n" +
                        "      \"id\" : {\n" +
                        "        \"maximum\" : 9999999999999999.99,\n" +
                        "        \"exclusiveMaximum\" : false,\n" +
                        "        \"minimum\" : -9999999999999999.99,\n" +
                        "        \"exclusiveMinimum\" : false,\n" +
                        "        \"type\" : \"number\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";
        JSONObject jsonObj1 = new JSONObject(json1);
        JSONObject jsonObj2 = new JSONObject(json2);

        JSONAssert.assertEquals(jsonObj1, jsonObj2, true);

    }

    static class ModelWithIntegerFields {
        @io.swagger.v3.oas.annotations.media.Schema
        @Min(value = 3)
        public Integer id;
    }

    static class ModelWithDecimalFields {
        @io.swagger.v3.oas.annotations.media.Schema
        @DecimalMin(value = "3.3")
        public Double id;
    }

    static class ModelWithoutScientificFields {
        @io.swagger.v3.oas.annotations.media.Schema
        @DecimalMin(value = "-9999999999999999.99")
        @DecimalMax(value = "9999999999999999.99")
        public BigDecimal id;
    }
}
