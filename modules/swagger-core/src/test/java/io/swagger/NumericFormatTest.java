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
	
	private static final String NEWLINE = System.getProperty("line.separator");
	
    @Test
    public void testFormatOfInteger() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(ModelWithIntegerFields.class);
        assertEquals(models.size(), 1);

        String json = Json.pretty(models);
        assertEquals(json,
            "{" + NEWLINE  +
            "  \"ModelWithIntegerFields\" : {" + NEWLINE  +
            "    \"type\" : \"object\"," + NEWLINE +
            "    \"properties\" : {" + NEWLINE  +
            "      \"id\" : {" + NEWLINE  +
            "        \"type\" : \"integer\"," + NEWLINE  +
            "        \"format\" : \"int32\"," + NEWLINE  +
            "        \"minimum\" : 3" + NEWLINE  +
            "      }" + NEWLINE  +
            "    }" + NEWLINE  +
            "  }" + NEWLINE  +
            "}");
    }

    @Test
    public void testFormatOfDecimal() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(ModelWithDecimalFields.class);
        assertEquals(models.size(), 1);

        String json = Json.pretty(models);
        assertEquals(json,
            "{" + NEWLINE  +
            "  \"ModelWithDecimalFields\" : {" + NEWLINE  +
            "    \"type\" : \"object\"," + NEWLINE  +
            "    \"properties\" : {" + NEWLINE  +
            "      \"id\" : {" + NEWLINE  +
            "        \"type\" : \"number\"," + NEWLINE  +
            "        \"format\" : \"double\"," + NEWLINE  +
            "        \"minimum\" : 3.3," + NEWLINE  +
            "        \"exclusiveMinimum\" : false" + NEWLINE  +
            "      }" + NEWLINE  +
            "    }" + NEWLINE  +
            "  }" + NEWLINE  +
            "}");
    }

    @Test
    public void testFormatOfBigDecimal() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(ModelWithoutScientificFields.class);
        assertEquals(models.size(), 1);

        String json = Json.pretty(models);

        assertEquals(json,
            "{" + NEWLINE  +
            "  \"ModelWithoutScientificFields\" : {" + NEWLINE  +
            "    \"type\" : \"object\"," + NEWLINE  +
            "    \"properties\" : {" + NEWLINE  +
            "      \"id\" : {" + NEWLINE  +
            "        \"type\" : \"number\"," + NEWLINE  +
            "        \"minimum\" : -9999999999999999.99," + NEWLINE  +
            "        \"maximum\" : 9999999999999999.99," + NEWLINE  +
            "        \"exclusiveMinimum\" : false," + NEWLINE  +
            "        \"exclusiveMaximum\" : false" + NEWLINE  +
            "      }" + NEWLINE  +
            "    }" + NEWLINE  +
            "  }" + NEWLINE  +
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
