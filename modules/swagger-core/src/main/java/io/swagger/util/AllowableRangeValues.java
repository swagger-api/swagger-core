package io.swagger.util;

import io.swagger.models.properties.PropertyBuilder;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllowableRangeValues implements AllowableValues {

    private static final Pattern RANGE_PATTERN = Pattern.compile("range(\\[|\\()([^,\\s]+)\\s*,\\s*([^\\]\\s]+)\\s*(\\]|\\))");
    private static final String POSITIVE_INFINITY_KEY = "infinity";
    private static final String NEGATIVE_INFINITY_KEY = "-infinity";
    private static final String OPEN_EXCLUSIVE_RANGE_KEY = "(";
    private static final String CLOSE_EXCLUSIVE_RANGE_KEY = ")";

    private final BigDecimal minimum;
    private final BigDecimal maximum;
    private final boolean exclusiveMinimum;
    private final boolean exclusiveMaximum;

    private AllowableRangeValues(BigDecimal minimum, boolean exclusiveMinimum, BigDecimal maximum, boolean exclusiveMaximum) {
        this.minimum = minimum;
        this.exclusiveMinimum = exclusiveMinimum;
        this.maximum = maximum;
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public static AllowableRangeValues create(String allowableValues) {
        final Matcher matcher = RANGE_PATTERN.matcher(allowableValues);
        return matcher.find() ?
            new AllowableRangeValues(findRangeValue(matcher.group(2)), isExclusiveRange(matcher.group(1)), findRangeValue(matcher.group(3)), isExclusiveRange(matcher.group(4))) :
            null;
    }

    private static BigDecimal findRangeValue(String value) {
        if (POSITIVE_INFINITY_KEY.equalsIgnoreCase(value) || NEGATIVE_INFINITY_KEY.equalsIgnoreCase(value)) {
            return null;
        } else {
            return new BigDecimal(value);
        }
    }

    private static boolean isExclusiveRange(String value) {
        return OPEN_EXCLUSIVE_RANGE_KEY.equals(value) || CLOSE_EXCLUSIVE_RANGE_KEY.equals(value);
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public boolean isExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public boolean isExclusiveMaximum() {
        return exclusiveMaximum;
    }

    @Override
    public Map<PropertyBuilder.PropertyId, Object> asPropertyArguments() {
        final Map<PropertyBuilder.PropertyId, Object> map = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
        map.put(PropertyBuilder.PropertyId.MINIMUM, minimum);
        map.put(PropertyBuilder.PropertyId.MAXIMUM, maximum);
        if (exclusiveMinimum) {
            map.put(PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM, exclusiveMinimum);
        }
        if (exclusiveMaximum) {
            map.put(PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM, exclusiveMaximum);
        }
        return map;
    }
}
