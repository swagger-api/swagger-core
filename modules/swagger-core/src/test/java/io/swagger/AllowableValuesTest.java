package io.swagger;

import io.swagger.models.properties.PropertyBuilder;
import io.swagger.util.AllowableEnumValues;
import io.swagger.util.AllowableRangeValues;
import io.swagger.util.AllowableValuesUtils;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class AllowableValuesTest {

    @Test(description = "parse allowableValues")
    public void allowableValuesTest() {
        final AllowableRangeValues inclusiveRange = (AllowableRangeValues) AllowableValuesUtils.create("range[1, 5]");
        assertEquals(inclusiveRange.getMinimum(), new BigDecimal(1.0));
        assertEquals(inclusiveRange.getMaximum(), new BigDecimal(5.0));
        assertFalse(inclusiveRange.isExclusiveMinimum());
        assertFalse(inclusiveRange.isExclusiveMaximum());
        final Map<PropertyBuilder.PropertyId, Object> map = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class) {
            {
                put(PropertyBuilder.PropertyId.MINIMUM, new BigDecimal(1.0));
                put(PropertyBuilder.PropertyId.MAXIMUM, new BigDecimal(5.0));
            }
        };
        assertEquals(inclusiveRange.asPropertyArguments(), map);

        final AllowableRangeValues exclusiveRange = (AllowableRangeValues) AllowableValuesUtils.create("range(1, 5)");
        assertEquals(exclusiveRange.getMinimum(), new BigDecimal(1.0));
        assertEquals(exclusiveRange.getMaximum(), new BigDecimal(5.0));
        assertTrue(exclusiveRange.isExclusiveMaximum());
        assertTrue(exclusiveRange.isExclusiveMinimum());

        final AllowableRangeValues negativeInfinityRange = (AllowableRangeValues) AllowableValuesUtils.create("range[-infinity, 5]");
        assertNull(negativeInfinityRange.getMinimum());
        assertEquals(negativeInfinityRange.getMaximum(), new BigDecimal(5.0));
        assertFalse(negativeInfinityRange.isExclusiveMaximum());
        assertFalse(negativeInfinityRange.isExclusiveMinimum());

        final AllowableRangeValues positiveInfinityRange = (AllowableRangeValues) AllowableValuesUtils.create("range[1, infinity]");
        assertEquals(positiveInfinityRange.getMinimum(), new BigDecimal(1.0));
        assertNull(positiveInfinityRange.getMaximum());
        assertFalse(positiveInfinityRange.isExclusiveMaximum());
        assertFalse(positiveInfinityRange.isExclusiveMinimum());

        final AllowableRangeValues mixedRange = (AllowableRangeValues) AllowableValuesUtils.create("range[1.0, 8.0)");
        assertEquals(mixedRange.getMinimum(), new BigDecimal("1.0"));
        assertEquals(mixedRange.getMaximum(), new BigDecimal("8.0"));
        assertFalse(mixedRange.isExclusiveMinimum());
        assertTrue(mixedRange.isExclusiveMaximum());

        final AllowableEnumValues stringValues = (AllowableEnumValues) AllowableValuesUtils.create("str1, str2");
        assertEquals(stringValues.getItems(), Arrays.asList("str1", "str2"));
        final Map<PropertyBuilder.PropertyId, Object> stringMap = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class) {
            {
                put(PropertyBuilder.PropertyId.ENUM, Arrays.asList("str1", "str2"));
            }
        };
        assertEquals(stringValues.asPropertyArguments(), stringMap);
    }
}
