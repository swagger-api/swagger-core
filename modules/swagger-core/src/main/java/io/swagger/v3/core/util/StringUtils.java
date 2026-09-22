package io.swagger.v3.core.util;

import java.security.SecureRandom;

/**
 * Utility methods replacing previously used commons-lang3 StringUtils.
 * Java 17 provides String.isBlank() / isEmpty() directly, so these
 * methods only add null-safe wrapping.
 */
public final class StringUtils {
    public static final String EMPTY = "";

    private static final SecureRandom RANDOM = new SecureRandom();

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
        if (str == null) {
            return null;
        }
        String trimmed = str.trim();
        return trimmed.isEmpty() ? null : trimmed;
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
        return str == null || str.isEmpty() || prefix == null || prefix.isEmpty() || str.startsWith(prefix) ? str : prefix + str;
    }

    /**
     * Capitalizes all the whitespace separated words in a String.
     * Only the first character of each word is changed.
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
     * A {@code null} input String returns {@code null}.
     * Capitalization uses the Unicode title case, normally equivalent to
     * upper case.</p>
     *
     * <pre>
     * capitalize(null)        = null
     * capitalize("")          = ""
     * capitalize("i am FINE") = "I Am FINE"
     * </pre>
     *
     * @param str  the String to capitalize, may be null
     * @return capitalized String, {@code null} if null String input
     */
    public static String capitalize(final String str) {
        if (isEmpty(str)) {
            return str;
        }
        final char[] buffer = str.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (Character.isWhitespace(ch)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }
        return new String(buffer);
    }

    /**
     * Creates a random string of printable ASCII characters (range 32-126).
     *
     * @param count the length of the random string to create
     * @return a random string of the requested length
     * @throws IllegalArgumentException if {@code count < 0}
     */
    public static String randomAscii(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative: " + count);
        }
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            // 126 - 32 + 1 = 95 printable ASCII characters, range [32, 126]
            sb.append((char) (RANDOM.nextInt(95) + 32));
        }
        return sb.toString();
    }
}
