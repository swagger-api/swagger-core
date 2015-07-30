package io.swagger.util;

import org.apache.commons.lang3.StringUtils;

public class AllowableValuesUtils {

    public static AllowableValues create(String values) {
        AllowableValues allowableValues = null;
        if (StringUtils.isNotEmpty(values)) {
            allowableValues = AllowableRangeValues.create(values);
            if (allowableValues == null) {
                allowableValues = AllowableEnumValues.create(values);
            }
        }
        return allowableValues;
    }
}
