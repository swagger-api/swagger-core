package io.swagger;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.util.Json;
import org.testng.annotations.Test;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class NumericFormatTest {
    @Test
    public void testFormatOfInteger() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(ModelWithIntegerFields.class);
        assertEquals(models.size(), 1);

        String json = Json.pretty(models);
        assertEquals(json,
            "{\n" +
            "  \"ModelWithIntegerFields\" : {\n" +
            "    \"type\" : \"object\",\n" +
            "    \"properties\" : {\n" +
            "      \"id\" : {\n" +
            "        \"type\" : \"integer\",\n" +
            "        \"format\" : \"int32\",\n" +
            "        \"minimum\" : 3\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}");
    }

    @Test
    public void testFormatOfDecimal() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(ModelWithDecimalFields.class);
        assertEquals(models.size(), 1);

        String json = Json.pretty(models);
        assertEquals(json,
            "{\n" +
            "  \"ModelWithDecimalFields\" : {\n" +
            "    \"type\" : \"object\",\n" +
            "    \"properties\" : {\n" +
            "      \"id\" : {\n" +
            "        \"type\" : \"number\",\n" +
            "        \"format\" : \"double\",\n" +
            "        \"minimum\" : 3.3,\n" +
            "        \"exclusiveMinimum\" : false\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}");
    }

    @Test
    public void testFormatOfBigDecimal() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(ModelWithoutScientificFields.class);
        assertEquals(models.size(), 1);

        String json = Json.pretty(models);

        assertEquals(json,
            "{\n" +
            "  \"ModelWithoutScientificFields\" : {\n" +
            "    \"type\" : \"object\",\n" +
            "    \"properties\" : {\n" +
            "      \"id\" : {\n" +
            "        \"type\" : \"number\",\n" +
            "        \"minimum\" : -9999999999999999.99,\n" +
            "        \"maximum\" : 9999999999999999.99,\n" +
            "        \"exclusiveMinimum\" : false,\n" +
            "        \"exclusiveMaximum\" : false\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}");

    }
    static class ModelWithIntegerFields {
        @ApiModelProperty
        @Min(value = 3)
        public Integer id;
    }

    static class ModelWithDecimalFields {
        @ApiModelProperty
        @DecimalMin(value = "3.3")
        public Double id;
    }

    static class ModelWithoutScientificFields {
        @ApiModelProperty
        @DecimalMin(value = "-9999999999999999.99")
        @DecimalMax(value = "9999999999999999.99")
        public BigDecimal id;
    }
}
