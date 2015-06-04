package io.swagger.jaxrs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AllowableRangeValues implements AllowableValues {

  private static final Pattern RANGE_PATTERN = Pattern.compile("range(\\[|\\()([^,\\s]+)\\s*,\\s*([^\\]\\s]+)\\s*(\\]|\\))");
  private static final String POSITIVE_INFINITY_KEY = "infinity";
  private static final String NEGATIVE_INFINITY_KEY = "-infinity";
  private static final String OPEN_EXCLUSIVE_RANGE_KEY = "(";
  private static final String CLOSE_EXCLUSIVE_RANGE_KEY = ")";

  private final Double minimum;
  private final Double maximum;
  private final Boolean exclusiveMinimum;
  private final Boolean exclusiveMaximum;

  private AllowableRangeValues(Double minimum, Boolean exclusiveMinimum, Double maximum, Boolean exclusiveMaximum) {
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

  public Double getMinimum() {
    return minimum;
  }

  public Double getMaximum() {
    return maximum;
  }

  public boolean isExclusiveMinimum() {
    return exclusiveMinimum;
  }

  public boolean isExclusiveMaximum() {
    return exclusiveMaximum;
  }

  private static Double findRangeValue(String value) {
    if (POSITIVE_INFINITY_KEY.equalsIgnoreCase(value) || NEGATIVE_INFINITY_KEY.equalsIgnoreCase(value)) {
      return null;
    } else {
      return Double.parseDouble(value);
    }
  }

  private static Boolean isExclusiveRange(String value) {
    return OPEN_EXCLUSIVE_RANGE_KEY.equals(value) || CLOSE_EXCLUSIVE_RANGE_KEY.equals(value);
  }
}
