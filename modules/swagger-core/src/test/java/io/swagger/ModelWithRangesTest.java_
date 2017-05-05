package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.models.ModelWithRanges;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class ModelWithRangesTest {

    @Test(description = "test model with @ApiModelProperty.allowableValues")
    public void modelWithRangesTest() {
        final Map<String, Property> properties = ModelConverters.getInstance().read(ModelWithRanges.class).get("ModelWithRanges").getProperties();

        final IntegerProperty inclusiveRange = (IntegerProperty) properties.get("inclusiveRange");
        assertEquals(inclusiveRange.getMinimum(), new BigDecimal(1));
        assertEquals(inclusiveRange.getMaximum(), new BigDecimal(5));
        assertNull(inclusiveRange.getExclusiveMaximum());
        assertNull(inclusiveRange.getExclusiveMinimum());

        final IntegerProperty exclusiveRange = (IntegerProperty) properties.get("exclusiveRange");
        assertEquals(exclusiveRange.getMinimum(), new BigDecimal(1));
        assertEquals(exclusiveRange.getMaximum(), new BigDecimal(5));
        assertEquals(exclusiveRange.getExclusiveMinimum(), Boolean.TRUE);
        assertEquals(exclusiveRange.getExclusiveMaximum(), Boolean.TRUE);

        final IntegerProperty positiveInfinityRange = (IntegerProperty) properties.get("positiveInfinityRange");
        assertEquals(positiveInfinityRange.getMinimum(), new BigDecimal(1.0));
        assertNull(positiveInfinityRange.getMaximum());
        assertNull(positiveInfinityRange.getExclusiveMaximum());
        assertNull(positiveInfinityRange.getExclusiveMinimum());

        final IntegerProperty negativeInfinityRange = (IntegerProperty) properties.get("negativeInfinityRange");
        assertNull(negativeInfinityRange.getMinimum());
        assertEquals(negativeInfinityRange.getMaximum(), new BigDecimal(5.0));
        assertNull(negativeInfinityRange.getExclusiveMaximum());
        assertNull(negativeInfinityRange.getExclusiveMinimum());

        final StringProperty stringValues = (StringProperty) properties.get("stringValues");
        assertEquals(stringValues.getEnum(), Arrays.asList("str1", "str2"));

        final DoubleProperty doubleValues = (DoubleProperty) properties.get("doubleValues");
        assertEquals(doubleValues.getMinimum(), new BigDecimal("1.0"));
        assertEquals(doubleValues.getMaximum(), new BigDecimal("8.0"));
        assertEquals(doubleValues.getExclusiveMaximum(), Boolean.TRUE);
        assertNull(doubleValues.getExclusiveMinimum());
    }
}
