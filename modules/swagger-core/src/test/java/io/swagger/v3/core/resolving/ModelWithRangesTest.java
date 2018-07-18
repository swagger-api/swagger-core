package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.oas.models.ModelWithRanges;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class ModelWithRangesTest {

    @Test(description = "test model with @ApiModelProperty.allowableValues")
    public void modelWithRangesTest() {
        final Map<String, Schema> properties = ModelConverters.getInstance().read(ModelWithRanges.class).get("ModelWithRanges").getProperties();

        final IntegerSchema inclusiveRange = (IntegerSchema) properties.get("inclusiveRange");
        assertEquals(inclusiveRange.getMinimum(), new BigDecimal(1));
        assertEquals(inclusiveRange.getMaximum(), new BigDecimal(5));
        assertNull(inclusiveRange.getExclusiveMaximum());
        assertNull(inclusiveRange.getExclusiveMinimum());

        final IntegerSchema exclusiveRange = (IntegerSchema) properties.get("exclusiveRange");
        assertEquals(exclusiveRange.getMinimum(), new BigDecimal(1));
        assertEquals(exclusiveRange.getMaximum(), new BigDecimal(5));
        assertEquals(exclusiveRange.getExclusiveMinimum(), Boolean.TRUE);
        assertEquals(exclusiveRange.getExclusiveMaximum(), Boolean.TRUE);

        final IntegerSchema positiveInfinityRange = (IntegerSchema) properties.get("positiveInfinityRange");
        assertEquals(positiveInfinityRange.getMinimum(), new BigDecimal(1.0));
        assertNull(positiveInfinityRange.getMaximum());
        assertNull(positiveInfinityRange.getExclusiveMaximum());
        assertNull(positiveInfinityRange.getExclusiveMinimum());

        final IntegerSchema negativeInfinityRange = (IntegerSchema) properties.get("negativeInfinityRange");
        assertNull(negativeInfinityRange.getMinimum());
        assertEquals(negativeInfinityRange.getMaximum(), new BigDecimal(5.0));
        assertNull(negativeInfinityRange.getExclusiveMaximum());
        assertNull(negativeInfinityRange.getExclusiveMinimum());

        final StringSchema stringValues = (StringSchema) properties.get("stringValues");
        assertEquals(stringValues.getEnum(), Arrays.asList("str1", "str2"));

        final NumberSchema doubleValues = (NumberSchema) properties.get("doubleValues");
        assertEquals(doubleValues.getMinimum(), new BigDecimal("1.0"));
        assertEquals(doubleValues.getMaximum(), new BigDecimal("8.0"));
        assertEquals(doubleValues.getExclusiveMaximum(), Boolean.TRUE);
        assertNull(doubleValues.getExclusiveMinimum());
    }
}
