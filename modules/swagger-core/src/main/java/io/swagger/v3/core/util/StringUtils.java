package io.swagger.v3.core.util;

/**
 * Utility methods replacing previously used commons-lang3 StringUtils.
 * Java 17 provides String.isBlank() / isEmpty() directly, so these
 * methods only add null-safe wrapping.
 */
public final class StringUtils {
    public static final String EMPTY = "";

    private StringUtils() {
    }

    /**
     * @return {@code true} if the string is null or blank
     */
    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    /**
     * @return {@code true} if the string is not null and not blank
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * @return {@code true} if the string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * @return {@code true} if the string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * @return the trimmed string, or null if the string is null or blank
     */
    public static String trimToNull(String str) {
        return str == null || str.isBlank() ? null : str.trim();
    }

    /**
     * @return {@code true} if both strings are equal (null-safe)
     */
    public static boolean equals(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
     * @return the string with prefix prepended if not already present, null-safe
     */
    public static String prependIfMissing(String str, String prefix) {
        return str == null || str.startsWith(prefix) ? str : prefix + str;
    }

    /**
     * Capitalizes the first character of the given string.
     *
     * @param str the string to capitalize
     * @return the capitalized string, null if null input, empty if empty input
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
