package io.swagger.jaxrs2.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import java.util.*;

public class ReaderUtils {
    /**
     * Splits the provided array of strings into an array, using comma as the separator.
     * Also removes leading and trailing whitespace and omits empty strings from the results.
     *
     * @param strings is the provided array of strings
     * @return the resulted array of strings
     */
    public static String[] splitContentValues(String[] strings) {
        final Set<String> result = new LinkedHashSet<String>();

        for (String string : strings) {
            Iterables.addAll(result, Splitter.on(",").trimResults().omitEmptyStrings().split(string));
        }

        return result.toArray(new String[result.size()]);
    }


    public static Optional<List<String>> getStringListFromStringArray(String[] array) {
        if (array == null) {
            return Optional.empty();
        }
        List<String> list = new ArrayList<>();
        for (String value : array) {
            list.add(value);
        }
        return Optional.of(list);
    }
}
