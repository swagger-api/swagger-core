package io.swagger.v3.core.util;

import java.math.BigDecimal;

/**
 * Utility methods replacing previously used commons-lang3 NumberUtils.
 */
public final class NumberUtils {

    private NumberUtils() {
    }

    /**
     * @return {@code true} if the string can be parsed as a non-null valid number
     */
    public static boolean isCreatable(String str) {
        if (str == null || str.isBlank()) {
            return false;
        }
        try {
            new BigDecimal(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
